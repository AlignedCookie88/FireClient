package com.alignedcookie88.fireclient.task.tasks;

public class WaitForConfirmationTask extends RunAsynchronouslyTask {

    private static boolean confirmed = false;
    private static long startTime = 0;
    private static boolean toConfirm = false;

    public WaitForConfirmationTask() {
        super(() -> {
            confirmed = false;
            startTime = System.currentTimeMillis();
            toConfirm = true;
            while (!confirmed) {
                Thread.onSpinWait();
                if (startTime+25000 < System.currentTimeMillis())
                    break;
            }
            toConfirm = false;
        });
    }

    public static void confirm() {
        confirmed = true;
    }

    public static boolean isToConfirm() {
        return toConfirm;
    }

    public static boolean wasConfirmed() {
        return confirmed;
    }

}
