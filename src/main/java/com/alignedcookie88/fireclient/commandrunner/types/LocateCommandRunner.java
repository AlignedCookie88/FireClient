package com.alignedcookie88.fireclient.commandrunner.types;

import com.alignedcookie88.fireclient.CommandQueue;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.commandrunner.CommandRunner;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.awt.*;
import java.util.*;
import java.util.List;

public class LocateCommandRunner extends CommandRunner {

    private final Map<String, Integer> plotIDMap = new HashMap<>();

    private final Map<Integer, Integer> plotColourMap = new HashMap<>();

    private final List<String> waitingPlayers = new ArrayList<>();

    private final List<String> donePlayers = new ArrayList<>();

    private final Random random = new Random();

    private int ticksUntilReset = 500;

    public LocateCommandRunner() {
        super(new String[] {
                " *\\n([a-zA-Z0-9_]*) (is|are) currently (playing|coding|building|existing) on:\\n\\n→ (.*) \\[([0-9]*)\\](\\n.*)?\\n→ Owner: ([a-zA-Z0-9_]*) (\\[Whitelisted\\])?\\n→ Server: Node .*\\n *", // Plot
                " *\\n([a-zA-Z0-9_]*) (is|are) currently at spawn\\n→ Server: Node .*\\n *" // Spawn
        }, "locate");
    }

    @Override
    protected void execute(String[] groups) {
//        Utility.sendStyledMessage("Locate response received: "+ Arrays.toString(groups));
        String username = groups[0];
        if (username == "You") {
            username = MinecraftClient.getInstance().player.getGameProfile().getName();
        }
        if (groups.length > 5) {
            Integer id = Integer.valueOf(groups[4]);
            plotIDMap.put(username, id);
        } else {
            plotIDMap.put(username, -1);
        }
        waitingPlayers.remove(username);
        donePlayers.add(username);
    }

    protected void run(String username) {
        super.run(username);
    }

    public Integer getPlotID(String username) {

        if (!donePlayers.contains(username)) {
            if (!waitingPlayers.contains(username) && shouldRun()) {
                waitingPlayers.add(username);
                run(username);
            }
        }

        if (plotIDMap.containsKey(username)) {
            return plotIDMap.get(username);
        }

        return null;
    }

    private int generatePastelColour() {
        int hue = random.nextInt(0, 360);
        int saturation = 25;
        int value = 100;
        return Color.HSBtoRGB((float) hue / 360, (float) saturation /100, (float) value /100);
    }

    public int getPlotIDColour(int id) {
        if (plotColourMap.containsKey(id)) {
            return plotColourMap.get(id);
        } else {
            int colour = generatePastelColour();
            plotColourMap.put(id, colour);
            return colour;
        }
    }

    public void tick() {
        ticksUntilReset--;
        if (ticksUntilReset < 1) {
            donePlayers.clear();
            ticksUntilReset = 500;
        }
    }

    public void changedWorld() {
        ticksUntilReset = 500;
        waitingPlayers.clear();
    }
}
