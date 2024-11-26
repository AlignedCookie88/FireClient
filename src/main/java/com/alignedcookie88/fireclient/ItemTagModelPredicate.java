package com.alignedcookie88.fireclient;

import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;

public class ItemTagModelPredicate implements ModelPredicateProvider {

    private final String item_tag;

    public ItemTagModelPredicate(String item_tag) {
        this.item_tag = item_tag;
    }

    @Override
    public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent == null)
            return Float.NEGATIVE_INFINITY;

        NbtCompound compound = nbtComponent.copyNbt();
        NbtCompound pbv = compound.getCompound("PublicBukkitValues");

        NbtElement elem = pbv.get("hypercube:"+item_tag);
        if (elem == null)
            return Float.NEGATIVE_INFINITY;
        if (!(elem instanceof AbstractNbtNumber num))
            return Float.NEGATIVE_INFINITY;

        return num.floatValue();
    }

}
