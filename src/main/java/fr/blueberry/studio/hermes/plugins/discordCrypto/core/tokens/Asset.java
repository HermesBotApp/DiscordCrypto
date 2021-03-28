package fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens;

public class Asset {
    private final String assetId;
    private final String assetUrl;

    public Asset(String assetId, String assetUrl) {
        this.assetId = assetId;
        this.assetUrl = assetUrl;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getAssetUrl() {
        return assetUrl;
    }
}
