package com.alignedcookie88.fireclient.sdk;

public abstract class SDKFunction {

    public abstract String getId();

    public void execute(String argString) {
        FireClientSDK.LOGGER.info("Arg String: {}", argString);
        onExecute();
    }

    public abstract void onExecute();

}
