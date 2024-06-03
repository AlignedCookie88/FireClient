package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    public void getPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        // isn't changed by my bad implementation of a packet modifier
        Text original = cir.getReturnValue();
        if (original != null) {
            Text modified = FireClient.modifyPacketText(original);
            cir.setReturnValue(modified);
        }
    }


}
