package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestPayload;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLoginNetworkHandler.class)
public class ClientLoginNetworkHandlerMixin {

    @Inject(method = "onQueryRequest", at = @At("HEAD"))
    public void onQueryRequest(LoginQueryRequestS2CPacket packet, CallbackInfo ci) {
        LoginQueryRequestPayload payload = packet.payload();
        if (payload.id().getNamespace().equals("fireclient") && payload.id().getPath().equals("notif_dftooling_ip")) {
            FireClient.LOGGER.info("Player joined via dftooling-ip!");
            FireClient.overrideDiamondFireDetection = true;
        }
    }
}
