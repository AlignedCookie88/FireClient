package com.alignedcookie88.fireclient.serialisation.blocks;

import com.alignedcookie88.fireclient.serialisation.DFArgument;
import com.alignedcookie88.fireclient.serialisation.DFBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerActionBlock implements DFBlock {

    public String id = "block";

    public String block = "player_action";

    public final String action;

    public final HashMap<String, List<DFArgument>> args = new HashMap<>();

    public PlayerActionBlock(String name) {
        action = name;
        List<DFArgument> list = new ArrayList<>();
        args.put("items", list);
    }

    public void push_argument(DFArgument argument) {
        args.get("items").add(argument);
    }

    public void pop_variable(int index) {
        args.get("items").remove(index);
    }
}
