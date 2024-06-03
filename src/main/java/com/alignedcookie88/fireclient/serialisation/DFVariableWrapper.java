package com.alignedcookie88.fireclient.serialisation;

public class DFVariableWrapper {
    public DFVariable data;

    public String id;

    public DFVariableWrapper(DFVariable data) {
        this.id = data.getID();
        this.data = data;
    }
}
