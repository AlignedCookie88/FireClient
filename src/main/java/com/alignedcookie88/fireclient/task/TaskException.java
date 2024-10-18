package com.alignedcookie88.fireclient.task;

public class TaskException extends RuntimeException {

    protected TaskException(String message) {
        super(message);
    }


    public static class TaskCurrentlyActive extends TaskException {

        public TaskCurrentlyActive() {
            super("There is currently an active task running.");
        }

    }

}
