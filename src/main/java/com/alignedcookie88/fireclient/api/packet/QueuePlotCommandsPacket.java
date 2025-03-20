package com.alignedcookie88.fireclient.api.packet;

import com.alignedcookie88.fireclient.CommandQueue;
import com.alignedcookie88.fireclient.api.ApiConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class QueuePlotCommandsPacket extends ApiIncomingPacket {
    @Override
    public String getType() {
        return "queue_plot_commands";
    }

    @Override
    public void receive(ApiJsonReader data, ApiConnection connection) {
        JsonArray commands = data.getArray("commands");
        for (JsonElement el : commands) {
            try {
                String command = el.getAsString();
                CommandQueue.queuePlotCommand(command);
            } catch (Exception ignored) {
                // Do nothing with invalid commands
            }
        }
        connection.sendEmptySuccess();
    }
}
