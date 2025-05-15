package com.alignedcookie88.fireclient.sdk.function;

import com.alignedcookie88.fireclient.sdk.FireClientSDK;
import com.alignedcookie88.fireclient.sdk.SDKArgument;
import com.alignedcookie88.fireclient.sdk.SDKFunction;
import com.alignedcookie88.fireclient.sdk.argument.StringArgument;

public class TestFunction extends SDKFunction {

    private static final StringArgument ARG_DATA = new StringArgument("data");

    @Override
    public String getId() {
        return "test_function";
    }

    @Override
    public void onExecute() {
        FireClientSDK.LOGGER.info("Test function called! Data: {}", ARG_DATA.getValue());
    }

    @Override
    public SDKArgument<?>[] getArguments() {
        return new SDKArgument<?>[] {
                ARG_DATA
        };
    }
}
