package org.bitcoin.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Gestion de la connection pour les requetes Http
 * @author Bendavid Natane
 */

class HttpConnection {

    HttpConnection() {}

    String send(URL uri, String method, HashMap<String, String> defaultHeaders) throws IOException {
        HttpURLConnection connection = this.openConnection(uri, method, defaultHeaders);
        String response = this.readResponse(this.getResponseStream(connection));
        connection.disconnect();
        return response;
    }

    private String readResponse(InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private HttpURLConnection openConnection(URL uri, String method, HashMap<String, String> defaultHeaders) throws IOException {
        HttpURLConnection connection = null;
        connection = (HttpURLConnection)uri.openConnection();
        connection.setRequestMethod(method);
        this.setDefaultHeaders(connection, defaultHeaders);
        return connection;
    }

    private HttpURLConnection setDefaultHeaders(HttpURLConnection connection, HashMap<String, String> defaultHeaders) {
        defaultHeaders.entrySet().stream().forEach(e -> connection.setRequestProperty(e.getKey(), e.getValue()));
        return connection;
    }

    private InputStream getResponseStream(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() > 299) {
            return connection.getErrorStream();
        } else {
            return connection.getInputStream();
        }
    }

}

