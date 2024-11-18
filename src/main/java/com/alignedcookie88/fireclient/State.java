package com.alignedcookie88.fireclient;


import com.alignedcookie88.fireclient.hud.HudElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class State {

    public static boolean canMove = true;

    public static CustomScreen screen = null;

    public static Boolean pythonExecution = null;

    public static String ability1Fn = null;

    public static String ability2Fn = null;

    public static String ability3Fn = null;

    public static List<HudElement> hud = new ArrayList<>();

    public static boolean plotCommandsForChat = false;

    public static PostEffectProcessor postProcessor = null;

    public static void reset() {
        CommandQueue.clearPlotCommands();

        canMove = true;
        screen = null;
        pythonExecution = null;
        ability1Fn = null;
        ability2Fn = null;
        ability3Fn = null;
        plotCommandsForChat = false;
        setPostProcessor(null);

        hud = new ArrayList<>();

        if (Config.state.alwaysBlockPython)
            pythonExecution = false;
    }

    public static void setPostProcessor(Identifier id) {
        if (postProcessor != null) {
            postProcessor.close();
        }

        if (id == null)
            return;

        id = Identifier.of(id.getNamespace(), "shaders/post/"+id.getPath()+".json");

        MinecraftClient client = MinecraftClient.getInstance();

        try {
            postProcessor = new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), id);
            postProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
        } catch (IOException var3) {
            Utility.sendStyledMessage("Failed to load the post-processing shader, see the game log for more details.");
            FireClient.LOGGER.warn("Failed to load post-processing shader: {}", id, var3);
        } catch (JsonSyntaxException var4) {
            Utility.sendStyledMessage("Failed to parse the post-processing shader, see the game log for more details.");
            FireClient.LOGGER.warn("Failed to parse post-processing shader: {}", id, var4);
        }

    }
}
