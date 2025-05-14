package com.alignedcookie88.fireclient.legacy_sdk.arguments;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BooleanArgument extends IntegerArgument {
    public BooleanArgument(String id) {
        super(id);
    }

    @Override
    public Object parse(String raw) {
        int num = (Integer) super.parse(raw);
        if (num > 0)
            return true;
        return false;
    }

    @Override
    public Text getName() {
        return Text.literal("Boolean").formatted(Formatting.DARK_RED);
    }
}
