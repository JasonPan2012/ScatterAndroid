package com.mariabeyrak.scatter.socket;

import android.util.Log;

import com.mariabeyrak.scatter.ScatterClient;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.InetSocketAddress;

import static com.mariabeyrak.scatter.socket.SocketsConstants.MESSAGE_START;
import static com.mariabeyrak.scatter.socket.models.Commands.API;
import static com.mariabeyrak.scatter.socket.models.Commands.PAIR;
import static com.mariabeyrak.scatter.socket.models.Commands.REKEYED;

class ScatterWebSocket extends WebSocketServer {

    private static String TAG = "SOCKET";

    private ScatterClient scatterClient;

    public ScatterWebSocket(ScatterClient scatterClient) {
        super(new InetSocketAddress("127.0.0.1", 50005));
        this.scatterClient = scatterClient;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.i(TAG, "Open");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.i(TAG, "Close");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        showLongLog(message);

        if (!message.startsWith(MESSAGE_START)) return;

        try {
            JSONArray params = new JSONArray(message.substring(MESSAGE_START.length()));

            switch (params.getString(0)) {
                case PAIR: {
                    ScatterSocketService.handlePairResponse(conn);
                    break;
                }
                case API: {
                    ScatterSocketService.handleApiResponse(conn, params, scatterClient);
                    break;
                }
                case REKEYED: {
                    ScatterSocketService.handleRekeyResponse(conn);
                    break;
                }
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.i(TAG, "Error: " + ex.toString());
    }

    @Override
    public void onStart() {
        Log.i(TAG, "Started");
    }

    private void showLongLog(String s) {
        final int chunkSize = 2048;
        for (int i = 0; i < s.length(); i += chunkSize) {
            Log.d(TAG, s.substring(i, Math.min(s.length(), i + chunkSize)));
        }
    }
}
