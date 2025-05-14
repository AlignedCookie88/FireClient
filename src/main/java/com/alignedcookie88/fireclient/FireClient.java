package com.alignedcookie88.fireclient;

import com.alignedcookie88.fireclient.api.FireClientApi;
import com.alignedcookie88.fireclient.commandrunner.CommandRunnerResponse;
import com.alignedcookie88.fireclient.commandrunner.CommandRunners;
import com.alignedcookie88.fireclient.legacy_sdk.functions.*;
import com.alignedcookie88.fireclient.integration.CodeClientIntegration;
import com.alignedcookie88.fireclient.legacy_sdk.FireFunction;
import com.alignedcookie88.fireclient.legacy_sdk.FireFunctionParser;
import com.alignedcookie88.fireclient.resources.ResourceReloadListener;
import com.alignedcookie88.fireclient.sdk.FireClientSDK;
import com.alignedcookie88.fireclient.task.TaskManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.registry.*;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FireClient implements ModInitializer {

    public static final List<String> legacyCommandQueue = new ArrayList<>();

    public static volatile boolean isProcessingLegacyCommands = false;

    public static final Logger LOGGER = LoggerFactory.getLogger("FireClient");

    public static final String version = FabricLoader.getInstance().getModContainer("fireclient").get().getMetadata().getVersion().getFriendlyString();

    public static final Identifier legacyFunctionRegistryIdentifier = Identifier.of("fireclient", "legacy_functions");

    public static Screen openOnNextTick = null;

    public static String joinCommand = null;

    public static boolean overrideDiamondFireDetection = false;

    private static boolean codeClientIntegration = false;

    private static final List<CommandRunnerResponse> commandRunnerResponses = new ArrayList<>();



    public static String VERSION;
    public static String USER_AGENT;


    public static final Registry<FireFunction> legacyFunctionRegistry = FabricRegistryBuilder.createSimple(FireFunction.class, legacyFunctionRegistryIdentifier).buildAndRegister();
    @Override
    public void onInitialize() {

        VERSION = FabricLoader.getInstance().getModContainer("fireclient").get().getMetadata().getVersion().getFriendlyString();
        USER_AGENT = "FireClient/"+VERSION;

        // CodeClient
        codeClientIntegration = FabricLoader.getInstance().isModLoaded("codeclient");

        // Load config
        Config.loadConfig();

        // Start API
        FireClientApi.setup();
        FireClientApi.start();

        // Reset state (required for some config options to work properly)
        State.reset();

        // Setup SDK
        FireClientSDK.init();

        // Register legacy functions
        registerLegacyFunction(new DebugFunction());
        registerLegacyFunction(new DisableMovementFunction());
        registerLegacyFunction(new EnableMovementFunction());
        registerLegacyFunction(new OpenScreenFunction());
        registerLegacyFunction(new ScreenAddButtonFunction());
        registerLegacyFunction(new ReportVersionFunction());
        registerLegacyFunction(new SetAbilityFunction());
        registerLegacyFunction(new HudAddTextFunction());
        registerLegacyFunction(new RemoveHudElementFunction());
        registerLegacyFunction(new HudAddBarFunction());
        registerLegacyFunction(new HudSetBarProgressFunction());
        registerLegacyFunction(new ScreenAddTextFunction());
        registerLegacyFunction(new UsePlotCommandsForChatFunction());
        registerLegacyFunction(new ScreenAddSlot());
        registerLegacyFunction(new SetPostProcessorFunction());
        registerLegacyFunction(new RemovePostProcessorFunction());
        registerLegacyFunction(new SetPostProcessorUniformFunction());
        registerLegacyFunction(new RebindCommandFunction());

        // Resource pack stuff
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ResourceReloadListener());

    }

    private static void registerLegacyFunction(FireFunction function) {
        Registry.register(legacyFunctionRegistry, Identifier.of("fireclient", function.getID()), function);
    }

    public static <T extends PacketListener> boolean handlePacket(Packet<T> packet) {
        String name = packet.getClass().getName().replace("net.minecraft.network.packet.s2c.play.", "");

        try {
            generalModifyPacket(packet);
        } catch (RuntimeException e) {
            LOGGER.error("Error during general packet modification.", e);
        }

        if (packet instanceof OverlayMessageS2CPacket) {
            return handleActionBarUpdate((OverlayMessageS2CPacket) packet);
        }

        return false;
    }

    public static <T extends PacketListener> void generalModifyPacket(Packet<T> packet) {
        String name = packet.getClass().getName().replace("net.minecraft.network.packet.s2c.play.", "");
        Field[] fields = packet.getClass().asSubclass(packet.getClass()).getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == Text.class) { // TEXT DATA
                field.setAccessible(true);
                Text data;
                try {
                    data = (Text) field.get(packet);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (data != null) {
                    Text newData = modifyPacketText(data);
                    try {
                        field.set(packet, newData);
                    } catch (IllegalAccessException e) {
                        //throw new RuntimeException(e);
                        //LOGGER.error("Failed to set field {} on packet {}", field.getName(), name, e);
                    }
                }
            }
        }
    }

    public static Text modifyPacketText(Text input) {
        if (input.getString().isEmpty())
            return input;
        String miniMessage = MiniMessage.miniMessage().serialize(Utility.textToComponent(input));
        miniMessage = miniMessage.replace("<bold>\u200C</bold>", ""); // remove nonsensical invisible character (?)
        //LOGGER.info(miniMessage);


        if (Config.state.oldRankTags) {
            // Overlord
            miniMessage = miniMessage.replace(
                    "<#aa002a>[</#aa002a><#ff7f7f>◆</#ff7f7f><red>Overlord</red><#ff7f7f>◆</#ff7f7f><#aa002a>]</#aa002a>",
                    "<dark_aqua>[</dark_aqua><gray>◆</gray><dark_red>Overlord</dark_red><gray>◆</gray><dark_aqua>]</dark_aqua>"
            );
            miniMessage = miniMessage.replace(
                    "<#aa002a>[</#aa002a><red>O</red><#aa002a>]</#aa002a>",
                    "<dark_aqua>[</dark_aqua><dark_red>O</dark_red><dark_aqua>]</dark_aqua>"
            );
            // Mythic
            miniMessage = miniMessage.replace(
                    "<#7f00aa>[</#7f00aa><#ff7fd4>◇</#ff7fd4><#d42ad4>Mythic</#d42ad4><#ff7fd4>◇</#ff7fd4><#7f00aa>]</#7f00aa>",
                    "<dark_gray>[</dark_gray><dark_purple>Mythic</dark_purple><dark_gray>]</dark_gray>"
            );
            miniMessage = miniMessage.replace(
                    "<#7f00aa>[</#7f00aa><#d42ad4>M</#d42ad4><#7f00aa>]</#7f00aa>",
                    "<dark_gray>[</dark_gray><dark_purple>M</dark_purple><dark_gray>]</dark_gray>"
            );
            // Emperor
            miniMessage = miniMessage.replace(
                    "<#2a70d4>[</#2a70d4><#aaffff>◦</#aaffff><#55aaff>Emperor</#55aaff><#aaffff>◦</#aaffff><#2a70d4>]</#2a70d4>",
                    "<dark_green>[</dark_green><aqua>Emperor</aqua><dark_green>]</dark_green>"
            );
            miniMessage = miniMessage.replace(
                    "<#2a70d4>[</#2a70d4><#55aaff>E</#55aaff><#2a70d4>]</#2a70d4>",
                    "<dark_green>[</dark_green><aqua>E</aqua><dark_green>]</dark_green>"
            );
            // Noble
            miniMessage = miniMessage.replace(
                    "<dark_green>[</dark_green><#7fff7f>Noble</#7fff7f><dark_green>]</dark_green>",
                    "<gold>[</gold><green>Noble</green><gold>]</gold>"
            );
            miniMessage = miniMessage.replace(
                    "<dark_green>[</dark_green><#7fff7f>N</#7fff7f><dark_green>]</dark_green>",
                    "<gold>[</gold><green>N</green><gold>]</gold>"
            );
        }

        if (Config.state.hideVIPTags) {
            // Short
            miniMessage = miniMessage.replace(
                    "<gold>[</gold><#ffd47f><click:suggest_command:'/vip'><hover:show_text:'<#ffd47f>VIP'>⭐</hover></click></#ffd47f><gold>]</gold> ",
                    ""
            );
            // Long
            miniMessage = miniMessage.replace(
                    "<gold>[</gold><#ffd47f>VIP</#ffd47f><gold>]</gold>",
                    ""
            );
        }


        Text modified = Utility.componentToText(MiniMessage.miniMessage().deserialize(Utility.sanitiseMiniMessage(miniMessage)));
        return modified;
    }

    public static boolean handleActionBarUpdate(OverlayMessageS2CPacket packet) {
        String data = packet.text().getString();
        if (data.startsWith(".fireclient ")) {
            queueLegacyCommand(data.replaceFirst("\\.fireclient ", ""));
            return true;
        }
        return false;
    }

    public static void queueLegacyCommand(String command) {
        while (isProcessingLegacyCommands) {
            Thread.onSpinWait();
        };
        legacyCommandQueue.add(command);
    }

    public static void handleLegacyCommand(String command) {
        if (State.sdkVersion != -1) { // Player has not been notified about legacy SDK usage in this case!
            State.sdkVersion = -1;
            Utility.sendStyledMessage("<red><bold>Warning!</bold> This plot is using the legacy (pre-1.5.0) FireClient SDK. This version of the SDK is no " +
                    "longer supported and will be removed in the near future. If you are the plot developer, please run <italic>/fireclient sdk</italic> " +
                    "while in dev mode to update to the newest version.");
        }

        String functionId = command.split(" ")[0];
        FireFunction function = legacyFunctionRegistry.get(Identifier.of(functionId));
        String args;
        if (command.length() == functionId.length()) {
            args = "";
        } else {
            args = command.substring(functionId.length()+1);
        }
        FireFunctionParser parser = new FireFunctionParser();
        parser.parseAndExecute(function, args);
    }

    public static void tick() {
        isProcessingLegacyCommands = true;
        for (String command : legacyCommandQueue) {
            if (Config.state.logFunctionCalls)
                LOGGER.info("Handling command {}", command);
            try {
                handleLegacyCommand(command);
            } catch (RuntimeException e) {
                Utility.sendStyledMessage("There was an error parsing a command from the plot. Check the log for more details.");
                LOGGER.error("Error parsing command.", e);
            }
        }
        legacyCommandQueue.clear();
        isProcessingLegacyCommands = false;


        if (openOnNextTick != null) {
            MinecraftClient.getInstance().setScreen(openOnNextTick);
            openOnNextTick = null;
        }

        if (codeClientIntegration) {
            CodeClientIntegration.tick();
        }

        CommandRunners.LOCATE.tick();

        FireClientApi.process(); // Process all messages

        TaskManager.tick(); // Tick the tasks
    }

    public static void addCommandRunnerResponse(CommandRunnerResponse response) {
        commandRunnerResponses.add(response);
    }

    public static boolean handleChatPacket(String text) {

        if (text.startsWith("[FireClient]"))
            return false;

        int foundIndex = -1;

        int i = 0;
        for (CommandRunnerResponse response : commandRunnerResponses) {
            for (String expr : response.getExpr()) {
                Pattern pattern = Pattern.compile(expr);
                Matcher matcher = pattern.matcher(text);

                if (matcher.find()) {
                    int groupCount = matcher.groupCount();
                    String[] groups = new String[groupCount];
                    for (int gi = 0; gi < groupCount; ++gi) {
                        groups[gi] = matcher.group(gi+1);
                    }
                    LOGGER.info("Found regex! Chat: {}, Data: {}", text, groups);
                    response.execute(groups);
                    foundIndex = i;
                    break;
                }
            }
            if (foundIndex > -1) {
                break;
            }
        }

        if (foundIndex > -1) {
            commandRunnerResponses.remove(foundIndex);
            return true;
        }

        return false;
    }

    public static void changedWorld() {
        commandRunnerResponses.clear();
    }

    public static boolean isCodeClientIntegrationEnabled() {
        return codeClientIntegration;
    }

    public static Logger createLogger(String serviceName) {
        return LoggerFactory.getLogger("FireClient|"+serviceName);
    }
}
