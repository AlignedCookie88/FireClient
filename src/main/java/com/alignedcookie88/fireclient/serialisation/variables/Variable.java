package com.alignedcookie88.fireclient.serialisation.variables;

import com.alignedcookie88.fireclient.serialisation.DFVariable;

public class Variable implements DFVariable {

    public String name;

    public String scope;

    public Variable(String name, String scope) {
        this.name = name;
        this.scope = scope;
    }

    @Override
    public String getID() {
        return "var";
    }
}
