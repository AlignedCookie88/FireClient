package com.alignedcookie88.fireclient.integration;
import com.alignedcookie88.fireclient.State;
import dev.dfonline.codeclient.CodeClient;

public class CodeClientIntegration {

    public static void tick() {
        if (CodeClient.location != null && CodeClient.location.name() != "play") {
            State.reset();
        }
    }

}
