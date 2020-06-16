package org.bitcoin.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Gestion des requetes Http
 * @author Bendavid Natane
 */

public class HttpClient {

    private static HttpClient http = new HttpClient();
    private String baseUri = "";
    private HashMap<String, String> defaultHeaders = new HashMap<String, String>();
    private HttpClient() {}

    public static HttpClient getHttp() {
        return HttpClient.http;
    }

    public String fetch(String url, String method) {
        try {
            URL callUri = this.getUri(url);
            HttpConnection httpConnection = new HttpConnection();
            return httpConnection.send(callUri, method, this.defaultHeaders);
        } catch(Exception e) {
            return null;
        }
    }

    public HttpClient setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    public HttpClient setDefaultHeaders(HashMap<String, String> headers) {
        this.defaultHeaders = headers;
        return this;
    }

    private URL getUri(String url) throws MalformedURLException {
        return new URL(this.baseUri + url);
    }

}

