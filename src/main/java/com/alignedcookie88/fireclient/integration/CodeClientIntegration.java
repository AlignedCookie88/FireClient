package com.alignedcookie88.fireclient.integration;
import com.alignedcookie88.fireclient.State;
import dev.dfonline.codeclient.CodeClient;
import dev.dfonline.codeclient.location.Dev;

public class CodeClientIntegration {

    public static void tick() {
        if (CodeClient.location != null && !CodeClient.location.name().equals("play")) {
            State.reset();
        }
    }

    public static boolean onPlot() {
        return !CodeClient.location.name().equals("spawn");
    }

    public static int getPlotCodeBlockLimit() {
        if (CodeClient.location instanceof Dev dev) {
            return dev.assumeSize().size/2;
        }
        return 0;
    }

}
