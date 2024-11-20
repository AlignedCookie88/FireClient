package com.alignedcookie88.fireclient.serialisation.variables;

import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.serialisation.DFVariable;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;

public class ItemVariable implements DFVariable {

    public String item;

    public ItemVariable(String item) {
        this.item = item;
    }

    public ItemVariable(ItemStack stack) {
        this(stack.encode(Utility.getRegistryWrapper()).asString());
    }

    public ItemStack getStack() {
        try {
            return ItemStack.fromNbtOrEmpty(Utility.getRegistryWrapper(), StringNbtReader.parse(item));
        } catch (CommandSyntaxException e) {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public String getID() {
        return "item";
    }
}
