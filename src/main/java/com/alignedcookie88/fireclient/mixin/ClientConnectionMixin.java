package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void handlePacket(Packet<T> packet, net.minecraft.network.listener.PacketListener listener, CallbackInfo ci) {
        if (FireClient.handlePacket(packet)) ci.cancel();
    }
}
