package com.alignedcookie88.fireclient.arguments;

import com.alignedcookie88.fireclient.FireArgument;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class StringArgument implements FireArgument {

    final String id;

    public StringArgument(String id) {
        this.id = id;
    }
    @Override
    public Object parse(String raw) {
        return raw;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getDFType() {
        return "txt";
    }

    @Override
    public Text getName() {
        return Text.literal("String").formatted(Formatting.DARK_AQUA);
    }
}
