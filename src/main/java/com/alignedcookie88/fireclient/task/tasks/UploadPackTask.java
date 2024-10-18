package com.alignedcookie88.fireclient.task.tasks;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.task.Task;
import com.alignedcookie88.fireclient.task.TaskManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UploadPackTask extends Task {

    private File folder;

    private File zipFile;

    private File tempFile;

    private byte[] pack_bytes;

    private State state;


    public static final URL uploadUrl;
    static {
        try {
            uploadUrl = new URL("https://dftooling-github-bot.alignedcookie88.com/upload_pack");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    public UploadPackTask(File pack) {
        if (pack.isDirectory()) {
            folder = pack;
            state = State.ZIP_PACK;
        } else {
            zipFile = pack;
            state = State.READ_PACK;
        }
    }

    @Override
    protected TickResult onTick() {
        return switch (state) {
            case ZIP_PACK -> {
                FireClient.LOGGER.info("Zipping pack...");
                Utility.sendMessage(Text.literal("Zipping..."));

                tempFile = getTemporaryFile();
                zipFile = getTemporaryFile();

                zipFolder(folder, zipFile);

                state = State.READ_PACK;

                yield TickResult.taskIncomplete();
            }
            case READ_PACK -> {
                FireClient.LOGGER.info("Reading pack...");
                try {
                    InputStream stream = new FileInputStream(zipFile);
                    pack_bytes = stream.readAllBytes();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                state = State.SEND_HTTP;
                yield TickResult.taskIncomplete();
            }
            case SEND_HTTP -> TickResult.waitForBackgroundJob(() -> {
                try {
                    Utility.sendMessage(Text.literal("Uploading..."));
                    FireClient.LOGGER.info("Uploading pack to server at {}...", uploadUrl);

                    // Setup
                    HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setInstanceFollowRedirects(true);
                    connection.setConnectTimeout(25000);
                    connection.setReadTimeout(25000);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("UploaderName", MinecraftClient.getInstance().getGameProfile().getName());
                    OutputStream stream = connection.getOutputStream();
                    stream.write(pack_bytes);
                    stream.flush();
                    stream.close();

                    // Handle
                    int response = connection.getResponseCode();
                    if (response > 199 && response < 300) {
                        InputStream resultStream = connection.getInputStream();
                        byte[] resultData = resultStream.readAllBytes();
                        resultStream.close();
                        String resultString = new String(resultData, StandardCharsets.UTF_8);
                        JsonObject object = JsonParser.parseString(resultString).getAsJsonObject();
                        String downloadUrl = object.get("uri").getAsString();
                        MutableText uploadMsg = Text.literal("The pack has been uploaded! URL: ").append(
                                Text.literal(downloadUrl).formatted(Formatting.AQUA).formatted(Formatting.UNDERLINE)
                        ).append(
                                Text.literal(" (Click to copy!)").formatted(Formatting.GRAY).formatted(Formatting.ITALIC)
                        );
                        uploadMsg.setStyle(uploadMsg.getStyle()
                                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, downloadUrl))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to copy!")))
                        );
                        Utility.sendMessage(uploadMsg);
                    } else {
                        Utility.sendMessage(Text.literal("There was an error uploading the pack."));
                    }
                } catch (Throwable throwable) {
                    Utility.sendMessage(Text.literal("An unexpected error occurred whilst uploading the pack. See the game log for more details."));
                    FireClient.LOGGER.error("An error occurred whilst upload the resource pack.", throwable);
                }
                this.state = State.COMPLETED;
            });
            case COMPLETED -> {

                if (tempFile != null) {
                    tempFile.delete();
                }

                yield TickResult.taskComplete();
            }
        };
    }

    private enum State {
        ZIP_PACK,
        READ_PACK,
        SEND_HTTP,
        COMPLETED
    }


    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("uploadpack")
                .then(ClientCommandManager.argument("pack", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            for (String file : getResourcePacks()) {
                                builder.suggest(StringArgumentType.escapeIfRequired(file));
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String pack = StringArgumentType.getString(context, "pack");
                            if (!getResourcePacks().contains(pack)) {
                                context.getSource().sendError(Text.literal("The pack \""+pack+"\" does not exist or is not valid. Please place the pack in the resourcepacks folder."));
                                return 1;
                            }

                            TaskManager.startTaskFromCommand(context, new UploadPackTask(getPack(pack)));

                            return 1;
                        })
                )
        );
    }


    public static Path getResourcePackDir() {
        return FabricLoader.getInstance().getGameDir().resolve("resourcepacks");
    }

    public static List<String> getResourcePacks() {

        Path path = getResourcePackDir();
        File dir = path.toFile();

        List<String> files = new ArrayList<>();

        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) { // Dir
                if (ArrayUtils.contains(file.list(), "pack.mcmeta"))
                    files.add(file.getName());
            } else { // File
                if (file.getName().endsWith(".zip"))
                    files.add(file.getName());
            }
        }

        return files;
    }

    public static File getPack(String name) {
        return getResourcePackDir().resolve(name).toFile();
    }

    public static File getTemporaryFile() {
        return FabricLoader.getInstance().getGameDir().resolve("temp-pack.zip").toFile();
    }

    private static void zipFolder(File folder, File zip) {
        try {
            zip.createNewFile();
            FileOutputStream fos = new FileOutputStream(zip);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zipFolderR(zos, "", folder);
            zos.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void zipFolderR(ZipOutputStream zos, String baseDir, File folder) throws IOException {
        for (File file : folder.listFiles()) {
            String zipPath = baseDir+"/"+file.getName();
            if (zipPath.startsWith("/"))
                zipPath = zipPath.replaceFirst("/", "");
            if (file.isDirectory()) {
                zipFolderR(zos, zipPath, file);
                continue;
            }

            FileInputStream stream = new FileInputStream(file);
            ZipEntry entry = new ZipEntry(zipPath);
            zos.putNextEntry(entry);
            zos.write(stream.readAllBytes());
            zos.closeEntry();
            stream.close();
        }
    }

}
