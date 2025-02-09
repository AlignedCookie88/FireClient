package com.alignedcookie88.fireclient;

public class ConfigState {

    public boolean oldRankTags = false;

    public boolean hideVIPTags = false;

    public boolean showPlotIDsInTab = true;

    public boolean showPlotNamesInTab = true;

    public boolean showPlotWhitelistedInTab = true;

    public boolean showModeInTab = true;


    public boolean logFunctionCalls = false;

    public boolean showLeavePlotInGameMenu = true;

    public float screenSafeArea = 1.0F;


    public boolean apiEnabled = true;

    public boolean apiAuthEnabled = true;


    public boolean dfToolingApiEnabled = true;

    public boolean dfToolingAdminFeatures = false;


    public boolean dfToolingApiAgreement = false;



    public static ConfigState getDefault() {
        return new ConfigState();
    }

    private ConfigState() {}
}
