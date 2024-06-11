package com.alignedcookie88.fireclient.client;

import com.alignedcookie88.fireclient.*;
import com.alignedcookie88.fireclient.screen_editor.ScreenEditor;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class FireClientClient implements ClientModInitializer {
    public static final Dynamic3CommandExceptionType INVALID_TYPE_EXCEPTION = new Dynamic3CommandExceptionType((element, type, expectedType) -> {
        return Text.stringifiedTranslatable("argument.resource.invalid_type", new Object[]{element, type, expectedType});
    });

    private static KeyBinding customAbility1;
    private static KeyBinding customAbility2;
    private static KeyBinding customAbility3;
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

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("fireclient")
                        .then(ClientCommandManager.literal("config").executes(context -> {
                            FireClient.LOGGER.info("Opening config screen");
                            FireClient.openOnNextTick = Config.getConfig(null);
                            return 1;
                        }))
                        .then(ClientCommandManager.literal("get_function").then(ClientCommandManager.argument("function", RegistryEntryArgumentType.registryEntry(CommandRegistryAccess.of(DynamicRegistryManager.of(Registries.REGISTRIES), FeatureSet.empty()), FireClient.functionRegistry.getKey())).executes(context -> {
                            FireFunction fireFunction = (FireFunction) getRegistryEntry(context, "function", RegistryKey.ofRegistry(FireClient.functionRegistryIdentifier)).value();
                            ItemStack stack = FireFunctionSerialiser.serialiseFunction(fireFunction);
                            Utility.giveItem(stack);
                            return 1;
                        })))
                        .then(ClientCommandManager.literal("help").executes(context -> {
                            Utility.sendStyledMessage("FireClient help");
                            Utility.sendStyledMessage("===============");
                            Utility.sendStyledMessage("`/fireclient config` to access the config");
                            Utility.sendStyledMessage("`/fireclient get_function <function>` to get a template for a client function");
                            Utility.sendStyledMessage("`/fireclient help` to show this message");
                            return 1;
                        }))
                        .then(ClientCommandManager.literal("screen_editor").executes(context -> {
                            FireClient.openOnNextTick = new ScreenEditor();
                            return 1;
                        }))
                )
        );

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            FireClient.tick();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            handleKeys();
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
    }

    public static <T> RegistryEntry.Reference<T> getRegistryEntry(CommandContext<FabricClientCommandSource> context, String name, RegistryKey<Registry<T>> registryRef) throws CommandSyntaxException {
        RegistryEntry.Reference<T> reference = (RegistryEntry.Reference)context.getArgument(name, RegistryEntry.Reference.class);
        RegistryKey<?> registryKey = reference.registryKey();
        if (registryKey.isOf(registryRef)) {
            return reference;
        } else {
            throw INVALID_TYPE_EXCEPTION.create(registryKey.getValue(), registryKey.getRegistry(), registryRef.getValue());
        }
    }

    private static void handleKeys() {
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
    }
}
