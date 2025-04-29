package com.alignedcookie88.fireclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Arrays;

public class Utility {
    /**
     * Send a message to the player, but styled.
     * @param message The message to send.
     */
    public static void sendStyledMessage(String message) {
        Text msg = componentToText(MiniMessage.miniMessage().deserialize("<dark_grey>[<gradient:#FF5A00:#FFA500>FireClient<dark_grey>]:<reset> "+message));
        sendMessage(msg);
    }

    /**
     * Send a message to the player.
     * @param message The message to send.
     */
    public static void sendMessage(Component message) {
        sendMessage(componentToText(message));
    }

    /**
     * Send a message to the player.
     * @param message The message to send.
     */
    public static void sendMessage(Text message) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.sendMessage(message, false);
    }

    /**
     * Send a message to the player.
     * @param message The message to send.
     */
    public static void sendMessage(String message) {
        sendMessage(Text.literal(message));
    }

    public static int getRemoteSlot(int slot) {
        if (0 <= slot && slot <= 8) {
            return slot + 36;
        } else return slot;
    }

    public static void sendInventory() {
        for (int i = 0; i <= 35; i++) {
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(getRemoteSlot(i), MinecraftClient.getInstance().player.getInventory().getStack(i)));
        }
    }

    public static boolean giveItem(ItemStack stack) {
        if (!MinecraftClient.getInstance().player.isCreative()) {
            Utility.sendMessage(Text.literal("You must be in creative to receive an item.").withColor(0xFF0000));
            return false;
        }

        boolean success = MinecraftClient.getInstance().player.getInventory().insertStack(stack);
        sendInventory();

        if (!success) {
            Utility.sendMessage(Text.literal("There is not enough space in your inventory to receive an item.").withColor(0xFF0000));
        }

        return success;
    }

    public static void runPlotCommand(String command) {
        CommandQueue.queuePlotCommand(command);
    }

    public static Text componentToText(Component component) {
        String json = JSONComponentSerializer.json().serialize(component);
        return new Text.Serializer(getRegistryWrapper()).deserialize(JsonParser.parseString(json), null, null);
    }

    public static Component textToComponent(Text text) {
        if (text.toString().isEmpty())
            return Component.empty();
        JsonElement elem = new Text.Serializer(getRegistryWrapper()).serialize(text, null, null);
        return JSONComponentSerializer.json().deserialize(elem.toString());
    }

    public static RegistryWrapper.WrapperLookup getRegistryWrapper() {
        return MinecraftClient.getInstance().world.getRegistryManager();
    }

    /**
     * Remove legacy formatting codes from miniMessage, replacing them with their appropriate miniMessage equivalent
     * @param miniMessage The miniMessage to sanitise
     * @return The sanitised miniMessage
     */
    public static String sanitiseMiniMessage(String miniMessage) {
        miniMessage = sanitiseMiniMessage(miniMessage, "§");
        miniMessage = sanitiseMiniMessage(miniMessage, "&");
        return miniMessage;
    }

    /**
     * Remove legacy formatting codes from miniMessage, with the provided code start (usually § or &), replacing them with their appropriate miniMessage equivalent
     * @param miniMessage The miniMessage to sanitise
     * @return The sanitised miniMessage
     */
    private static String sanitiseMiniMessage(String miniMessage, String code_start) {
        miniMessage = miniMessage.replace(code_start+"0", "<black>");
        miniMessage = miniMessage.replace(code_start+"1", "<dark_blue>");
        miniMessage = miniMessage.replace(code_start+"2", "<dark_green>");
        miniMessage = miniMessage.replace(code_start+"3", "<dark_aqua>");
        miniMessage = miniMessage.replace(code_start+"4", "<dark_red>");
        miniMessage = miniMessage.replace(code_start+"5", "<dark_purple>");
        miniMessage = miniMessage.replace(code_start+"6", "<gold>");
        miniMessage = miniMessage.replace(code_start+"7", "<grey>");
        miniMessage = miniMessage.replace(code_start+"8", "<dark_grey>");
        miniMessage = miniMessage.replace(code_start+"9", "<blue>");
        miniMessage = miniMessage.replace(code_start+"a", "<green>");
        miniMessage = miniMessage.replace(code_start+"b", "<aqua>");
        miniMessage = miniMessage.replace(code_start+"c", "<red>");
        miniMessage = miniMessage.replace(code_start+"d", "<light_purple>");
        miniMessage = miniMessage.replace(code_start+"e", "<yellow>");
        miniMessage = miniMessage.replace(code_start+"f", "<white>");
        miniMessage = miniMessage.replace(code_start+"k", "<obfuscated>");
        miniMessage = miniMessage.replace(code_start+"l", "<bold>");
        miniMessage = miniMessage.replace(code_start+"m", "<strikethrough>");
        miniMessage = miniMessage.replace(code_start+"n", "<underlined>");
        miniMessage = miniMessage.replace(code_start+"o", "<italic>");
        miniMessage = miniMessage.replace(code_start+"r", "<reset>");
        miniMessage = miniMessage.replace(code_start+"A", "<green>");
        miniMessage = miniMessage.replace(code_start+"B", "<aqua>");
        miniMessage = miniMessage.replace(code_start+"C", "<red>");
        miniMessage = miniMessage.replace(code_start+"D", "<light_purple>");
        miniMessage = miniMessage.replace(code_start+"E", "<yellow>");
        miniMessage = miniMessage.replace(code_start+"F", "<white>");
        miniMessage = miniMessage.replace(code_start+"K", "<obfuscated>");
        miniMessage = miniMessage.replace(code_start+"L", "<bold>");
        miniMessage = miniMessage.replace(code_start+"M", "<strikethrough>");
        miniMessage = miniMessage.replace(code_start+"N", "<underlined>");
        miniMessage = miniMessage.replace(code_start+"O", "<italic>");
        miniMessage = miniMessage.replace(code_start+"R", "<reset>");
        return miniMessage;
    }

    public static boolean isCreative() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return player != null && player.isCreative();
    }

    public static void sendToast(Text title, Text description) {
        MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION, title, description));
    }

    public static void sendToast(Text message) {
        sendToast(Text.literal("FireClient"), message);
    }


    public static boolean isPlayingDiamondFire() {
        if (MinecraftClient.getInstance().world == null)
            return false;

        if (MinecraftClient.getInstance().isInSingleplayer())
            return false;

        if (FireClient.overrideDiamondFireDetection)
            return true;

        ServerInfo currentServer = MinecraftClient.getInstance().getCurrentServerEntry();

        if (currentServer == null)
            return false;

        String[] dfAddresses = {
                "mcdiamondfire.com",
                "play.mcdiamondfire.com",
                "node1.mcdiamondfire.com",
                "node2.mcdiamondfire.com",
                "node3.mcdiamondfire.com",
                "node4.mcdiamondfire.com",
                "node5.mcdiamondfire.com",
                "node6.mcdiamondfire.com",
                "node7.mcdiamondfire.com",
                "beta.mcdiamondfire.com",
                "dev.mcdiamondfire.com",
                "dev2.mcdiamondfire.com"
        };

        if (currentServer.address.endsWith(".diamondfire.games"))
            return true;

        return Arrays.stream(dfAddresses).toList().contains(currentServer.address);
    }


    public static Text miniMessage(String miniMessage) {
        return componentToText(MiniMessage.miniMessage().deserialize(miniMessage));
    }

    public static int parseIntOrDefault(String s, int d) {
        if (s == null)
            return d;

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return d;
        }
    }

    public static String formatWaitTime(int time) {
        return DurationFormatUtils.formatDuration((long) time * 1000, "H:mm:ss", false);
    }
}
