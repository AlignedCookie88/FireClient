package com.alignedcookie88.fireclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    public void getTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> returnValue = cir.getReturnValue();

        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_CONTROL)) {
            returnValue.add(Text.empty());
            returnValue.add(Text.literal("Item Tags").formatted(Formatting.UNDERLINE));
            ItemStack stack = (ItemStack) (Object) this;
            NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
            if (component != null) {
                NbtCompound compound = component.copyNbt();
                NbtCompound pbv = compound.getCompound("PublicBukkitValues");
                Set<String> keys = pbv.getKeys();
                for (String key : keys) {
                    if (key.startsWith("hypercube:")) {
                        String name = key.replaceFirst("hypercube:", "");
                        NbtElement elem = pbv.get(key);
                        returnValue.add(Text.literal(" "+name).formatted(Formatting.GRAY).append(
                                Text.literal(": ").formatted(Formatting.DARK_GRAY)
                        ).append(
                                Text.literal(elem.asString()).formatted(Formatting.WHITE)
                        ));
                    }
                }
            }
        }

        cir.setReturnValue(returnValue);
    }

}
