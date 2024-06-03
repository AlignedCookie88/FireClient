package com.alignedcookie88.fireclient.serialisation;

public class DFArgument {
    public Integer slot;

    public DFVariableWrapper item;

    public DFArgument(Integer slot, DFVariable variable) {
        this.slot = slot;
        this.item = new DFVariableWrapper(variable);
    }
}
