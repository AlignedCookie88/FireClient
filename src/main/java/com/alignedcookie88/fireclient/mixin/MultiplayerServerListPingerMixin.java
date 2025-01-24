package com.alignedcookie88.fireclient.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiplayerServerListPinger.class)
public class MultiplayerServerListPingerMixin {

    @WrapOperation(method = "add", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ServerAddress;parse(Ljava/lang/String;)Lnet/minecraft/client/network/ServerAddress;"))
    public ServerAddress add(String address, Operation<ServerAddress> original) {
        ServerAddress parsed = ServerAddress.parse(address);
        if (parsed.getAddress().equals("dev3.mcdiamondfire.com") || parsed.getAddress().equals("event.mcdiamondfire.com") || parsed.getAddress().equals("build.mcdiamondfire.com") || parsed.getAddress().equals("dev-events.mcdiamondfire.com")) {
            return new ServerAddress("mcdiamondfire.com", parsed.getPort());
        }
        return original.call(address);
    }

}
