package com.alignedcookie88.fireclient.resources;

import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.Utility;
import com.google.common.collect.ImmutableList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TagDisplayData {

    private static Map<String, TagDisplayData> ALL;

    public static void cleanup() {
        ALL = new HashMap<>();
    }

    public static void register(String tag, TagDisplayData data) {
        ALL.put(tag, data);
    }

    public static @Nullable TagDisplayData get(String tag) {
        return ALL.get(tag);
    }

    public static List<TagDisplayData> forStack(ItemStack stack) {
        List<TagDisplayData> l = new ArrayList<>();
        NbtComponent component = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (component != null) {
            NbtCompound compound = component.copyNbt();
            NbtCompound pbv = compound.getCompound("PublicBukkitValues");
            Set<String> keys = pbv.getKeys();
            for (String key : keys) {
                if (key.startsWith("hypercube:")) {
                    String name = key.replaceFirst("hypercube:", "");
                    TagDisplayData p = get(name);
                    if (p != null)
                        l.add(p);
                }
            }
        }
        return ImmutableList.copyOf(l);
    }




    public String badge;

    public transient Text badgeText;




    public transient boolean parsed = false;

    public void parseData() {
        if (parsed)
            return;
        parsed = true;
        try {
            if (badge != null)
                badgeText = Utility.miniMessage(badge);
            else badgeText = Text.empty();
        } catch (Exception e) {
            FireClient.LOGGER.error("Failed to load tag display data", e);
        }
    }

}
