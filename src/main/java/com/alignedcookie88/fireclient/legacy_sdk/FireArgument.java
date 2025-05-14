package com.alignedcookie88.fireclient.legacy_sdk;

import com.alignedcookie88.fireclient.legacy_sdk.serialisation.DFBlock;
import com.alignedcookie88.fireclient.legacy_sdk.serialisation.variables.Variable;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public interface FireArgument {
    public Object parse(String raw);

    public String getID();

    public String getDFType();

    public default List<DFBlock> getFormattingBlocks(Variable var) {
        return new ArrayList<>();
    }

    Text getName();
}
