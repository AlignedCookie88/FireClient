package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.State;
import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.hud.HudElement;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        List<HudElement> elementsToRemove = new ArrayList<>();

        // Render
        TextRenderer textRenderer = ((InGameHud) (Object) this).getTextRenderer();
        for (HudElement element : State.hud) {
            try {
                element.render(context, textRenderer, tickDelta, context.getScaledWindowWidth(), context.getScaledWindowHeight());
            } catch (Exception e) {
                Utility.sendStyledMessage("There was an error rendering HUD element '%s', it has been removed. See the log for more info.".formatted(element.getID()));
                FireClient.LOGGER.error("There was an error rendering HUD element '{}'. See below.", element.getID(), e);
                elementsToRemove.add(element);
            }
        }

        // Remove erroring elements
        for (HudElement element : elementsToRemove) {
            State.hud.remove(element);
        }
    }

}
