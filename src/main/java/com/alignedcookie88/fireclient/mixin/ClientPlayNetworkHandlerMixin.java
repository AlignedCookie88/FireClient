package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.sdk.FireClientSDK;
import com.mojang.serialization.DynamicOps;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow @Final private DynamicRegistryManager.Immutable combinedDynamicRegistries;

    @Inject(method = "onBlockEntityUpdate", at = @At("HEAD"), cancellable = true)
    public void handleSDKCalls(BlockEntityUpdateS2CPacket packet, CallbackInfo ci) {
        if (packet.getBlockEntityType() == BlockEntityType.SIGN) { // SDK calls are sent via sign text updates

            // Extract the front sign text, returning if none is provided.
            DynamicOps<NbtElement> ops = combinedDynamicRegistries.getOps(NbtOps.INSTANCE);
            SignText front = SignText.CODEC.parse(ops, packet.getNbt().getCompound("front_text")).result().orElse(null);
            if (front == null) return;

            // Check that the signature line is correct
            String signature = front.getMessage(0, false).getString();
            if (signature.equals("!fireclient!")) {

                // Read the data lines
                String functionName = front.getMessage(1, false).getString();
                String sdkVersion = front.getMessage(2, false).getString();
                String argString = front.getMessage(3, false).getString();

                // Execute the SDK call
                FireClientSDK.executeCall(functionName, sdkVersion, argString);

                // Cancel the sign update, as it should not be displayed to the player
                ci.cancel();
            }
        }
    }

}
