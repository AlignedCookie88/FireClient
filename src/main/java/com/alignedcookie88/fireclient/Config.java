package com.alignedcookie88.fireclient;

import com.google.gson.Gson;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.*;

public class Config {

    public static ConfigState state;

    public static Screen getConfig(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("FireClient Config"));

        builder.setSavingRunnable(() -> {
            saveConfig();
            State.reset(); // required for some config options to work correctly
        });

        createGeneralCategory(builder);
        createVisualCategory(builder);

        return builder.build();
    }

    public static void openConfig(Screen parent) {
        MinecraftClient.getInstance().setScreen(
                getConfig(parent)
        );
    }

    public static void openConfig() {
        openConfig(null);
    }

    public static void loadConfig() {
        if (!getConfigFile().isFile()) {
            state = ConfigState.getDefault();
            saveConfig();
            return;
        }

        Gson gson = new Gson();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(getConfigFile()));
            state = gson.fromJson(reader, ConfigState.class);
            reader.close();
        } catch (IOException e) {
            FireClient.LOGGER.error("Error loading config", e);
        }
    }

    public static void saveConfig() {
        Gson gson = new Gson();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getConfigFile()));
            writer.write(gson.toJson(state));
            writer.close();
        } catch (IOException e) {
            FireClient.LOGGER.error("Error saving config", e);
        }
    }

    private static File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("fireclient.json").toFile();
    }

    private static void createGeneralCategory(ConfigBuilder builder) {
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Always block Python"), state.alwaysBlockPython)
                .setDefaultValue(ConfigState.getDefault().alwaysBlockPython)
                .setTooltip(Text.literal("Enabling this option will automatically block plots from running client-side Python code, instead of asking for your consent every time."))
                .setSaveConsumer(newValue -> state.alwaysBlockPython = newValue)
                .build());
    }

    private static void createVisualCategory(ConfigBuilder builder) {
        ConfigCategory visual = builder.getOrCreateCategory(Text.literal("Visual"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        visual.addEntry(entryBuilder.startBooleanToggle(Text.literal("Old rank tags"), state.oldRankTags)
                .setDefaultValue(ConfigState.getDefault().oldRankTags)
                .setTooltip(Text.literal("Replaces the new rank tags with the old ones."))
                .setSaveConsumer(newValue -> state.oldRankTags = newValue)
                .build());

        visual.addEntry(entryBuilder.startBooleanToggle(Text.literal("Hide VIP tags"), state.hideVIPTags)
                .setDefaultValue(ConfigState.getDefault().hideVIPTags)
                .setTooltip(Text.literal("Hide the VIP tags from chat and /whois."))
                .setSaveConsumer(newValue -> state.hideVIPTags = newValue)
                .build());

        visual.addEntry(entryBuilder.startBooleanToggle(Text.literal("Show plot IDs in tab"), state.showPlotIDsInTab)
                .setDefaultValue(ConfigState.getDefault().showPlotIDsInTab)
                .setTooltip(Text.literal("If FireClient should show plot IDs in tab."))
                .setSaveConsumer(newValue -> state.showPlotIDsInTab = newValue)
                .build());

        visual.addEntry(entryBuilder.startBooleanToggle(Text.literal("Show plot names in tab"), state.showPlotNamesInTab)
                .setDefaultValue(ConfigState.getDefault().showPlotNamesInTab)
                .setTooltip(Text.literal("If FireClient should show plot names in tab."))
                .setSaveConsumer(newValue -> state.showPlotNamesInTab = newValue)
                .build());

        visual.addEntry(entryBuilder.startBooleanToggle(Text.literal("Show player's current plot whitelisted state in tab"), state.showPlotWhitelistedInTab)
                .setDefaultValue(ConfigState.getDefault().showPlotWhitelistedInTab)
                .setTooltip(Text.literal("If FireClient should show whether the player is on a whitelisted plot in tab."))
                .setSaveConsumer(newValue -> state.showPlotWhitelistedInTab = newValue)
                .build());
    }
}
