package com.alignedcookie88.fireclient.codegen.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Optional;

public class ParameterItem extends CodeItem {

    protected final String name;
    protected final String type;
    protected final boolean plural;
    protected final boolean optional;
    protected final Optional<String> note;
    protected final Optional<String> description;
    protected final Optional<CodeItem> defaultValue;

    public ParameterItem(String name, String type, boolean plural, boolean optional, Optional<String> note, Optional<String> description, Optional<CodeItem> defaultValue) {
        this.name = name;
        this.type = type;
        this.plural = plural;
        this.optional = optional;
        this.note = note;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    @Override
    protected String getId() {
        return "pn_el";
    }

    @Override
    protected JsonElement getData() {
        JsonObject obj = new JsonObject();

        obj.add("name", new JsonPrimitive(name));
        obj.add("type", new JsonPrimitive(type));
        obj.add("plural", new JsonPrimitive(plural));
        obj.add("optional", new JsonPrimitive(optional));
        note.ifPresent(string -> obj.add("note", new JsonPrimitive(string)));
        description.ifPresent(string -> obj.add("description", new JsonPrimitive(string)));
        defaultValue.ifPresent(val -> obj.add("default", val.getData()));

        return obj;
    }
}
