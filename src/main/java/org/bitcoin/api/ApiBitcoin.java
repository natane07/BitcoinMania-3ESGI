package org.bitcoin.api;

import org.bitcoin.app.App;
import org.bitcoin.http.HttpClient;
import org.bitcoin.utils.Modal;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

/**
 * Classe pour l'api bitcoin
 * @author Bendavid Natane
 */

public class ApiBitcoin {
    private static final String apiUri = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private static HttpClient http = HttpClient.getHttp().setBaseUri(apiUri);
    private static JSONObject json;
    private static JSONObject jsonCoindesk;

    public static void test() {
        String response = http.fetch("", "GET");
        JSONObject json = new JSONObject(response);
        JSONObject categories = json.getJSONObject("time");
        System.out.println(categories);
        System.out.println(categories.getString("updateduk"));
    }

    public static ArrayList<String> getAllExchange() {
        final String apiUriBitcoin = "https://blockchain.info";
        HttpClient httpBitcoin = HttpClient.getHttp().setBaseUri(apiUriBitcoin);
        App.logger.debug("Call api: " + apiUriBitcoin);
        String response = httpBitcoin.fetch("/ticker", "GET");
        json = new JSONObject(response);
        Iterator<String> keys = json.keys();
        ArrayList<String> money = new ArrayList<>();
        while(keys.hasNext()) {
            String key = keys.next();
            if (json.get(key) instanceof JSONObject) {
                money.add(key);
            }
        }
        return money;
    }
}
