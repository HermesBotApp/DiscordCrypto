package fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens;

public class CryptoCurrency {
    private final String assetId;
    private final String name;
    private final double priceUSD;

    public CryptoCurrency(String assetId, String name, double priceUSD) {
        this.assetId = assetId;
        this.name = name;
        this.priceUSD = priceUSD;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getName() {
        return name;
    }

    public double getPriceUSD() {
        return priceUSD;
    }
}
