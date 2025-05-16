package com.alignedcookie88.fireclient.codegen.block;

import com.alignedcookie88.fireclient.codegen.item.TagItem;

public class FunctionBlock extends DataBlock {
    public FunctionBlock(String name, boolean hidden) {
        super(name);
        withItem(new TagItem(
                "Is Hidden",
                hidden ? "True" : "False",
                this
        ));
    }

    @Override
    public String getType() {
        return "func";
    }
}
