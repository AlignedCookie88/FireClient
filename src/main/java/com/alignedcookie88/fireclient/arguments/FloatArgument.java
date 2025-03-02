package com.alignedcookie88.fireclient.arguments;

import com.alignedcookie88.fireclient.FireArgument;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FloatArgument implements FireArgument {
    final String id;

    public FloatArgument(String id) {
        this.id = id;
    }

    @Override
    public Object parse(String raw) {
        return Float.valueOf(raw);
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
        return Text.literal("Number").formatted(Formatting.DARK_RED);
    }


}
