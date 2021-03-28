package fr.blueberry.studio.hermes.plugins.discordCrypto.commands;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import emoji4j.EmojiUtils;
import fr.blueberry.studio.hermes.api.app.Sender;
import fr.blueberry.studio.hermes.api.commands.Command;
import fr.blueberry.studio.hermes.api.utils.MessageEmbedHelper;
import fr.blueberry.studio.hermes.api.utils.StringHelper;
import fr.blueberry.studio.hermes.plugins.discordCrypto.DiscordCrypto;
import fr.blueberry.studio.hermes.plugins.discordCrypto.core.CoinAPI;
import fr.blueberry.studio.hermes.plugins.discordCrypto.core.tokens.Asset;
import fr.blueberry.studio.hermes.plugins.discordCrypto.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class CommandCrypto extends Command {

    @Override
    public String getLabel() {
        return "crypto";
    }

    @Override
    public void execute(Sender sender, String[] args) {
        final DecimalFormat df = CoinAPI.MONEY_VALUE_FORMATTER;

        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("search")) {
                final String searchedAsset = StringHelper.stringify(1, args);
                final ArrayList<Asset> assets = DiscordCrypto.COIN_API.searchAssets(searchedAsset);
                final StringBuilder sb = new StringBuilder();
                
                if(assets.size() > 0) {
                    sb.append(EmojiUtils.getEmoji(":moneybag:").getEmoji() + " " + assets.size() + " cryptomonnaies trouvées. \n\n");
                    int i = 0;
                    for(Asset asset : assets) {
                        i++;
                        sb.append(String.valueOf(i))
                          .append("・")
                          .append(asset.getName())
                          .append(" (")
                          .append(asset.getSymbol())
                          .append(")")
                          .append(",")
                          .append("\n");
                    }
                } else {
                    sb.append(EmojiUtils.getEmoji(":x:").getEmoji() + " Aucune cryptommonaie trouvée pour la recherche.");
                }
                
                final MessageEmbed embed = MessageEmbedHelper.fastEmbed(EmojiUtils.getEmoji(":mag:").getEmoji() + " Résultats de la recherche pour \"" + searchedAsset + "\"", sb.toString());

                sender.reply(embed);
            } else {
                final String searchedAsset = StringHelper.stringify(0, args);
                final Asset asset = DiscordCrypto.COIN_API.retrieveAsset(searchedAsset);
                
                MessageEmbed embed;
                if(asset != null) {
                    embed = MessageEmbedHelper.getBuilder()
                        .setTitle(EmojiUtils.getEmoji(":moneybag:").getEmoji() + " Valeur du " + asset.getName())
                        .setColor(ColorUtils.pickColorFromImage(asset.getAssetUrl()))
                        .addField(asset.getSymbol()+" / USD", df.format(asset.getPriceUsd()) + " USD", true)
                        .setThumbnail(asset.getAssetUrl())
                        .setTimestamp(Instant.now())
                        .build();
                    
                } else {
                    embed = MessageEmbedHelper.fastEmbed("Aucune cryptomonnaie nommée \"" + searchedAsset + "\" trouvée.");
                }
               
                sender.reply(embed);
            }
        } else {
            final ArrayList<Asset> assets = DiscordCrypto.COIN_API.retrieveTop();
            final EmbedBuilder embedBuilder = MessageEmbedHelper.getBuilder()
                    .setTitle(EmojiUtils.getEmoji(":trophy:").getEmoji() + " Top des cryptomonnaies " + EmojiUtils.getEmoji(":moneybag:").getEmoji())
                    .setThumbnail(assets.get(0).getAssetUrl())
                    .setColor(ColorUtils.pickColorFromImage(assets.get(0).getAssetUrl()))
                    .setFooter("Développé par Grimille " + EmojiUtils.getEmoji(":heart:").getEmoji())
                    .addBlankField(false);

            int i = 0;
            String[] medals = {":first_place: ", ":second_place: ", ":third_place: ", "4・", "5・", "6・", "7・","8・","9・"};
            for(Asset asset : assets) {
                embedBuilder.addField(medals[i] + asset.getName(), df.format(asset.getPriceUsd()) + " USD  ", true);
                i++;
            }

            sender.reply(embedBuilder.build());
        }
    }

    @Override
    public boolean isOpRestricted() {
        return false;
    }

    @Override
    public String[] getAliases() {
        return new String[] {"crypt", "cr"};
    }

    @Override
    public String getDescription() {
        return "Consulter le cours des cryptomonnaies sur Discord.";
    }

    @Override
    public String getUsage() {
        return """
                !crypto - Affiche le top des cryptomonnaies.
                !crypto <symbole|nom> - Affiche le cours de la crypto demandée.
                !crypto search <recherche> - Recherche une cryptomonnaie.
               """;
    }
    
}
