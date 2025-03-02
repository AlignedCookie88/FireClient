package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.FireFunction;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = SignBlockEntity.class, priority = 999)
public class SignBlockEntityMixin {
    @Shadow
    private SignText frontText;

    @Inject(method = "getFrontText", at = @At("HEAD"), cancellable = true)
    public void getFrontText(CallbackInfoReturnable<SignText> cir) {
        SignText original = this.frontText;
        boolean isAlreadyKnown = Objects.equals(original.getMessage(0, false).getString(), "CLIENT ACTION");
        if (isAlreadyKnown || Objects.equals(original.getMessage(0, false).getString(), "CALL FUNCTION")) {
            if (isAlreadyKnown || original.getMessage(1, false).getString().startsWith("fireclient.")) {
                String functionId = original.getMessage(1, false).getString().replaceFirst("fireclient\\.", "");

                BlockPos pos = ((SignBlockEntity) (Object) this).getPos();
                BlockPos oreBlock = pos.mutableCopy().add(1, 0, 0);
                World world = ((SignBlockEntity) (Object) this).getWorld();

                if (world.isChunkLoaded(oreBlock) && world.getBlockState(oreBlock).getBlock() != Blocks.REDSTONE_ORE) {
                    world.setBlockState(oreBlock, Blocks.REDSTONE_ORE.getDefaultState());
                }

                if (isAlreadyKnown)
                    return;

                for (FireFunction function : FireClient.functionRegistry) {
                    if (Objects.equals(function.getID(), functionId)) {
                        SignText newSignText = original
                                .withMessage(0, Text.literal("CLIENT ACTION"))
                                .withMessage(1, Text.literal(function.getSignName()));
                        frontText = newSignText;
                        cir.setReturnValue(newSignText);
                    }
                }
            }
        }
    }
}
