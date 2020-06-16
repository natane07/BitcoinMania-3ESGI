package org.bitcoin.api;

import org.bitcoin.http.HttpClient;
import org.json.JSONObject;

/**
 * Classe pour l'api bitcoin
 * @author Bendavid Natane
 */

public class ApiBitcoin {
    private static final String apiUri = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private static HttpClient http = HttpClient.getHttp().setBaseUri(apiUri);

    public static void test() {
        String response = http.fetch("", "GET");
        JSONObject json = new JSONObject(response);
        JSONObject categories = json.getJSONObject("time");
        System.out.println(categories);
        System.out.println(categories.getString("updateduk"));
    }
}
