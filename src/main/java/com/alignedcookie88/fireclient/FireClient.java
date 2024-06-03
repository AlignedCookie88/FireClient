package com.alignedcookie88.fireclient;

import com.alignedcookie88.fireclient.functions.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FireClient implements ModInitializer {

    public static List<String> commandQueue = new ArrayList<>();

    public static volatile boolean isProcessingCommands = false;

    public static Logger LOGGER = LoggerFactory.getLogger("FireClient");

    public static String version = "1.0.0";

    public static Identifier functionRegistryIdentifier = new Identifier("fireclient", "functions");

    public static Screen openOnNextTick = null;

    private static boolean codeClientIntegration = false;


    public static Registry<FireFunction> functionRegistry = FabricRegistryBuilder.createSimple(FireFunction.class, functionRegistryIdentifier).buildAndRegister();
    @Override
    public void onInitialize() {

        if (System.getProperty("os.name").toLowerCase().startsWith("mac os x"))
            throw new UserIsBadWithFinancialDecisionsException("The user bought an overpriced piece of rubbish from Apple Computers Inc");

        // CodeClient
        codeClientIntegration = FabricLoader.getInstance().isModLoaded("codeclient");

        // Load config
        Config.loadConfig();

        // Reset state (required for some config options to work properly)
        State.reset();

        // Register functions
        Registry.register(functionRegistry, new Identifier("fireclient", "debug"), new DebugFunction());
        Registry.register(functionRegistry, new Identifier("fireclient", "disable_movement"), new DisableMovementFunction());
        Registry.register(functionRegistry, new Identifier("fireclient", "enable_movement"), new EnableMovementFunction());
        Registry.register(functionRegistry, new Identifier("fireclient", "open_screen"), new OpenScreenFunction());
        Registry.register(functionRegistry, new Identifier("fireclient", "screen_add_button"), new ScreenAddButtonFunction());
        Registry.register(functionRegistry, new Identifier("fireclient", "report_version"), new ReportVersionFunction());
        Registry.register(functionRegistry, new Identifier("fireclient", "execute_python"), new ExecutePythonFunction());
        Registry.register(functionRegistry, new Identifier("fireclient", "set_ability"), new SetAbilityFunction());

    }

    public static <T extends PacketListener> boolean handlePacket(Packet<T> packet) {
        String name = packet.getClass().getName().replace("net.minecraft.network.packet.s2c.play.", "");

        try {
            generalModifyPacket(packet);
        } catch (RuntimeException e) {
            LOGGER.error("Error during general packet modification.", e);
        }

        if (name.equals("OverlayMessageS2CPacket")) {
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
                Text data = null;
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
                        LOGGER.error("Failed to set field {} on packet {}", field.getName(), name, e);
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


        Text modified = Utility.componentToText(MiniMessage.miniMessage().deserialize(miniMessage));
        return modified;
    }

    public static boolean handleActionBarUpdate(OverlayMessageS2CPacket packet) {
        String data = packet.getMessage().getString();
        if (data.startsWith(".fireclient ")) {
            queueCommand(data.replaceFirst("\\.fireclient ", ""));
            return true;
        }
        return false;
    }

    public static void queueCommand(String command) {
        while (isProcessingCommands) {
            Thread.onSpinWait();
        };
        commandQueue.add(command);
    }

    public static void handleCommand(String command) {
        String functionId = command.split(" ")[0];
        FireFunction function = functionRegistry.get(new Identifier(functionId));
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
        isProcessingCommands = true;
        for (String command : commandQueue) {
            LOGGER.info("Handling command {}", command);
            try {
                handleCommand(command);
            } catch (RuntimeException e) {
                Utility.sendStyledMessage("There was an error parsing a command from the plot. Check the log for more details.");
                LOGGER.error("Error parsing command.", e);
            }
        }
        commandQueue.clear();
        isProcessingCommands = false;


        if (openOnNextTick != null) {
            MinecraftClient.getInstance().setScreen(openOnNextTick);
            openOnNextTick = null;
        }

        if (codeClientIntegration) {
            CodeClientIntegration.tick();
        }
    }
}
