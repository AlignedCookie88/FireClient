package com.alignedcookie88.fireclient.legacy_sdk.serialisation;

public class DFVariableWrapper {
    public final DFVariable data;

    public final String id;

    public DFVariableWrapper(DFVariable data) {
        this.id = data.getID();
        this.data = data;
    }
}
