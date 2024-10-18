package com.alignedcookie88.fireclient.task.tasks;

import com.alignedcookie88.fireclient.task.Task;

public class RunAsynchronouslyTask extends Task {

    private boolean hasStarted = false;

    private boolean hasFinished = false;

    private final Runnable target;

    public RunAsynchronouslyTask(Runnable target) {
        this.target = target;
    }

    @Override
    protected TickResult onTick() {
        if (!hasStarted) {
            hasStarted = true;
            Thread thread = new Thread(() -> {
                this.target.run();
                this.hasFinished = true;
            });
            thread.start();
            return TickResult.taskIncomplete();
        }

        if (hasFinished)
            return TickResult.taskComplete();

        return TickResult.taskIncomplete();
    }

}
