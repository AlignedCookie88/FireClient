package com.alignedcookie88.fireclient.serialisation;

public class DFArgument {
    public final Integer slot;

    public final DFVariableWrapper item;

    public DFArgument(Integer slot, DFVariable variable) {
        this.slot = slot;
        this.item = new DFVariableWrapper(variable);
    }
}
