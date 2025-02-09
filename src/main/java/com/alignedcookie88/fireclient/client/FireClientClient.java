package com.alignedcookie88.fireclient.client;

import com.alignedcookie88.fireclient.*;
import com.alignedcookie88.fireclient.api.ApiConnection;
import com.alignedcookie88.fireclient.api.FireClientApi;
import com.alignedcookie88.fireclient.commandrunner.CommandRunners;
import com.alignedcookie88.fireclient.functions_screen.FunctionsScreen;
import com.alignedcookie88.fireclient.task.TaskManager;
import com.alignedcookie88.fireclient.task.tasks.DFToolingApiTask;
import com.alignedcookie88.fireclient.task.tasks.UploadPackTask;
import com.alignedcookie88.fireclient.task.tasks.WaitForConfirmationTask;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.function.Consumer;

public class FireClientClient implements ClientModInitializer {
    public static final Dynamic3CommandExceptionType INVALID_TYPE_EXCEPTION = new Dynamic3CommandExceptionType((element, type, expectedType) -> {
        return Text.stringifiedTranslatable("argument.resource.invalid_type", new Object[]{element, type, expectedType});
    });

    private static KeyBinding customAbility1;
    private static KeyBinding customAbility2;
    private static KeyBinding customAbility3;
    private static KeyBinding openFunctionsScreen;

    private ClientWorld lastWorld;

    @Override
    public void onInitializeClient() {

//        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
//                dispatcher.register(ClientCommandManager.literal("get_function")
//                        .then(ClientCommandManager.argument("function", RegistryEntryArgumentType.registryEntry(CommandRegistryAccess.of(DynamicRegistryManager.of(Registries.REGISTRIES), FeatureSet.empty()), FireClient.functionRegistry.getKey())).executes(context -> {
//                            FireFunction fireFunction = (FireFunction) getRegistryEntry(context, "function", RegistryKey.ofRegistry(FireClient.functionRegistryIdentifier)).value();
//                            ItemStack stack = FireFunctionSerialiser.serialiseFunction(fireFunction);
//                            Utility.giveItem(stack);
//                            return 1;
//                        }))
//                )
//        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                    dispatcher.register(ClientCommandManager.literal("fireclient")
                            .requires(source -> Utility.isPlayingDiamondFire())
                            .then(ClientCommandManager.literal("config").executes(context -> {
                                FireClient.LOGGER.info("Opening config screen");
                                FireClient.openOnNextTick = Config.getConfig(null);
                                return 1;
                            }))
                            .then(ClientCommandManager.literal("help").executes(context -> {
                                Utility.sendStyledMessage("FireClient help");
                                Utility.sendStyledMessage("===============");
                                Utility.sendStyledMessage("`/fireclient config` to access the config");
                                Utility.sendStyledMessage("`/fireclient get_function <function>` to get a template for a client function");
                                Utility.sendStyledMessage("`/fireclient help` to show this message");
                                return 1;
                            }))
                            .then(ClientCommandManager.literal("functions").executes(context -> {
                                FireClient.openOnNextTick = new FunctionsScreen();
                                return 1;
                            }))
                            .then(ClientCommandManager.literal("api")
                                    .then(ClientCommandManager.literal("query").executes(context -> {
                                        if (Config.state.apiEnabled) {
                                            context.getSource().sendFeedback(Text.literal("There are "+ FireClientApi.apiConnections.size() +" program(s) connected. Run /fireclient api connections for more info."));
                                        } else {
                                            context.getSource().sendFeedback(Text.literal("The API is disabled."));
                                        }
                                        return 1;
                                    }))
                                    .then(ClientCommandManager.literal("connections").executes(context -> {
                                        if (!Config.state.apiEnabled) {
                                            context.getSource().sendError(Text.literal("The API is disabled."));
                                            return 1;
                                        }

                                        Utility.sendMessage("Connected programs:");
                                        for (ApiConnection connection : FireClientApi.apiConnections) {
                                            Utility.sendMessage(Text.literal("- "+connection.applicationName).styled(style -> style.withHoverEvent(new HoverEvent(
                                                    HoverEvent.Action.SHOW_TEXT,
                                                    Text.literal("ID: ").append(
                                                            Text.literal(connection.connectionId.toString()).formatted(Formatting.GRAY)
                                                    ).append(
                                                            Text.literal("\nConnection Type: ").append(
                                                                    Text.literal(connection.getTypeName()).formatted(Formatting.GRAY)
                                                            )
                                                    )
                                            ))).styled(style -> style.withClickEvent(new ClickEvent(
                                                    ClickEvent.Action.SUGGEST_COMMAND,
                                                    "/fireclient api run_action "+connection.connectionId.toString()+" "
                                            ))));
                                        }
                                        if (FireClientApi.apiConnections.isEmpty())
                                            Utility.sendMessage(Text.literal("There are no programs connected.").formatted(Formatting.GRAY));

                                        return 1;
                                    }))
                                    .then(ClientCommandManager.literal("run_action").then(ClientCommandManager.argument("id", StringArgumentType.string())
                                            .then(ClientCommandManager.literal("disconnect").executes(context -> {
                                                runOnApiConnection(context, connection -> {
                                                    connection.close();
                                                    context.getSource().sendFeedback(Text.literal("Disconnected the program."));
                                                });
                                                return 1;
                                            }))
                                    ))
                            )
                            .then(ClientCommandManager.literal("confirm").executes(context -> {
                                if (WaitForConfirmationTask.isToConfirm())
                                    WaitForConfirmationTask.confirm();
                                else Utility.sendStyledMessage("There is nothing to confirm.");
                                return 0;
                            }))
                    );

