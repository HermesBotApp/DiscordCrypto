package fr.blueberry.studio.hermes.plugins.discordCrypto;

import fr.blueberry.studio.hermes.api.plugins.Plugin;

public class DiscordCrypto extends Plugin {
    public static DiscordCrypto INSTANCE;

    @Override
    public void onLoad() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        
    }
}
