package com.alignedcookie88.fireclient.sdk;

import com.alignedcookie88.fireclient.sdk.function.TestFunction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FireClientSDK {

    public static final Logger LOGGER = LoggerFactory.getLogger("FireClient|SDK");

    private static final Map<String, SDKFunction> functionMap = new HashMap<>();

    /**
     * Initialises the FireClient SDK
     */
    public static void init() {
        LOGGER.info("Initialising...");

        // Register functions
        registerFunction(new TestFunction());

        LOGGER.info("Done!");
    }

    /**
     * Registers a function.
     * @param function The function to register.
     */
    private static void registerFunction(@NotNull SDKFunction function) {
        LOGGER.info("Registering function {} ({})", function.getId(), function.getClass());
        functionMap.put(function.getId(), function);
    }


    /**
     * Execute a call from the server.
     */
    public static void executeCall(String functionName, String sdkVersion, String argString) {
        SDKFunction function = functionMap.get(functionName);

        if (function == null) {
            LOGGER.warn("Couldn't find function {}! Reported SDK version: {}", functionName, sdkVersion);
            return;
        }

        function.execute(argString);
    }

}
