package fr.blueberry.studio.hermes.plugins.discordCrypto.commands;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Set;

import fr.blueberry.studio.hermes.api.app.Sender;
import fr.blueberry.studio.hermes.api.commands.Command;
import fr.blueberry.studio.hermes.api.utils.MessageEmbedHelper;
import fr.blueberry.studio.hermes.plugins.discordCrypto.DiscordCrypto;
import fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens.Asset;
import fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens.CryptoCurrency;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class CommandCrypto extends Command {

    @Override
    public String getLabel() {
        return "crypto";
    }

    @Override
    public void execute(Sender sender, String[] args) {
        final String assetId = args[0];

        if(args[0].contentEquals("test")) {
            System.out.println(DiscordCrypto.COIN_API.getSymbols());
            return;
        }
        if(args[0].equalsIgnoreCase("list")) {
            final Set<Asset> assets = DiscordCrypto.COIN_API.getAssets();
            final StringBuilder sb = new StringBuilder();

            for(Asset asset : assets) {
                sb.append(asset.getAssetId()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);

            final int pagesCount = sb.length() % 2048 == 0 ? sb.length() / 2048 : (sb.length() / 2048) + 1;
            System.out.println(pagesCount);

            for(int i = 0; i < pagesCount; i++) {
                final MessageEmbed embed = MessageEmbedHelper.fastEmbed("Liste des cryptomonnaies disponibles (" + (i + 1) + "/" + pagesCount + ")",
                 sb.substring(i * 2048, Math.min(((i + 1) * 2048), sb.length())));
                
                sender.reply(embed);
            }
          
            
        } else {
            final CryptoCurrency cryptoCurrency = DiscordCrypto.COIN_API.getCrypto(assetId);
            final Asset asset = DiscordCrypto.COIN_API.getAsset(cryptoCurrency.getAssetId());
            final DecimalFormat df = new DecimalFormat("#.####");
    
            df.setRoundingMode(RoundingMode.DOWN);
    
            final MessageEmbed embed = MessageEmbedHelper.getBuilder()
                .setTitle("Valeur du " + cryptoCurrency.getName())
                .addField(asset.getAssetId() +" / USD", df.format(cryptoCurrency.getPriceUSD()) + "$", true)
                .setThumbnail(asset.getAssetUrl())
                .build();
            
            sender.reply(embed);
        }

    }

    @Override
    public boolean isOpRestricted() {
        return false;
    }
    
}
