package com.alignedcookie88.fireclient.api;

import com.alignedcookie88.fireclient.Config;
import com.alignedcookie88.fireclient.FireClient;
import com.alignedcookie88.fireclient.api.packet.ApiIncomingPacket;
import com.alignedcookie88.fireclient.api.packet.DoAuthPacket;
import com.alignedcookie88.fireclient.api.packet.IdentifyPacket;
import com.alignedcookie88.fireclient.api.packet.ShowToastPacket;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FireClientApi {

    public static final List<ApiConnection> apiConnections = new ArrayList<>();

    public static final Logger LOGGER = FireClient.createLogger("API");

    public static final List<Message> messages = new ArrayList<>();

    private static volatile boolean processing = false;

    public record Message(String message, ApiConnection apiConnection) {

    }

    /**
     * Set up the API, called at startup. Should be called no matter if the API is disabled.
     */
    public static void setup() {
        ApiIncomingPacket.register(new DoAuthPacket());
        ApiIncomingPacket.register(new IdentifyPacket());
        ApiIncomingPacket.register(new ShowToastPacket());
    }

    /**
     * Should be called when an API connection is opened.
     * @param apiConnection The api connection that has just been opened
     */
    public static void connectionOpened(ApiConnection apiConnection) {
        if (!running)
            return;

        apiConnections.add(apiConnection);
        LOGGER.info("API connection opened (uuid: {}, type: {})", apiConnection.connectionId, apiConnection.getTypeName());
    }

    /**
     * Should be called when an API connection is closed
     * @param apiConnection The api connection that has just been closed
     * @param remote Whether the connection was closed by the remote program
     */
    public static void connectionClosed(ApiConnection apiConnection, boolean remote) {
        if (!running)
            return;

        apiConnections.remove(apiConnection);
        LOGGER.info("API connection closed (uuid: {}, type: {}, remote: {})", apiConnection.connectionId, apiConnection.getTypeName(), remote);
    }

    /**
     * Should be called when a message is received from an API connection, the message will be processed the next time process() is called.
     * @param apiConnection The api connection the message is coming from
     * @param message The incoming message
     */
    public static void messageReceived(ApiConnection apiConnection, String message) {
        if (!running)
            return;

        while (processing) {
            Thread.onSpinWait();
        };
        messages.add(new Message(message, apiConnection));
    }

    private static boolean running = false;

    private static ApiWebsocket websocket;

    /**
     * Starts the API, if enabled in the config.
     */
    public static void start() {
        if (running)
            return;

        if (!Config.state.apiEnabled)
            return;

        LOGGER.info("Starting...");

        websocket = new ApiWebsocket();
        websocket.start();

        LOGGER.info("Done!");

        running = true;
    }

    /**
     * Stops the API, forcefully disconnecting all applications.
     */
    public static void stop() {
        if (!running)
            return;

        LOGGER.info("Stopping...");

        try {
            websocket.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        apiConnections.clear();

        LOGGER.info("Done!");

        running = false;
    }

    /**
     * Process all messages in the internal buffer. Should be called on the render thread.
     */
    public static void process() {

        if (!running)
            return;

        processing = true;

        for (Message message : messages) {
            process(message);
        }

        messages.clear();

        processing = false;

    }

    /**
     * Process a message
     * @param message The message to process
     */
    protected static void process(Message message) {
        LOGGER.info("Processing message {}", message);
        ApiIncomingPacket.receive(message.message(), message.apiConnection());
    }

}
