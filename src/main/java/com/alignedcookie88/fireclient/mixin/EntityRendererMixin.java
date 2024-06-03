package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @ModifyVariable(method = "renderLabelIfPresent", at = @At("HEAD"), index = 2, argsOnly = true)
    private Text renderLabelIfPresentText(Text value) {
        return FireClient.modifyPacketText(value); // needed for tags to work properly
    }

}
