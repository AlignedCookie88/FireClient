package com.alignedcookie88.fireclient.serialisation.variables;

import com.alignedcookie88.fireclient.serialisation.DFVariable;

public class FunctionParameterVariable implements DFVariable {

    public final String name;

    public final String type;

    public final boolean plural;

    public final boolean optional;

    public FunctionParameterVariable(String name, String type, boolean plural, boolean optional) {
        this.name = name;
        this.type = type;
        this.plural = plural;
        this.optional = optional;
    }

    @Override
    public String getID() {
        return "pn_el";
    }
}
