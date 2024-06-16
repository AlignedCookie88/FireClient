package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.Config;
import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.commandrunner.CommandRunners;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
            MutableText modified = FireClient.modifyPacketText(original).copy();

            // add plot id to end
            if (Config.state.showPlotIDsInTab) {
                Integer plotID = CommandRunners.LOCATE.getPlotID(entry.getProfile().getName());
                if (plotID != null) {
                    if (plotID == -1) {
                        modified.append(Text.literal(" Spawn").withColor(0xbddefc));
                    } else {
                        modified.append(Text.literal(" "+plotID).withColor(CommandRunners.LOCATE.getPlotIDColour(plotID)));
                    }
                }
            }

            cir.setReturnValue(modified);
        }
    }


}
