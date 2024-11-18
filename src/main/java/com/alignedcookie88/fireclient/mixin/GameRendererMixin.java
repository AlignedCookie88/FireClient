package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.State;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", ordinal = 0))
    public void render(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if (State.postProcessor != null) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            State.postProcessor.render(tickCounter.getLastFrameDuration());
        }
    }

    @Inject(method = "onResized", at = @At("HEAD"))
    public void onResized(int width, int height, CallbackInfo ci) {
        if (State.postProcessor != null) {
            State.postProcessor.setupDimensions(width, height);
        }
    }
}
