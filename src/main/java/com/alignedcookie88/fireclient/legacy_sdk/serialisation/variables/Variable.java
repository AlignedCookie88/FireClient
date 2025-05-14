package com.alignedcookie88.fireclient.legacy_sdk.serialisation.variables;

import com.alignedcookie88.fireclient.legacy_sdk.serialisation.DFVariable;

public class Variable implements DFVariable {

    public final String name;

    public final String scope;

    public Variable(String name, String scope) {
        this.name = name;
        this.scope = scope;
    }

    @Override
    public String getID() {
        return "var";
    }
}
