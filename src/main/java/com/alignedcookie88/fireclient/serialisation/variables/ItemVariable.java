package com.alignedcookie88.fireclient.serialisation.variables;

import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.serialisation.DFVariable;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringNbtReader;

public class ItemVariable implements DFVariable {

    public final String item;

    public ItemVariable(String item) {
        this.item = item;
    }

    public ItemVariable(ItemStack stack) {
        this(ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack).getOrThrow().asString().orElse(""));
    }

    public ItemStack getStack() {
        try {
            return ItemStack.CODEC.decode(NbtOps.INSTANCE, StringNbtReader.readCompound(item)).getOrThrow().getFirst();
        } catch (CommandSyntaxException e) {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public String getID() {
        return "item";
    }
}
