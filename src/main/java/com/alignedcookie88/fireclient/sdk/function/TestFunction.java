package com.alignedcookie88.fireclient.sdk.function;

import com.alignedcookie88.fireclient.sdk.FireClientSDK;
import com.alignedcookie88.fireclient.sdk.SDKFunction;

public class TestFunction extends SDKFunction {
    @Override
    public String getId() {
        return "test_function";
    }

    @Override
    public void onExecute() {
        FireClientSDK.LOGGER.info("Test function called!");
        FireClientSDK.LOGGER.info("Test function called!");
        FireClientSDK.LOGGER.info("Test function called!");
        FireClientSDK.LOGGER.info("Test function called!");
        FireClientSDK.LOGGER.info("Test function called!");
    }
}
