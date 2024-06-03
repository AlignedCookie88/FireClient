package com.alignedcookie88.fireclient.serialisation.variables;

import com.alignedcookie88.fireclient.serialisation.DFVariable;

public class FunctionParameterVariable implements DFVariable {

    public String name;

    public String type;

    public boolean plural;

    public boolean optional;

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
