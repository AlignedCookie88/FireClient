package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.legacy_sdk.FireFunction;
import com.alignedcookie88.fireclient.sdk.FireClientSDK;
import com.alignedcookie88.fireclient.sdk.SDKFunction;
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

        BlockPos pos = ((SignBlockEntity) (Object) this).getPos();
        BlockPos orePos = pos.mutableCopy().add(1, 0, 0);
        World world = ((SignBlockEntity) (Object) this).getWorld();
        boolean isNotBeingBroken = world.getBlockState(orePos).getBlock() != Blocks.AIR;

        // Legacy Client Actions
        boolean alreadyKnownLegacyAction = Objects.equals(original.getMessage(0, false).getString(), "[L] CLIENT ACTION");
        if (alreadyKnownLegacyAction || Objects.equals(original.getMessage(0, false).getString(), "CALL FUNCTION")) {
            if (alreadyKnownLegacyAction || original.getMessage(1, false).getString().startsWith("fireclient.")) {
                String functionId = original.getMessage(1, false).getString().replaceFirst("fireclient\\.", "");

                if (world.isChunkLoaded(orePos) && world.getBlockState(orePos).getBlock() != Blocks.RED_MUSHROOM_BLOCK && isNotBeingBroken) {
                    world.setBlockState(orePos, Blocks.RED_MUSHROOM_BLOCK.getDefaultState());
                }

                if (alreadyKnownLegacyAction)
                    return;

                for (FireFunction function : FireClient.legacyFunctionRegistry) {
                    if (Objects.equals(function.getID(), functionId)) {
                        SignText newSignText = original
                                .withMessage(0, Text.literal("[L] CLIENT ACTION"))
                                .withMessage(1, Text.literal(function.getSignName()))
                                .withMessage(2, Text.literal("Update Me!").withColor(0xFF0000))
                                .withMessage(3, Text.literal("/fireclient sdk").withColor(0xFF0000));
                        frontText = newSignText;
                        cir.setReturnValue(newSignText);
                    }
                }
            }
        }


        // Client Actions
        boolean alreadyKnownClientAction = Objects.equals(original.getMessage(0, false).getString(), "CLIENT ACTION");
        if (alreadyKnownClientAction || Objects.equals(original.getMessage(0, false).getString(), "CALL FUNCTION")) {
            if (alreadyKnownClientAction || original.getMessage(1, false).getString().startsWith(".fireclient action/")) {
                String functionId = original.getMessage(1, false).getString().replaceFirst("\\.fireclient action/", "");
                SDKFunction function = FireClientSDK.getFunction(functionId);

                if (world.isChunkLoaded(orePos.getX(), orePos.getY()) && world.getBlockState(orePos).getBlock() != Blocks.REDSTONE_ORE && isNotBeingBroken)
                    world.setBlockState(orePos, Blocks.REDSTONE_ORE.getDefaultState());

                if (!alreadyKnownClientAction) {
                    SignText newSignText = original
                            .withMessage(0, Text.literal("CLIENT ACTION"))
                            .withMessage(1, Text.literal(function == null ? "" : function.getSignName()));
                    frontText = newSignText;
                    cir.setReturnValue(newSignText);
                }
            }
        }
    }
}
