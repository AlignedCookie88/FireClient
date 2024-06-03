package com.alignedcookie88.fireclient;
import dev.dfonline.codeclient.CodeClient;

public class CodeClientIntegration {

    public static void tick() {
        if (CodeClient.location != null && CodeClient.location.name() != "play") {
            State.reset();
        }
    }

}
