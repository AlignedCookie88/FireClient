package com.alignedcookie88.fireclient.codegen.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class LocationItem extends CodeItem {

    private final boolean isBlock;

    private final float x;
    private final float y;
    private final float z;

    private final float pitch;
    private final float yaw;

    public LocationItem(boolean isBlock, float x, float y, float z, float pitch, float yaw) {
        this.isBlock = isBlock;

        this.x = x;
        this.y = y;
        this.z = z;

        this.pitch = pitch;
        this.yaw = yaw;
    }

    public LocationItem(boolean isBlock, float x, float y, float z) {
        this(isBlock, x, y, z, 0, 0);
    }

    public LocationItem(int x, int y, int z) {
        this(true, x, y, z);
    }

    @Override
    protected String getId() {
        return "loc";
    }

    @Override
    protected JsonElement getData() {
        JsonObject obj = new JsonObject();

        obj.add("isBlock", new JsonPrimitive(isBlock));

        JsonObject loc = new JsonObject();

        loc.add("x", new JsonPrimitive(x));
        loc.add("y", new JsonPrimitive(y));
        loc.add("z", new JsonPrimitive(z));

        loc.add("pitch", new JsonPrimitive(pitch));
        loc.add("yaw", new JsonPrimitive(yaw));

        obj.add("loc", loc);

        return obj;
    }
}
