package com.alignedcookie88.fireclient.legacy_sdk.serialisation.variables;

import com.alignedcookie88.fireclient.legacy_sdk.serialisation.DFVariable;

public class StringVariable implements DFVariable {

    public final String name;

    public StringVariable(String name) {
        this.name = name;
    }

    @Override
    public String getID() {
        return "txt";
    }
}
