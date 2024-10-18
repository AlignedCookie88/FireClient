package com.alignedcookie88.fireclient.task;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class TaskManager {

    private static Task currentTask = null;

    /**
     * Checks if a task is active
     * @return Whether a task is active
     */
    public static boolean isTaskActive() {
        return currentTask != null && !currentTask.completed;
    }

    /**
     * Sets the current task
     * @param task The task to set
     * @throws TaskException.TaskCurrentlyActive If there is a currently active task
     */
    public static void setCurrentTask(Task task) throws TaskException.TaskCurrentlyActive {
        if (isTaskActive())
            throw new TaskException.TaskCurrentlyActive();

        currentTask = task;
    }

    /**
     * Ticks the currently active task, if there is one
     */
    public static void tick() {
        if (isTaskActive()) {
            currentTask.tick();
        }
    }


    /**
     * Starts a task, should be used when the user runs a command
     * @param context The command context
     * @param toRun The task to run
     * @return Whether the task was started
     */
    public static boolean startTaskFromCommand(CommandContext<FabricClientCommandSource> context, Task toRun) {
        if (isTaskActive()) {
            context.getSource().sendError(Text.literal("FireClient is currently running another task. Please wait for that task to finish."));
            return false;
        }

        setCurrentTask(toRun);
        return true;
    }

}
