package com.alignedcookie88.fireclient.task;

import com.alignedcookie88.fireclient.task.tasks.RunAsynchronouslyTask;

public abstract class Task {

    public boolean completed = false;

    private Task childTask = null;

    /**
     * Calls this task's `onTick` method, or the child task's `tick` method.
     */
    public void tick() {
        // Handle the child task
        if (childTask != null) {
            childTask.tick();
            if (childTask.completed) {
                childTask = null;
            }
            return;
        }

        // Handle this task
        TickResult result = onTick();
        result.handle(this);
    }


    protected abstract TickResult onTick();



    public static abstract class TickResult {

        /**
         * Handles the result for a specific task
         * @param task The task that should be modified by the result
         */
        public abstract void handle(Task task);


        /**
         * This result should be returned when the task has completed
         */
        public static TickResult taskComplete() {
            return new TickResult() {
                @Override
                public void handle(Task task) {
                    task.completed = true;
                }
            };
        }

        /**
         * This result should be returned when the task hasn't finished yet, but needs to allow the game to tick.
         */
        public static TickResult taskIncomplete() {
            return new TickResult() {
                @Override
                public void handle(Task task) {

                }
            };
        }

        /**
         * This result should be returned the task requires another task to complete before it can continue.
         * @param taskToWaitFor The task it needs to wait for
         */
        public static TickResult waitForTask(Task taskToWaitFor) {
            return new TickResult() {
                @Override
                public void handle(Task task) {
                    task.childTask = taskToWaitFor;
                }
            };
        }


        /**
         * This result should be returned when the task needs to wait for a slow-running background job to complete. (e.g. making a HTTP request)
         * @param target The runnable to run in the background
         */
        public static TickResult waitForBackgroundJob(Runnable target) {
            return TickResult.waitForTask(new RunAsynchronouslyTask(target));
        }

    }
}
