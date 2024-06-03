package com.alignedcookie88.fireclient;

import com.alignedcookie88.fireclient.serialisation.DFBlock;
import com.alignedcookie88.fireclient.serialisation.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public interface FireArgument {
    public Object parse(String raw);

    public String getID();

    public String getDFType();

    public default List<DFBlock> getFormattingBlocks(Variable var) {
        return new ArrayList<>();
    }
}
