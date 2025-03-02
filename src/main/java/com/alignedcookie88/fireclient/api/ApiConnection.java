package com.alignedcookie88.fireclient.api;

import com.alignedcookie88.fireclient.Utility;
import com.alignedcookie88.fireclient.util.JsonObjectBuilder;
import com.google.gson.JsonElement;
import net.minecraft.text.Text;

import java.util.UUID;

public abstract class ApiConnection {

    public final UUID connectionId;

    public String applicationName;

    protected ApiConnection() {
        this(UUID.randomUUID());
    }

    protected ApiConnection(UUID uuid) {
        connectionId = uuid;
        applicationName = generateName();
    }

    protected String generateName() {
        return "$" + connectionId.toString();
    }

    /**
     * Sends a welcome message to the application. It likely expects this, this method MUST be called by the API connection implementation of choice when a client first connects.
     */
    protected void sendWelcome() {
        sendMessage(new JsonObjectBuilder()
                .withString("_", "Welcome! If this is your first time using the FireClient API you can find our documentation at https://github.com/AlignedCookie88/FireClient/wiki/API.")
                .withString("id", connectionId.toString())
                .build());
    }

    public void setApplicationName(String string) {
        FireClientApi.LOGGER.info("Setting name of application {} to \"{}\".", connectionId, string);
        this.applicationName = string;
    }

    /**
     * Send a message over the connection
     * @param message The message to send
     */
    public abstract void sendMessage(String message);

    /** Send a JSON message over the connection
     * @param message The JSON to send
     */
    public void sendMessage(JsonElement message) {
        sendMessage(message.toString());
    }

    /**
     * Send an empty success packet
     */
    public void sendEmptySuccess() {
        sendMessage(new JsonObjectBuilder()
                .withBoolean("success", true)
                .build());
    }

    /**
     * Send a success packet with a response
     * @param response The response JSON to send
     */
    public void sendSuccessWithResponse(JsonElement response) {
        sendMessage(new JsonObjectBuilder()
                .withBoolean("success", true)
                .with("response", response)
                .build());
    }

    /**
     * Send an error packet
     * @param e The error to send
     */
    public void sendError(Exception e) {
        sendMessage(new JsonObjectBuilder()
                .withBoolean("success", false)
                .with("error", new JsonObjectBuilder()
                        .withString("type", e.getClass().getTypeName())
                        .withString("message", e.getMessage())
                        .withString("For more info", "see game log")
                        .build())
                .build());
        if (!(e instanceof FireClientApiUserFaultException))
            Utility.sendToast(
                    Text.literal("FireClient API Error"),
                    Text.literal("An application (%s) sent an invalid API request.".formatted(applicationName))
            );
    }

    /**
     * Gets the type of connection
     * @return The type name
     */
    public abstract String getTypeName();

    /**
     * Closes the connection
     */
    public abstract void close();

}
