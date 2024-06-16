package com.alignedcookie88.fireclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.ArrayList;
import java.util.List;

public class CommandQueue {

    private static int spam = 200;

    private static final List<String> commands = new ArrayList<>();

    public static void tick() {
        // Decrement spam
        if (spam > 0)
            spam--;

        // Send commands
        if (!commands.isEmpty()) {

            List<Integer> toRemove = new ArrayList<>();
            int current = 0;

            FireClient.LOGGER.info("Command Queue Length: {}, Spam Cooldown: {}", commands.size(), spam);

            for (String command : commands) {
                if (spam < 150) { // Leave clearance
                    FireClient.LOGGER.info("Running command: {}", command);
                    MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);
                    spam += 20;
                    toRemove.add(current);
                }
                current++;
            }

            int removed = 0;

            for (Integer index : toRemove) {
                commands.remove((int) index-removed);
                removed++;
            }
        }
    }

    public static void queueCommand(String command) {
        commands.add(command);
    }

    public static int getQueueLength() {
        return commands.size();
    }

}