                    registerAliases(dispatcher);

                    UploadPackTask.registerCommand(dispatcher);

                    if (Config.state.dfToolingAdminFeatures) {
                        dispatcher.register(ClientCommandManager.literal("dftooling_admin")
                                .then(
                                        ClientCommandManager.literal("actiondump").then(
                                                ClientCommandManager.literal("delete").then(
                                                        ClientCommandManager.argument("patch", StringArgumentType.word()).executes(context -> {
                                                            String patch = StringArgumentType.getString(context, "patch");
                                                            TaskManager.startTaskFromCommand(context, new DFToolingApiTask(
                                                                    new DFToolingApiTask.Request(
                                                                            DFToolingApiTask.ACTIONDUMP_ADMIN_DELETE,
                                                                            DFToolingApiTask.argsBuilder()
                                                                                    .add("patch", patch)
                                                                                    .build()
                                                                    ),
                                                                    result -> Utility.sendMessage(result.getStatusMessage())
                                                            ));
                                                            return 0;
                                                        })
                                                )
                                        ).then(
                                                ClientCommandManager.literal("upload").then(
                                                        ClientCommandManager.argument("patch", StringArgumentType.word()).then(
                                                                ClientCommandManager.argument("latest", BoolArgumentType.bool()).then(
                                                                        ClientCommandManager.argument("beta", BoolArgumentType.bool()).executes(context -> {
                                                                            String patch = StringArgumentType.getString(context, "patch");
                                                                            boolean latest = BoolArgumentType.getBool(context, "latest");
                                                                            boolean beta = BoolArgumentType.getBool(context, "beta");

                                                                            File actiondumpFile = FabricLoader.getInstance().getGameDir().resolve("actiondump.json").toFile();
                                                                            if (!actiondumpFile.isFile()) {
                                                                                context.getSource().sendError(Text.literal("There is no actiondump.json file in the game directory."));
                                                                                return 0;
                                                                            }

                                                                            TaskManager.startTaskFromCommand(context, new DFToolingApiTask(
                                                                                    new DFToolingApiTask.Request(
                                                                                            DFToolingApiTask.ACTIONDUMP_ADMIN_UPLOAD,
                                                                                            DFToolingApiTask.argsBuilder()
                                                                                                    .add("patch", patch)
                                                                                                    .add("latest", Boolean.toString(latest))
                                                                                                    .add("beta", Boolean.toString(beta))
                                                                                                    .build(),
                                                                                            stream -> {
                                                                                                FileInputStream inputStream = new FileInputStream(actiondumpFile);
                                                                                                while (true) {
                                                                                                    byte[] data = inputStream.readNBytes(1024);
                                                                                                    if (data.length == 0)
                                                                                                        break;
                                                                                                    stream.write(data);
                                                                                                }
                                                                                                inputStream.close();
                                                                                            }
                                                                                    ),
                                                                                    result -> Utility.sendMessage(result.getStatusMessage())
                                                                            ));

                                                                            return 0;
                                                                        })
                                                                )
                                                        )
                                                )
                                        )
                                )
                        );
                    }
                }
        );

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            FireClient.tick();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            handleKeys();
        });

        ClientTickEvents.START_WORLD_TICK.register(world -> {
            CommandQueue.tick(); // For some reason CommandQueue must tick here
            if (lastWorld != MinecraftClient.getInstance().world) {
                FireClient.LOGGER.info("World load");
                CommandRunners.LOCATE.changedWorld();
                FireClient.changedWorld();
                lastWorld = MinecraftClient.getInstance().world;
            }

            if (FireClient.joinCommand != null)
                MinecraftClient.getInstance().getNetworkHandler().sendCommand(FireClient.joinCommand);
            FireClient.joinCommand = null;
        });


        customAbility1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fireclient.custom1", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.fireclient.keys" // The translation key of the keybinding's category.
        ));


        customAbility2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fireclient.custom2", // The translation key of the keybinding's name
                InputUtil.Type.MOUSE, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_MOUSE_BUTTON_4, // The keycode of the key
                "category.fireclient.keys" // The translation key of the keybinding's category.
        ));


        customAbility3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fireclient.custom3", // The translation key of the keybinding's name
                InputUtil.Type.MOUSE, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_MOUSE_BUTTON_5, // The keycode of the key
                "category.fireclient.keys" // The translation key of the keybinding's category.
        ));


        openFunctionsScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fireclient.open_functions", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_HOME, // The keycode of the key
                "category.fireclient.keys" // The translation key of the keybinding's category.
        ));
    }

    private static void runOnApiConnection(CommandContext<FabricClientCommandSource> context, Consumer<ApiConnection> consumer) {
        if (!Config.state.apiEnabled) {
            context.getSource().sendError(Text.literal("The API is disabled."));
            return;
        }

        String id = StringArgumentType.getString(context, "id");
        for (ApiConnection connection : FireClientApi.apiConnections) {
            if (Objects.equals(connection.connectionId.toString(), id)) {
                consumer.accept(connection);
                return;
            }
        }

        context.getSource().sendError(Text.literal("No such API connection."));
    }

    private static void handleKeys() {


        if (!Utility.isPlayingDiamondFire()) // All keybinds below here won't work outside of DF.
            return;

        while (customAbility1.wasPressed()) {
            if (State.ability1Fn != null) {
                Utility.runPlotCommand(State.ability1Fn);
            }
        }
        while (customAbility2.wasPressed()) {
            if (State.ability2Fn != null) {
                Utility.runPlotCommand(State.ability2Fn);
            }
        }
        while (customAbility3.wasPressed()) {
            if (State.ability3Fn != null) {
                Utility.runPlotCommand(State.ability3Fn);
            }
        }
        while (openFunctionsScreen.wasPressed()) {
            MinecraftClient.getInstance().setScreen(new FunctionsScreen());
        }
    }

    private static void registerAliases(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // Spelling
        registerAlias(dispatcher, "colours", "colors");

        // Funnies
        registerAlias(dispatcher, "bigvote", "boost");
        registerAlias(dispatcher, "smallboost", "vote");

        // Nodes
        registerNodeAlias(dispatcher, "node1");
        registerNodeAlias(dispatcher, "node2");
        registerNodeAlias(dispatcher, "node3");
        registerNodeAlias(dispatcher, "node4");
        registerNodeAlias(dispatcher, "node5");
        registerNodeAlias(dispatcher, "node6");
        registerNodeAlias(dispatcher, "node7");
        registerNodeAlias(dispatcher, "beta");
        registerNodeAlias(dispatcher, "event");
    }

    private static void registerAlias(CommandDispatcher<FabricClientCommandSource> dispatcher, String alias, String command) {
        dispatcher.register(ClientCommandManager.literal(alias).requires(source -> Utility.isPlayingDiamondFire()).executes(context -> {
            CommandQueue.queueCommand(command);
            return 1;
        }));
    }

    private static void registerNodeAlias(CommandDispatcher<FabricClientCommandSource> dispatcher, String node) {
        registerAlias(dispatcher, node, "server "+node);
    }
}
