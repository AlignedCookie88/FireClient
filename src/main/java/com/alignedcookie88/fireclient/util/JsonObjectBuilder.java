package com.alignedcookie88.fireclient.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonObjectBuilder {

    private final JsonObject object;

    public JsonObjectBuilder() {
        object = new JsonObject();
    }

    public JsonObjectBuilder with(String name, JsonElement jsonElement) {
        object.add(name, jsonElement);
        return this;
    }

    public JsonObjectBuilder withString(String name, String value) {
        if (value == null)
            return this;
        return with(name, new JsonPrimitive(value));
    }

    public JsonObjectBuilder withBoolean(String name, Boolean value) {
        return with(name, new JsonPrimitive(value));
    }

    public JsonElement build() {
        return object.deepCopy();
    }

}
