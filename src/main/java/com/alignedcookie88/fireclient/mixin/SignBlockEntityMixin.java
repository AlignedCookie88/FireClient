package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.FireFunction;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.text.Text;
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
        if (Objects.equals(original.getMessage(0, false).getString(), "CALL FUNCTION")) {
            if (original.getMessage(1, false).getString().startsWith("fireclient.")) {
                String functionId = original.getMessage(1, false).getString().replaceFirst("fireclient\\.", "");
                for (FireFunction function : FireClient.functionRegistry) {
                    if (Objects.equals(function.getID(), functionId)) {
                        SignText newSignText = original
                                .withMessage(0, Text.literal("CLIENT ACTION"))
                                .withMessage(1, Text.literal(function.getName().replace(" ", "")));
                        frontText = newSignText;
                        cir.setReturnValue(newSignText);
                    }
                }
            }
        }
    }
}
