package com.alignedcookie88.fireclient.arguments;

import com.alignedcookie88.fireclient.FireArgument;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class IntegerArgument implements FireArgument {
    String id;

    public IntegerArgument(String id) {
        this.id = id;
    }

    @Override
    public Object parse(String raw) {
        return Integer.valueOf(raw);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getDFType() {
        return "num";
    }

    @Override
    public Text getName() {
        return Text.literal("Integer").formatted(Formatting.DARK_RED);
    }
}
