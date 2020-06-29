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

    public static String getSymbolvalue(String key) {
        App.logger.debug("getSymbolvalue");
        JSONObject value = json.getJSONObject(key);
        return value.getString("symbol");
    }

    public static Double getlastPriceBitcoinvalue(String key) {
        App.logger.debug("getlastPriceBitcoinvalue");
        JSONObject value = json.getJSONObject(key);
        return value.getDouble("last");
    }

    public static ArrayList<String> currentPriceMarketBitcoin(String dateStart, String dateFinish) {
        final String apiCoindesk = "https://api.coindesk.com/v1/bpi/historical/close.json?start=" + dateStart + "&end=" + dateFinish;
        HttpClient httpCoindesk = HttpClient.getHttp().setBaseUri(apiCoindesk);
        App.logger.debug("Call api: " + apiCoindesk);
        String response = httpCoindesk.fetch("", "GET");
        System.out.println(response);

        JSONObject jsonBitcoin = new JSONObject(response);
        System.out.println(jsonBitcoin);

        jsonCoindesk = jsonBitcoin.getJSONObject("bpi");
        System.out.println(jsonCoindesk);
        ArrayList<String> moneyUs = new ArrayList<>();
        try {
            Iterator<String> keys = jsonCoindesk.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                moneyUs.add(key);
            }
        } catch (Exception exception) {
            Modal.showModalError(exception.getMessage());
        }
        Collections.sort(moneyUs);
        return moneyUs;
    }

    public static Double getPriceBitcoinWithDate(String key) {
        return jsonCoindesk.getDouble(key);
    }

    public static Double getMaxPriceBitcoin() {
        Double maxValue = 0.0;
        try {
            Iterator<String> keys = jsonCoindesk.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                Double value = jsonCoindesk.getDouble(key);
                if (value > maxValue) {
                    maxValue = value;
                }
            }
        } catch (Exception exception) {
            Modal.showModalError(exception.getMessage());
        }
        return maxValue;
    }

    public static Double getMinPriceBitcoin() {
        Double minValue = Double.MAX_VALUE;
        try {
            Iterator<String> keys = jsonCoindesk.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                Double value = jsonCoindesk.getDouble(key);
                if (value < minValue) {
                    minValue = value;
                }
            }
        } catch (Exception exception) {
            Modal.showModalError(exception.getMessage());
        }
        return minValue;
    }
}
