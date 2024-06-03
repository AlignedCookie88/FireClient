package com.alignedcookie88.fireclient.serialisation.variables;

import com.alignedcookie88.fireclient.serialisation.DFVariable;

public class StringVariable implements DFVariable {

    public String name;

    public StringVariable(String name) {
        this.name = name;
    }

    @Override
    public String getID() {
        return "txt";
    }
}
