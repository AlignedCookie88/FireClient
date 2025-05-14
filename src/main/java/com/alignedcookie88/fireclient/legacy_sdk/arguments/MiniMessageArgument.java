package com.alignedcookie88.fireclient.legacy_sdk.arguments;

import com.alignedcookie88.fireclient.legacy_sdk.FireArgument;
import com.alignedcookie88.fireclient.legacy_sdk.serialisation.DFArgument;
import com.alignedcookie88.fireclient.legacy_sdk.serialisation.DFBlock;
import com.alignedcookie88.fireclient.legacy_sdk.serialisation.blocks.SetVariableBlock;
import com.alignedcookie88.fireclient.legacy_sdk.serialisation.variables.Variable;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class MiniMessageArgument implements FireArgument {

    final String id;

    public MiniMessageArgument(String id) {
        this.id = id;
    }

    @Override
    public Object parse(String raw) {
        return MiniMessage.miniMessage().deserialize(raw);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getDFType() {
        return "comp";
    }

    @Override
    public List<DFBlock> getFormattingBlocks(Variable var) {
        List<DFBlock> blocks = new ArrayList<>();
        SetVariableBlock setVariableBlock = new SetVariableBlock("GetMiniMessageExpr");
        setVariableBlock.push_argument(
                new DFArgument(0, var)
        );
        setVariableBlock.push_argument(
                new DFArgument(1, var)
        );
        blocks.add(setVariableBlock);
        return blocks;
    }

    @Override
    public Text getName() {
        return Text.literal("Text").formatted(Formatting.DARK_GREEN);
    }
}
