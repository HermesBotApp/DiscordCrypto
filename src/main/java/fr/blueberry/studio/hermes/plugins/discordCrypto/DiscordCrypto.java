package fr.blueberry.studio.hermes.plugins.discordCrypto;

import fr.blueberry.studio.hermes.api.plugins.Plugin;
import fr.blueberry.studio.hermes.plugins.discordCrypto.commands.CommandCrypto;
import fr.blueberry.studio.hermes.plugins.discordCrypto.core.CoinAPI;

public class DiscordCrypto extends Plugin {
    public static DiscordCrypto INSTANCE;
    public static CoinAPI COIN_API;

    @Override
    public void onLoad() {
        INSTANCE = this;
        COIN_API = new CoinAPI();
    }

    @Override
    public void onEnable() {
        getCommandRegistry().registerCommand(new CommandCrypto(), this);
    }

    @Override
    public void onDisable() {
        
    }
}
