package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.add_nodes_screen.AddNodesScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        addDrawableChild(ButtonWidget.builder(Text.literal("Add Nodes"), button -> {
            MinecraftClient.getInstance().setScreen(new AddNodesScreen((MultiplayerScreen) (Screen) this));
        }).dimensions(3, 3, 75, 20).build());
    }

}
