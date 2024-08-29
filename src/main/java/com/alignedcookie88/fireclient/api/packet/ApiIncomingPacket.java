package com.alignedcookie88.fireclient.api.packet;

import com.alignedcookie88.fireclient.api.ApiConnection;
import com.alignedcookie88.fireclient.api.FireClientApi;
import com.alignedcookie88.fireclient.api.FireClientApiException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ApiIncomingPacket {

    private static final List<ApiIncomingPacket> packetTypes = new ArrayList<>();

    public static void register(ApiIncomingPacket packet) {
        packetTypes.add(packet);
    }

    public static void receive(String string, ApiConnection connection) {
        try {
            JsonElement jsonElement = JsonParser.parseString(string);
            JsonObject object = jsonElement.getAsJsonObject();
            String type = object.get("type").getAsString();
            JsonObject data = object.get("data").getAsJsonObject();
            for (ApiIncomingPacket packet : packetTypes) {
                if (Objects.equals(packet.getType(), type)) {
                    packet.receive(new ApiJsonReader(data), connection);
                    return;
                }
            }
            throw new FireClientApiException("There is no packet type with id \"%s\".".formatted(type));
        } catch (Exception e) {
            FireClientApi.LOGGER.error("Exception whilst handling API packet from connection {}.", connection.connectionId, e);
            connection.sendError(e);
        }
    }

    public abstract String getType();

    public abstract void receive(ApiJsonReader data, ApiConnection connection);

    public static class ApiJsonReader {

        private final JsonObject jsonObject;

        public ApiJsonReader(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        private interface Wrapper<T> {
            T call();
        }

        private <T> T wrapExceptions(Wrapper<T> wrapper) {
            try {
                return wrapper.call();
            } catch (Exception e) {
                throw new FireClientApiException("Exception whilst parsing JSON data.", e);
            }
        }

        public ApiJsonReader getSubObject(String name) {
            return wrapExceptions(() -> new ApiJsonReader(jsonObject.get("name").getAsJsonObject()));
        }

        public String getString(String name) {
            return wrapExceptions(() -> jsonObject.get(name).getAsString());
        }

        public JsonObject getObject() {
            return jsonObject.deepCopy();
        }

    }

}
