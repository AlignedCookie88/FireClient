package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {

    @WrapMethod(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;Lnet/minecraft/client/network/CookieStorage;)V")
    public void connect(MinecraftClient client, ServerAddress address, ServerInfo info, CookieStorage cookieStorage, Operation<Void> original) {
        if (address.getAddress().equals("dev3.mcdiamondfire.com")) {
            FireClient.joinCommand = "server dev3";
            address = new ServerAddress("dev.mcdiamondfire.com", address.getPort());
        } else if (address.getAddress().equals("event.mcdiamondfire.com")) {
            FireClient.joinCommand = "server event";
            address = new ServerAddress("mcdiamondfire.com", address.getPort());
        } else if (address.getAddress().equals("build.mcdiamondfire.com")) {
            FireClient.joinCommand = "server build";
            address = new ServerAddress("mcdiamondfire.com", address.getPort());
        } else if (address.getAddress().equals("dev-events.mcdiamondfire.com")) {
            FireClient.joinCommand = "server dev-events";
            address = new ServerAddress("mcdiamondfire.com", address.getPort());
        } else {
            FireClient.joinCommand = null;
        }
        original.call(client, address, info, cookieStorage);
        FireClient.overrideDiamondFireDetection = false;
    }

}
