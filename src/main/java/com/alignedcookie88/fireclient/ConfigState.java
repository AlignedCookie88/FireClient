package com.alignedcookie88.fireclient;

public class ConfigState {

    public boolean alwaysBlockPython = false;

    public boolean oldRankTags = false;

    public boolean hideVIPTags = false;

    public boolean showPlotIDsInTab = true;



    public static ConfigState getDefault() {
        return new ConfigState();
    }

    private ConfigState() {}
}
