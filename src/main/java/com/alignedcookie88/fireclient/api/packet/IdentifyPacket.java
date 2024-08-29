package com.alignedcookie88.fireclient.api.packet;

import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.api.ApiConnection;
import net.minecraft.text.Text;

public class IdentifyPacket extends ApiIncomingPacket {
    @Override
    public String getType() {
        return "identify";
    }

    @Override
    public void receive(ApiJsonReader data, ApiConnection connection) {
        String name = data.getString("name");
        connection.setApplicationName(name);

        connection.sendEmptySuccess();
    }
}
