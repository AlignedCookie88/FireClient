package com.alignedcookie88.fireclient.api.packet;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.api.ApiConnection;
import com.alignedcookie88.fireclient.api.FireClientApiException;
import com.alignedcookie88.fireclient.util.JsonObjectBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class DoAuthPacket extends ApiIncomingPacket {
    @Override
    public String getType() {
        return "do_auth";
    }

    public static final Logger LOGGER = FireClient.createLogger("Auth");

    @Override
    public void receive(ApiJsonReader data, ApiConnection connection) {
        String public_key_str = data.getString("publicKey");

        LOGGER.info("Public key: {}", public_key_str);

        byte[] public_key_bytes = Base64.getDecoder().decode(public_key_str);

        PublicKey publicKey;
        try {
            publicKey = NetworkEncryptionUtils.decodeEncodedRsaPublicKey(public_key_bytes);
        } catch (NetworkEncryptionException e) {
            throw new FireClientApiException("Error whilst decoding public key.", e);
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

        LOGGER.info("Secret key: {}", Base64.getEncoder().encodeToString(secretKey.getEncoded()));

        byte[] encryptedSecretKey;
        try {
            encryptedSecretKey = NetworkEncryptionUtils.encrypt(publicKey, secretKey.getEncoded());
        } catch (NetworkEncryptionException e) {
            throw new RuntimeException(e);
        }

        String serverHash = DigestUtils.sha1Hex(ArrayUtils.addAll(secretKey.getEncoded(), public_key_bytes));
        serverHash = serverHash.substring(0, 30);


        LOGGER.info("Server hash: {}", serverHash);

        LOGGER.info("Sending request to sessionserver");

        try {
            client.getSessionService().joinServer(
                    profile.getId(),
                    client.getSession().getAccessToken(),
                    serverHash
            );
        } catch (AuthenticationException e) {
            connection.sendError(e);
            return;
        }

        Utility.sendToast(
                Text.literal("Logged in to ").append(
                        Text.literal(connection.applicationName).formatted(Formatting.GRAY).append(
                                Text.literal(" successfully!").formatted(Formatting.WHITE)
                        )
                )
        );

        connection.sendSuccessWithResponse(new JsonObjectBuilder()
                        .withString("secretKey", Base64.getEncoder().encodeToString(encryptedSecretKey))
                        .withString("username", profile.getName())
                .build());
    }
}
