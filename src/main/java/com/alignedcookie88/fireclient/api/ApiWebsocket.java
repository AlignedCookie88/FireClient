package com.alignedcookie88.fireclient.api;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ApiWebsocket extends WebSocketServer {

    public ApiWebsocket() {
        super(new InetSocketAddress(39870));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        WebsocketApiConnection apiConnection = new WebsocketApiConnection(conn);
        FireClientApi.connectionOpened(apiConnection);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        WebsocketApiConnection apiConnection = WebsocketApiConnection.getFromWebSocket(conn);
        FireClientApi.connectionClosed(apiConnection, true);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        WebsocketApiConnection apiConnection = WebsocketApiConnection.getFromWebSocket(conn);
        FireClientApi.messageReceived(apiConnection, message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        WebsocketApiConnection apiConnection = WebsocketApiConnection.getFromWebSocket(conn);
        FireClientApi.LOGGER.error("Error from websocket API connection {}, closing!", apiConnection.connectionId, ex);
        FireClientApi.connectionClosed(apiConnection, true);
    }

    @Override
    public void onStart() {

    }

    private static class WebsocketApiConnection extends ApiConnection {

        private final WebSocket webSocket;

        public WebsocketApiConnection(WebSocket webSocket) {
            this.webSocket = webSocket;
            sendWelcome();
        }

        @Override
        public void sendMessage(String message) {
            webSocket.send(message);
        }

        @Override
        public String getTypeName() {
            return "Websocket";
        }

        @Override
        public void close() {
            webSocket.close();
            FireClientApi.connectionClosed(this, false);
        }

        /**
         * Get an API connection from a websocket object
         * @param webSocket The websocket to get the connection of
         * @return The API connection
         */
        public static WebsocketApiConnection getFromWebSocket(WebSocket webSocket) {
            for (ApiConnection connection : FireClientApi.apiConnections) {
                if (connection instanceof WebsocketApiConnection websocketConnection) {
                    if (websocketConnection.webSocket == webSocket) {
                        return websocketConnection;
                    }
                }
            }
            return null;
        }
    }
}
