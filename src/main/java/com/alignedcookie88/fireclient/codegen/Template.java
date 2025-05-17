package com.alignedcookie88.fireclient.codegen;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.codegen.block.CodeBlock;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.zip.GZIPOutputStream;

public class Template {

    protected final JsonArray blocks = new JsonArray();

    protected Text exportName = Text.literal("Code Template");

    public static Template merge(Collection<Template> templates) {
        Template t = new Template();
        for (Template e : templates) {
            for (JsonElement el : e.blocks) {
                t.blocks.add(el);
            }
        }
        return t;
    }

    public static Template merge(Template... templates) {
        return merge(List.of(templates));
    }

    /**
     * Merges multiple templates into a single until the max amount of blocks is reached, where it starts a new template.
     * If a single template is larger than the maximum amount of blocks, it will have its own template and will NOT be split.
     * @param templates A collection of templates to merge
     * @param maxCodeBlocks The maximum amount of code blocks in a single merged template
     * @param nameFunction Takes the current (merged) template index, and the total count, and returns a name for the item
     * @return A collection of merged templates
     */
    public static Collection<Template> mergeWithMax(Collection<Template> templates, int maxCodeBlocks, BiFunction<Integer, Integer, Text> nameFunction) {
        Collection<Template> merged = new ArrayList<>();

        Collection<Template> thisMerge = new ArrayList<>();
        int currentLength = 0;

        for (Template template : templates) {
            int length = template.getCodeBlockCount();
            if (currentLength+length > maxCodeBlocks && !thisMerge.isEmpty()) {
                merged.add(merge(thisMerge));
                thisMerge.clear();
            }
            currentLength += length;
            thisMerge.add(template);
        }

        if (!thisMerge.isEmpty()) {
            merged.add(merge(thisMerge));
        }

        int i = 0;
        for (Template template : merged) {
            template.withName(nameFunction.apply(i, merged.size()));
            i++;
        }

        return merged;
    }

    private Template bracket(String direction, boolean sticky) {
        JsonObject obj = new JsonObject();
        obj.add("id", new JsonPrimitive("bracket"));
        obj.add("direct", new JsonPrimitive(direction));
        obj.add("type", new JsonPrimitive(sticky ? "repeat" : "norm"));
        blocks.add(obj);
        return this;
    }


    public Template openBracket() {
        return bracket("open", false);
    }

    public Template openStickyBracket() {
        return bracket("open", true);
    }

    public Template closeBracket() {
        return bracket("close", false);
    }

    public Template closeStickyBracket() {
        return bracket("close", true);
    }


    public Template add(CodeBlock block) {
        blocks.add(block.toJson());
        return this;
    }


    public Template withName(Text name) {
        exportName = name;
        return this;
    }


    public int getCodeBlockCount() {
        int i = 0;
        for (JsonElement el : blocks) {
            JsonObject obj = el.getAsJsonObject();
            if (!(obj.get("id").getAsString().equals("bracket") && obj.get("direct").getAsString().equals("open")))
                i++;
        }
        return i;
    }


    public JsonElement serialise() {
        JsonObject obj = new JsonObject();
        obj.add("blocks", blocks.deepCopy());
        return obj;
    }

    public String serialiseAndCompress() {
        String uncompressed = serialise().toString();

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            GZIPOutputStream gzipOs = new GZIPOutputStream(os);

            gzipOs.write(uncompressed.getBytes(StandardCharsets.UTF_8));
            gzipOs.close();

            byte[] bytes = os.toByteArray();

            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public ItemStack toItem() {
        String data = serialiseAndCompress();

        JsonObject ctp = new JsonObject();
        ctp.add("author", new JsonPrimitive("FireClient"));
        ctp.add("name", new JsonPrimitive(exportName.getString()));
        ctp.add("version", new JsonPrimitive(1));
        ctp.add("code", new JsonPrimitive(data));

        ItemStack stack = new ItemStack(Items.CAMPFIRE);
        stack.set(DataComponentTypes.ITEM_NAME, exportName);

        NbtCompound nbt = new NbtCompound();
        NbtCompound pbv = new NbtCompound();
        pbv.put("hypercube:codetemplatedata", NbtString.of(ctp.toString()));
        nbt.put("PublicBukkitValues", pbv);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        return stack;
    }

}
