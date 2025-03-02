package com.alignedcookie88.fireclient;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class CommandQueue {

    private static int spam = 200;

    private static final List<String> commands = new ArrayList<>();

    private static final List<String> plotCommands = new ArrayList<>();

    public static void tick() {
        // Decrement spam
        if (spam > 0)
            spam--;

        // Send commands
        if (!commands.isEmpty()) {

            List<Integer> toRemove = new ArrayList<>();
            int current = 0;

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


        // Send plot commands
        if (!plotCommands.isEmpty()) {

            List<Integer> toRemove = new ArrayList<>();
            int current = 0;

            for (String command : plotCommands) {
                if (spam < 150) { // Leave clearance
                    FireClient.LOGGER.info("Running plot command: {}", command);
                    MinecraftClient.getInstance().player.networkHandler.sendChatMessage("@"+command);
                    spam += 20;
                    toRemove.add(current);
                }
                current++;
            }

            int removed = 0;

            for (Integer index : toRemove) {
                plotCommands.remove((int) index-removed);
                removed++;
            }
        }
    }

    public static void queueCommand(String command) {
        commands.add(command);
    }

    public static void queuePlotCommand(String plotCommand) {
        plotCommands.add(plotCommand);
    }

    public static int getQueueLength() {
        return commands.size();
    }

    /**
     * Should be called when the player chats.
     */
    public static void playerChatted() {
        spam += 20;
    }

    /**
     * Should be called when the player leaves a plot. (spamming @balls in chat repeatedly is not good)
     */
    public static void clearPlotCommands() {
        plotCommands.clear();
    }

    public static boolean isSafeToChat() {
        return !(spam > 125);
    }

}
