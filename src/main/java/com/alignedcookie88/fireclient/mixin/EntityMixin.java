package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.State;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "changeLookDirection", cancellable = true)
    public void changeLookDirectionMixin(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Object) this instanceof ClientPlayerEntity) {
            if (!State.canMove) {
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getName", cancellable = true)
    public void getName(CallbackInfoReturnable<Text> cir) {
        if ((Object) this instanceof PlayerEntity || (Object) this instanceof ClientPlayerEntity) {
            // isn't changed by my bad implementation of a packet modifier
            Text original = cir.getReturnValue();
            if (original != null) {
                Text modified = FireClient.modifyPacketText(original);
                cir.setReturnValue(modified);
            }
        }
    }
}
