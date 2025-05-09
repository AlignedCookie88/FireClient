package com.alignedcookie88.fireclient.task.tasks;

import com.alignedcookie88.fireclient.Config;
import com.alignedcookie88.fireclient.DFToolingAPITermsAgreeScreen;
import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.Utility;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DFToolingApiTask extends RunAsynchronouslyTask {

    public static final Endpoint ACTIONDUMP_ADMIN_UPLOAD = new Endpoint("/actiondump/v0/admin/upload", "POST", true);
    public static final Endpoint ACTIONDUMP_ADMIN_DELETE = new Endpoint("/actiondump/v0/admin/delete", "DELETE", true);

    public static final Endpoint AUTH_GET_KEY = new Endpoint("/auth/v0/fireclient/get_key", "GET", false);
    public static final Endpoint AUTH_FINALISE = new Endpoint("/auth/v0/fireclient/finalise", "POST", false);

    public static final Endpoint PACKS_UPLOAD = new Endpoint("/packs/v0/upload", "POST", true);


    public static final String ROOT = "https://api.dftooling.dev/api";

    private static String token;

    public DFToolingApiTask(Request request, Consumer<Result> resultConsumer) {
        super(() -> {
            if (token == null && request.requiresAuth()) {
                Result badResult = auth();
                if (badResult != null) {
                    resultConsumer.accept(badResult);
                    return;
                }
            }

            Result result = request.run();

            if (result.isAuthInvalid()) { // In case the token expired or smth
                Result badResult = auth();
                if (badResult != null) {
                    resultConsumer.accept(badResult);
                    return;
                }
                result = request.run();
            }

            resultConsumer.accept(result);
        });
    }

    @Override
    protected TickResult onTick() {
        if (!Config.state.dfToolingApiAgreement) {
            FireClient.openOnNextTick = new DFToolingAPITermsAgreeScreen(this);
            return TickResult.taskComplete();
        }

        return super.onTick();
    }

    private static Result auth() {

        Result public_key_result = new Request(AUTH_GET_KEY).run();
        if (!public_key_result.success())
            return public_key_result;
        byte[] public_key_bytes = public_key_result.data();

        PublicKey publicKey;
        try {
            publicKey = NetworkEncryptionUtils.decodeEncodedRsaPublicKey(public_key_bytes);
        } catch (NetworkEncryptionException e) {
            throw new RuntimeException(e);
        }

        byte[] shared_secret = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(shared_secret);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        MinecraftClient client = MinecraftClient.getInstance();
        GameProfile profile = client.getGameProfile();


        SecretKey secretKey;
        try {
            secretKey = NetworkEncryptionUtils.generateSecretKey();
        } catch (NetworkEncryptionException e) {
            throw new RuntimeException(e);
        }

        FireClient.LOGGER.info("Secret key: {}", Base64.getEncoder().encodeToString(secretKey.getEncoded()));

        byte[] encryptedSecretKey;
        try {
            encryptedSecretKey = NetworkEncryptionUtils.encrypt(publicKey, secretKey.getEncoded());
        } catch (NetworkEncryptionException e) {
            throw new RuntimeException(e);
        }

        String serverHash = DigestUtils.sha1Hex(ArrayUtils.addAll(secretKey.getEncoded(), public_key_bytes));
        serverHash = serverHash.substring(0, 30);


        FireClient.LOGGER.info("Server hash: {}", serverHash);

        FireClient.LOGGER.info("Sending request to sessionserver");

        try {
            client.getSessionService().joinServer(
                    profile.getId(),
                    client.getSession().getAccessToken(),
                    serverHash
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }

        Result result = new Request(AUTH_FINALISE, argsBuilder()
                .add("username", profile.getName())
                .build(), stream -> stream.write(encryptedSecretKey)).run();

        if (result.success())
            token = result.getJson().getAsJsonObject().get("token").getAsString();
        else return result;

        return null;
    }



    public static class Endpoint {

        private final String endpoint;
        private final String method;
        private final boolean requiresAuth;

        private Endpoint(String endpoint, String method, boolean requiresAuth) {
            this.endpoint = endpoint;
            this.method = method;
            this.requiresAuth = requiresAuth;
        }

        private boolean acceptsData() {
            return method.equals("POST") || method.equals("PUT");
        }

        private URL getUrl(Map<String, String> args) throws MalformedURLException {
            if (requiresAuth)
                args.put("token", token);
            StringBuilder builder = new StringBuilder();
            char start_char = '?';
            for (String key : args.keySet()) {
                builder.append(start_char);
                builder.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
                builder.append('=');
                builder.append(URLEncoder.encode(args.get(key), StandardCharsets.UTF_8));
                start_char = '&';
            }
            return new URL(ROOT+endpoint+builder);
        }

    }

    public interface BodyWriter {

        void write(OutputStream stream) throws IOException;

    }

    public record Request(Endpoint endpoint, Map<String, String> args, BodyWriter bodyWriter) {

        public Request(Endpoint endpoint, Map<String, String> args) {
            this(endpoint, args, null);
        }

        public Request(Endpoint endpoint) {
            this(endpoint, new HashMap<>());
        }


        private Result run() {

            if (!Config.state.dfToolingApiEnabled)
                return new Result(-1);

            try {

                URL uploadUrl = endpoint.getUrl(args);

                HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();
                connection.setRequestMethod(endpoint.method);
                connection.setRequestProperty("User-Agent", FireClient.USER_AGENT);
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout(25000);
                connection.setReadTimeout(25000);

                if (endpoint.acceptsData() && bodyWriter != null) {
                    connection.setDoOutput(true);
                    OutputStream stream = connection.getOutputStream();
                    bodyWriter.write(stream);
                    stream.flush();
                    stream.close();
                }

                int code = connection.getResponseCode();

                int retryAfter = Utility.parseIntOrDefault(connection.getHeaderField("Retry-After"), -1);

                InputStream stream = code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream();
                byte[] data = stream.readAllBytes();
                stream.close();

                return new Result(code, data, retryAfter);

            } catch (Exception ignored) {

            }

            return new Result(0);
        }

        private boolean requiresAuth() {
            return endpoint.requiresAuth;
        }

    }

    public record Result(int status, byte[] data, int retryAfter) {

        public Result(int status) {
            this(status, new byte[] {}, -1);
        }

        public boolean isAuthInvalid() {
            return status == 401;
        }

        public boolean success() {
            return status >= 200 && status < 300;
        }

        private String formatRetryAfter() {
            if (retryAfter == -1)
                return "[Unknown]";
            return Utility.formatWaitTime(retryAfter);
        }

        public MutableText getStatusMessage() {
            if (success())
                return Text.literal("Success.");

            return switch (status) {
                case -1 -> Text.literal("The DFTooling API is disabled in the FireClient config. Please enable it to use this feature.");
                case 0 -> Text.literal("The connection to the DFTooling API failed. Is the server down?");
                case 400 -> Text.literal("The sent data was invalid. Did you enter everything correctly?");
                case 401 -> Text.literal("The authentication was invalid.");
                case 403 -> Text.literal("You are not authorised to do this.");
                case 410 -> Text.literal("The action you are trying to complete is no longer possible due to backend API changes. Please ensure you are on the latest version of FireClient.");
                case 413 -> Text.literal("The data exceeds the size limit.");
                case 415 -> Text.literal("The data you sent is not of a type that the server expected, or it may be unreadable to the server. Please make sure the data is valid and readable.");
                case 429 -> Text.literal("You are doing this too quickly. Please try again in "+formatRetryAfter()+".");
                case 502 -> Text.literal("The DFTooling API is currently unavailable.");
                case 503 -> Text.literal("The DFTooling API is currently unavailable or overloaded.");
                case 504 -> Text.literal("The DFTooling API is currently unavailable, overloaded or very broken.");

                default -> Text.literal("Unexpected status %s. Click this message for more info.".formatted(status))
                        .styled(style -> style.withClickEvent(
                                new ClickEvent(
                                        ClickEvent.Action.OPEN_URL,
                                        "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/" + status
                                )
                        ));
            };
        }

        public MutableText getStatusMessageResourcePack() {
            MutableText msg = getStatusMessage();
            if (status == 415) {
                return msg.append(Text.literal(" (Note: If you are using packsquash to protect your resource pack, it may be causing the DFTooling API to be unable to verify that the file is indeed a resource pack!)").formatted(Formatting.GRAY, Formatting.ITALIC));
            }
            return msg;
        }

        public JsonElement getJson() {
            String string = new String(data);
            return JsonParser.parseString(string);
        }

    }

    public static ArgsBuilder argsBuilder() {
        return new ArgsBuilder();
    }

    public static class ArgsBuilder {

        private final Map<String, String> map;

        private ArgsBuilder() {
            map = new HashMap<>();
        }

        public ArgsBuilder add(String key, String val) {
            map.put(key, val);
            return this;
        }

        public Map<String, String> build() {
            return map;
        }

    }

}
