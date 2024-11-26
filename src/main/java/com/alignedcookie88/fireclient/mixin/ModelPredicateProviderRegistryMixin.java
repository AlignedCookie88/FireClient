package com.alignedcookie88.fireclient.mixin;

import com.alignedcookie88.fireclient.ItemTagModelPredicate;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelPredicateProviderRegistry.class)
public class ModelPredicateProviderRegistryMixin {


    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private static void get(ItemStack stack, Identifier id, CallbackInfoReturnable<ModelPredicateProvider> cir) {
        if (id.getNamespace().equals("fireclient") && id.getPath().startsWith("item_tag/")) {
            String item_tag = id.getPath().replaceFirst("item_tag/", "");
            cir.setReturnValue(new ItemTagModelPredicate(item_tag));
        }
    }

}
