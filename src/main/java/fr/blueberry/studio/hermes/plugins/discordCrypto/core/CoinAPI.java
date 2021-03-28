package fr.blueberry.studio.hermes.plugins.discordCrypto.core;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens.Asset;
import fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens.CryptoCurrency;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CoinAPI {
    private final Gson gson;
    private final String apiKey;
    private final OkHttpClient client;
    private final Set<Asset> assets;

    public CoinAPI(String apiKey) {
        this.gson = new Gson();
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.assets = this.loadAssets();
    }

    private String request(String url) throws IOException {
        final Request request = new Request.Builder()
            .url("http://rest-sandbox.coinapi.io" + url)
            .addHeader("X-CoinAPI-Key", apiKey)
            .build();
        
        final Response response = client.newCall(request).execute();
        final ResponseBody body = response.body();

        return body.string();
    }

    private Set<Asset> loadAssets() {
        try {
            final Set<Asset> assets = new HashSet<>();
            final String json = request("/v1/assets/icons/128");
            final JsonArray assetJsonArray = this.gson.fromJson(json, JsonArray.class);
            final Iterator<JsonElement> it = assetJsonArray.iterator();

            JsonElement element;
            while(it.hasNext()) {
                element = it.next();

                if(element.isJsonObject()) {
                    JsonObject jsonObject = element.getAsJsonObject();

                    assets.add(new Asset(jsonObject.get("asset_id").getAsString(), jsonObject.get("url").getAsString()));
                }
            }

            return assets;
        } catch(IOException e) {
            return new HashSet<>();
        }
    }

    public void close() {
        this.client.dispatcher().executorService().shutdown();
        this.client.connectionPool().evictAll();
    }

    public CryptoCurrency getCrypto(String assetId) {
        try {
            final String json = request("/v1/assets/" + assetId);
            final JsonObject jsonObject = this.gson.fromJson(json, JsonArray.class).get(0).getAsJsonObject();
            System.out.println(json);
            final CryptoCurrency cryptoCurrency = new CryptoCurrency(jsonObject.get("asset_id").getAsString(), jsonObject.get("name").getAsString(), jsonObject.get("price_usd").getAsDouble());
            
            return cryptoCurrency;
        } catch (IOException e) {
            return null;
        }
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public String getSymbols() {
        try {
            return request("/v1/symbols");
        } catch(IOException e) {
            return "{}";
        }
    }

    public Asset getAsset(String assetId) {
        return this.assets.stream().filter(a -> a.getAssetId().toUpperCase().equals(assetId.toUpperCase())).findFirst().get();
    }
}
