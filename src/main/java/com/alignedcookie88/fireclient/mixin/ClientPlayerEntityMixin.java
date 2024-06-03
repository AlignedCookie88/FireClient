package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.State;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    /**
     * @author AlignedCookie88
     * @reason need to stop movement sometimes
     */
    @Overwrite
    public boolean canMoveVoluntarily() {
        return State.canMove;
    }
}
