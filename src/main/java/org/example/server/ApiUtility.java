package org.example.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ApiUtility {
    public static void sendErrorResponse(HttpExchange exchange, int code, String message) {
        try {
            exchange.sendResponseHeaders(code, message.getBytes().length);
            exchange.getResponseBody().write(message.getBytes());
            exchange.getResponseBody().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
