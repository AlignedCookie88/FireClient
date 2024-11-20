package com.alignedcookie88.fireclient.serialisation;

import com.alignedcookie88.fireclient.FireClient;
import com.google.gson.Gson;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class DFTemplate {
    public List<DFBlock> blocks;

    public DFTemplate() {
        blocks = new ArrayList<>();
    }

    public void push(DFBlock block) {
        blocks.add(block);
    }

    public void pop(int index) {
        blocks.remove(index);
    }

    public String serialiseUnsafe() throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(obj);
        gzip.write(json.getBytes(StandardCharsets.UTF_8));
        gzip.close();
        return new String(Base64.getEncoder().encode(obj.toByteArray()));
    }

    public String serialise() {
        try {
            return serialiseUnsafe();
        } catch (IOException e) {
            return null;
        }
    }

    public ItemStack serialiseToItemStack(@NotNull Item itemType, Text name) {
        ItemStack stack = new ItemStack(itemType);
        if (name != null) stack.apply(DataComponentTypes.CUSTOM_NAME, name, com -> com);


        if (name == null) name = Text.literal("Template");

        CodeTemplateData data = new CodeTemplateData(name.getString(), serialise(), "FireClient");
        Gson gson = new Gson();
        String sdata = gson.toJson(data);


        NbtCompound publicBukkitValues = new NbtCompound();
        publicBukkitValues.putString("hypercube:codetemplatedata", sdata);

        NbtCompound customDataTag = new NbtCompound();
        customDataTag.put("PublicBukkitValues", publicBukkitValues);

        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(customDataTag), com -> com);

        return stack;
    }
}
