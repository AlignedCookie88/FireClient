package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.Utility;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSignEditScreen.class)
public class AbstractSignEditScreenMixin {

    @WrapOperation(method = "method_45658", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Ljava/lang/String;)I"))
    public int fixSignMiniMessageHandling(TextRenderer instance, String text, Operation<Integer> original) {
        return original.call(instance, Utility.miniMessage(Utility.sanitiseMiniMessage(text)).getString());
    }

}
