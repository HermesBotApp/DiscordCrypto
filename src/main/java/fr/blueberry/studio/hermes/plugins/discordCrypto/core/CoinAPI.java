package fr.blueberry.studio.hermes.plugins.discordCrypto.core;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens.Asset;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CoinAPI {
    public static final DecimalFormat MONEY_VALUE_FORMATTER = new DecimalFormat("#.####");

    private final Gson gson;
    private final OkHttpClient client;

    public CoinAPI() {
        this.gson = new Gson();
        this.client = new OkHttpClient();
    }

    @SuppressWarnings("unused")
    private String request(String url) {
        return request(url, null);
    }

    private String request(String url, String args) {
        try {
            final Request request = new Request.Builder()
            .url("https://api.coincap.io/v2" + url + (args != null ? "?" + args : ""))
            .method("GET", null)
            .build();
        
            final Response response = client.newCall(request).execute();
            final ResponseBody body = response.body();

            return body.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "{}";
    }

    public Asset retrieveAsset(String search) {
        final String json = request("/assets", "limit=1&search=" + search);
        final ArrayList<Asset> assets = parseAssets(json);

        return assets.size() > 0 ? assets.get(0) : null;
    }

    public ArrayList<Asset> searchAssets(String search) {
        final String json = request("/assets", "&search=" + search);
        final ArrayList<Asset> assets = parseAssets(json);

        return assets;
    }

    public ArrayList<Asset> retrieveTop() {
        final String json = request("/assets", "limit=9");
        final ArrayList<Asset> assets = parseAssets(json);
        
        return assets;
    }

    public void close() {
        this.client.dispatcher().executorService().shutdown();
        this.client.connectionPool().evictAll();
    }

    private ArrayList<Asset> parseAssets(String json) {
        final JsonObject root = this.gson.fromJson(json, JsonObject.class);
        final ArrayList<Asset> assets = new ArrayList<>();

        if(root.has("data")) {
            final JsonArray data = root.get("data").getAsJsonArray();
            final Iterator<JsonElement> it = data.iterator();
            
            while(it.hasNext()) {
                final JsonObject dataEntry = it.next().getAsJsonObject();
                final String id = dataEntry.get("id").getAsString();
                final String name = dataEntry.get("name").getAsString();
                final String symbol = dataEntry.get("symbol").getAsString();
                final double priceUsd = !dataEntry.get("priceUsd").isJsonNull() ? dataEntry.get("priceUsd").getAsDouble() : 0.0D;
                final Asset asset = new Asset(id, symbol, name, priceUsd);

                assets.add(asset);
            }
        } 

        return assets;
    }
}
