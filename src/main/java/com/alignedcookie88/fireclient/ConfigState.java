package com.alignedcookie88.fireclient;

public class ConfigState {

    public boolean alwaysBlockPython = true;

    public boolean oldRankTags = false;

    public boolean hideVIPTags = false;

    public boolean showPlotIDsInTab = true;

    public boolean showPlotNamesInTab = true;

    public boolean showPlotWhitelistedInTab = true;



    public static ConfigState getDefault() {
        return new ConfigState();
    }

    private ConfigState() {}
}
