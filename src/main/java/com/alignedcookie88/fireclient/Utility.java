package com.alignedcookie88.fireclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.text.Text;

public class Utility {
    /**
     * Send a message to the player, but styled.
     * @param message The message to send.
     */
    public static void sendStyledMessage(String message) {
        Text prefix = componentToText(MiniMessage.miniMessage().deserialize("<dark_grey>[<gradient:#FF5A00:#FFA500>FireClient<dark_grey>]:<reset> "));
        sendMessage(prefix.copy().append(Text.literal(message)));
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
        FireClient.LOGGER.info("Running plot command @{}", command);
        MinecraftClient.getInstance().getNetworkHandler().sendChatMessage("@"+command);
    }

    public static Text componentToText(Component component) {
        String json = JSONComponentSerializer.json().serialize(component);
        return new Text.Serializer().deserialize(JsonParser.parseString(json), null, null);
    }

    public static Component textToComponent(Text text) {
        if (text.toString().isEmpty())
            return Component.empty();
        JsonElement elem = new Text.Serializer().serialize(text, null, null);
        return JSONComponentSerializer.json().deserialize(elem.toString());
    }
}
