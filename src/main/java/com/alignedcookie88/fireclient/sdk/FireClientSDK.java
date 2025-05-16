package com.alignedcookie88.fireclient.sdk;

import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.sdk.function.TestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FireClientSDK {

    public static final Logger LOGGER = LoggerFactory.getLogger("FireClient|SDK");

    public static final String SDK_VERSION = "2.0";

    private static final Map<String, SDKFunction> functionMap = new HashMap<>();

    private static final List<Runnable> queued = new ArrayList<>();
    private static volatile boolean isExecutingQueue = false;

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
     * Gets a function from its id
     * @param id The ID of the function
     * @return The SDKFunction object
     */
    public static SDKFunction getFunction(String id) {
        return functionMap.get(id);
    }

    /**
     * Gets a list of all functions registered
     */
    public static Set<String> getFunctions() {
        return functionMap.keySet();
    }


    /**
     * Execute a call from the server.
     * Execution may be delayed and as such this function may return before the SDK function has completed, but this is also not guaranteed.
     */
    public static void executeCall(String functionName, String sdkVersion, String argString) {
        SDKFunction function = functionMap.get(functionName);

        if (function == null) {
            LOGGER.warn("Couldn't find function {}! Plot SDK version is {}, client is {}", functionName, sdkVersion, SDK_VERSION);
            return;
        }

        if (!RenderSystem.isOnRenderThreadOrInit() && function.executeSynchronously()) { // Make sure to execute the function synchronously when requested.
            while (isExecutingQueue)
                Thread.onSpinWait();
            queued.add(() -> executeCall(functionName, sdkVersion, argString));
            return;
        }

        try {
            function.execute(argString);
        } catch (RuntimeException e) {
            LOGGER.error("Error executing function {}! Plot SDK version is {}, client is {}", functionName, sdkVersion, SDK_VERSION, e);
            Utility.sendStyledMessage("There was an error executing an SDK call from the plot. See the game logs for more details.");
        }
    }

    /**
     * Ticks the SDK and executes any queued calls.
     */
    public static void tick() {
        isExecutingQueue = true;
        for (Runnable runnable : queued) {
            runnable.run();
        }
        queued.clear();
        isExecutingQueue = false;
    }


    public static Text styleSDKFunctionName(Text name) {
        return Utility.miniMessage("<gradient:#ff5a00:#ffa97a>\uD83D\uDD25 FireClient SDK:<reset> ").copy().append(name);
    }

}
