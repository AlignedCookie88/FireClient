package com.alignedcookie88.fireclient.commandrunner.types;

import com.alignedcookie88.fireclient.CommandQueue;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.commandrunner.CommandRunner;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.*;
import java.util.List;

public class LocateCommandRunner extends CommandRunner {

    private final Map<String, Integer> plotIDMap = new HashMap<>();

    private final Map<String, String> plotNameMap = new HashMap<>();

    private final Map<String, Boolean> whitelistedMap = new HashMap<>();

    private final Map<String, String> modeMap = new HashMap<>();

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
        if (username.equals("You")) {
            username = MinecraftClient.getInstance().player.getGameProfile().getName();
        }
        if (groups.length > 5) {
            Integer id = Integer.valueOf(groups[4]);
            plotIDMap.put(username, id);
            plotNameMap.put(username, groups[3]);
            if (groups[7] == null) {
                whitelistedMap.put(username, false);
            } else {
                whitelistedMap.put(username, true);
            }
            modeMap.put(username, groups[2]);
        } else {
            plotIDMap.put(username, -1);
            whitelistedMap.put(username, false);
            modeMap.put(username, null);
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

    public int getPlotIDColour(int id, boolean vibrant) {
        int col;
        if (plotColourMap.containsKey(id)) {
            col = plotColourMap.get(id);
        } else {
            int colour = generatePastelColour();
            plotColourMap.put(id, colour);
            col = colour;
        }

        if (vibrant)
            return col * 2;
        return col;
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

    public String getPlotName(String username) {
        return plotNameMap.getOrDefault(username, null);
    }

    public String getShortenedPlotName(String username) {
        String plotName = getPlotName(username);
        if (plotName == null)
            return null;
        String allowedChars = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘꞯʀꜱᴛᴜᴠᴡxʏᴢABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 1234567890ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ'";
        StringBuilder done = new StringBuilder();
        char prev = ' ';

        int i = 0;
        int fi = 0;
        for (char chara : plotName.toCharArray()) {
            if (allowedChars.contains(String.valueOf(chara))) {
                if (prev == ' ' && chara == ' ')
                    continue;
                prev = chara;
                done.append(chara);
                if (i == 15 && fi != plotName.length()-1) {
                    done = new StringBuilder().append(done.toString().stripTrailing()); // hack to strip trailing whitespace
                    done.append("...");
                    break;
                }
                i++;
            }
            fi++;
        }

        return done.toString().stripTrailing();
    }

    public boolean isPlayerOnWhitelistedPlot(String username) {
        return whitelistedMap.getOrDefault(username, false);
    }

    public String getPlayerMode(String username) {
        return modeMap.getOrDefault(username, null);
    }

    public Identifier getPlayerModeIcon(String username) {
        String mode = getPlayerMode(username);
        if (mode == null) return null;

        return new Identifier("fireclient", "textures/mode/"+mode+".png");
    }
}
