package com.alignedcookie88.fireclient.codegen.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;

public class VanillaItem extends CodeItem {

    protected final ItemStack stack;

    public VanillaItem(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    protected String getId() {
        return "item";
    }

    @Override
    protected JsonElement getData() {
        NbtElement nbt = stack.encode(MinecraftClient.getInstance().world.getRegistryManager());
        String snbt = nbt.asString();

        JsonObject obj = new JsonObject();
        obj.add("item", new JsonPrimitive(snbt));
        return obj;
    }
}
