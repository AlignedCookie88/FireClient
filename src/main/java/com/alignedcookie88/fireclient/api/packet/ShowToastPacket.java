package com.alignedcookie88.fireclient.api.packet;

import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.api.ApiConnection;
import net.minecraft.text.Text;

public class ShowToastPacket extends ApiIncomingPacket {
    @Override
    public String getType() {
        return "show_toast";
    }

    @Override
    public void receive(ApiJsonReader data, ApiConnection connection) {
        String message = data.getString("message");
        Utility.sendToast(
                Text.literal(connection.applicationName + " (FireClient API)"),
                Text.literal(message)
        );
        connection.sendEmptySuccess();
    }
}
