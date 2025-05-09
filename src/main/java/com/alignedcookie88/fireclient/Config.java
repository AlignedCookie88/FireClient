package com.alignedcookie88.fireclient;

import com.alignedcookie88.fireclient.api.FireClientApi;
import com.google.gson.Gson;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
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
        createApiCategory(builder);
        createDfToolingApiCategory(builder);

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

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Log function calls"), state.logFunctionCalls)
                .setDefaultValue(ConfigState.getDefault().logFunctionCalls)
                .setTooltip(Text.literal("May cause alot of log spam with some plots, mainly for debug use."))
                .setSaveConsumer(newValue -> state.logFunctionCalls = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Leave Plot button in game menu"), state.showLeavePlotInGameMenu)
                .setDefaultValue(ConfigState.getDefault().showLeavePlotInGameMenu)
                .setTooltip(Text.literal("Adds a Leave Plot button to the game menu. Requires CodeClient to be installed."))
                .setSaveConsumer(newValue -> state.showLeavePlotInGameMenu = newValue)
                .setDisplayRequirement(FireClient::isCodeClientIntegrationEnabled)
                .build());

        general.addEntry(entryBuilder.startFloatField(Text.literal("Screen safe area (%)"), state.screenSafeArea * 100)
                .setDefaultValue(ConfigState.getDefault().screenSafeArea * 100)
                .setTooltip(Text.literal("Sets the area where custom HUD will be shown."))
                .setSaveConsumer(newValue -> state.screenSafeArea = newValue / 100)
                .setMin(75.0F)
                .setMax(100.0F)
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

        visual.addEntry(entryBuilder.startBooleanToggle(Text.literal("Show player's mode in tab"), state.showModeInTab)
                .setDefaultValue(ConfigState.getDefault().showModeInTab)
                .setTooltip(Text.literal("If FireClient should show player modes in tab."))
                .setSaveConsumer(newValue -> state.showModeInTab = newValue)
                .build());
    }

    public static void createApiCategory(ConfigBuilder builder) {
        ConfigCategory api = builder.getOrCreateCategory(Text.literal("API"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        api.addEntry(entryBuilder.startBooleanToggle(Text.literal("API Enabled"), state.apiEnabled)
                .setDefaultValue(ConfigState.getDefault().apiEnabled)
                .setTooltip(Text.literal("Whether the FireClient API Should be enabled."))
                .setSaveConsumer(newValue -> {
                    state.apiEnabled = newValue;
                    if (newValue)
                        FireClientApi.start();
                    else FireClientApi.stop();
                })
                .build());

        api.addEntry(entryBuilder.startBooleanToggle(Text.literal("API Auth Enabled"), state.apiAuthEnabled)
                .setDefaultValue(ConfigState.getDefault().apiAuthEnabled)
                .setTooltip(Text.literal("Whether the FireClient API should accept auth requests. This allows websites and programs to safely and securely check what Minecraft account is in use when connected to the FireClient API. NO DATA THAT WOULD ALLOW THE APPLICATION TO LOGIN AS YOU IS SHARED."))
                .setSaveConsumer(newValue -> state.apiAuthEnabled = newValue)
                .build());
    }

    public static void createDfToolingApiCategory(ConfigBuilder builder) {
        ConfigCategory api = builder.getOrCreateCategory(Text.literal("DFTooling API"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        api.addEntry(entryBuilder.startBooleanToggle(Text.literal("DFTooling API Enabled"), state.dfToolingApiEnabled)
                .setDefaultValue(ConfigState.getDefault().dfToolingApiEnabled)
                .setTooltip(Text.literal("Whether the DFTooling API should be enabled. The DFTooling API provides features like /uploadpack."))
                .setSaveConsumer(newValue -> state.dfToolingApiEnabled = newValue)
                .build());

        api.addEntry(entryBuilder.startBooleanToggle(Text.literal("DFTooling Admin Features"), state.dfToolingAdminFeatures)
                .setDefaultValue(ConfigState.getDefault().dfToolingAdminFeatures)
                .setTooltip(Text.literal("Whether the DFTooling Admin Features (/dftooling_admin) should be enabled. Please note the commands won't work unless you have been granted permission."))
                .setSaveConsumer(newValue -> {
                    if (MinecraftClient.getInstance().world != null && newValue != state.dfToolingApiEnabled) {
                        FireClient.openOnNextTick = new Screen(Text.empty()) {
                            @Override
                            protected void init() {
                                MinecraftClient.getInstance().world.disconnect();
                                MinecraftClient.getInstance().disconnect();
                                MinecraftClient.getInstance().setScreen(new TitleScreen());
                            }
                        };
                    }
                    state.dfToolingAdminFeatures = newValue;
                })
                .build());

        if (state.dfToolingApiAgreement)
            api.addEntry(entryBuilder.startBooleanToggle(Text.literal("DFTooling API Agreement"), true)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Disabling this option will cause you to have to confirm you agree to the DFTooling API's logging agreement next time you use it."))
                    .setSaveConsumer(newValue -> state.dfToolingApiAgreement = newValue)
                    .build());
    }
}
