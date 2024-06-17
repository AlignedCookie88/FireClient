package com.alignedcookie88.fireclient;


public class State {

    public static boolean canMove = true;

    public static CustomScreen screen = null;

    public static Boolean pythonExecution = null;

    public static String ability1Fn = null;

    public static String ability2Fn = null;

    public static String ability3Fn = null;

    public static void reset() {
        CommandQueue.clearPlotCommands();

        canMove = true;
        screen = null;
        pythonExecution = null;
        ability1Fn = null;
        ability2Fn = null;
        ability3Fn = null;

        if (Config.state.alwaysBlockPython)
            pythonExecution = false;
    }
}
