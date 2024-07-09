package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.hud.HudElement;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        TextRenderer textRenderer = ((InGameHud) (Object) this).getTextRenderer();
        for (HudElement element : State.hud) {
            element.render(context, textRenderer, tickDelta, context.getScaledWindowWidth(), context.getScaledWindowHeight());
        }
    }

}
