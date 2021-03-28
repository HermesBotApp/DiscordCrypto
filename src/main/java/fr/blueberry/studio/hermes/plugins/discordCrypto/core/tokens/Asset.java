package fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens;

public class Asset {
    private static final String ASSET_IMAGE_BASE_URL = "https://static.coincap.io/assets/icons/%symbol%@2x.png";

    private final String id;
    private final String symbol;
    private final String name;
    private final double priceUsd;

    public Asset(String id, String symbol, String name, double priceUsd) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.priceUsd = priceUsd;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPriceUsd() {
        return priceUsd;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAssetUrl() {
        return ASSET_IMAGE_BASE_URL.replace("%symbol%", getSymbol().toLowerCase());
    }
}
