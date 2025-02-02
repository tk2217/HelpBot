package com.diamondfire.helpbot.bot.command.impl.other;

import com.diamondfire.helpbot.bot.command.argument.ArgumentSet;
import com.diamondfire.helpbot.bot.command.help.*;
import com.diamondfire.helpbot.bot.command.impl.Command;
import com.diamondfire.helpbot.bot.command.permissions.Permission;
import com.diamondfire.helpbot.bot.command.reply.PresetBuilder;
import com.diamondfire.helpbot.bot.command.reply.feature.informative.*;
import com.diamondfire.helpbot.bot.events.command.*;
import com.diamondfire.helpbot.util.WebUtil;
import com.google.gson.*;
import net.dv8tion.jda.api.EmbedBuilder;

import java.io.IOException;
import java.util.*;


public class StoreCommand extends Command {
    
    @Override
    public String getName() {
        return "store";
    }
    
    @Override
    public String[] getAliases() {
        return new String[]{"shop"};
    }
    
    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Shows all items, recent purchases and sales (if there is one) from store.mcdiamondfire.com.")
                .category(CommandCategory.OTHER);
    }
    
    @Override
    protected ArgumentSet compileArguments() {
        return new ArgumentSet();
    }
    
    @Override
    public Permission getPermission() {
        return Permission.USER;
    }
    
    @Override
    public void run(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("<:diamondfire:867472383098486794> DiamondFire Store <:diamondfire:867472383098486794>", "https://store.mcdiamondfire.com/");
        builder.setDescription("Fetching store..");
        if (event instanceof MessageCommandEvent) {
            event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> {
                try {
                    message.editMessageEmbeds(createStoreEmbed().build()).queue();
                } catch (IOException e) {
                    message.editMessageEmbeds(new PresetBuilder().withPreset(new InformativeReply(InformativeReplyType.ERROR, "Failed to retrieve store items.")).getEmbed().build()).queue();
                }
            });
        } else if (event instanceof ApplicationCommandEvent applicationCommandEvent) {
            applicationCommandEvent.getInternalEvent().deferReply().queue(interactionHook -> {
                try {
                    interactionHook.editOriginalEmbeds(createStoreEmbed().build()).queue();
                } catch (IOException e) {
                    interactionHook.editOriginalEmbeds(new PresetBuilder().withPreset(new InformativeReply(InformativeReplyType.ERROR, "Failed to retrieve store items.")).getEmbed().build()).queue();
                }
            });
        }
    }
    
    private EmbedBuilder createStoreEmbed() throws IOException {
        EmbedBuilder builder = new EmbedBuilder();
        
        JsonObject json = WebUtil.getJson("https://df.vatten.dev/store/").getAsJsonObject();
        builder.setTitle("<:diamondfire:867472383098486794> DiamondFire Store <:diamondfire:867472383098486794>", "https://store.mcdiamondfire.com/");
        boolean sale = json.has("sale");
        if(sale) builder.setDescription("\n<a:boostx3:809172442496630815> **SALE! " + (int) (json.get("sale").getAsFloat() * 100) + "% OFF!** <a:boostx3:809172442496630815>\n");
    
        //lazy reorganizing
        JsonObject items = new JsonObject();
        for(String category : new String[]{"Ranks", "Plots", "Boosters"}) {
            items.add(category, json.getAsJsonObject("items").getAsJsonArray(category.toLowerCase()));
        }
    
        HashMap<String, String> emotes = new HashMap<>();
        emotes.put("Ranks", "<a:overlord:867780069653348402>");
        emotes.put("Plots", "<:location:789274944923369483>");
        emotes.put("Boosters", "<:potion:789274945162575912>");
    
        for(Map.Entry<String, JsonElement> category : items.entrySet()) {
            ArrayList<String> fieldval = new ArrayList<>();
            for(JsonElement item : category.getValue().getAsJsonArray()) {
                String name = item.getAsJsonObject().get("name").getAsString();
                String original_price = item.getAsJsonObject().get("original-price").getAsString();
                String discount_price = sale ? item.getAsJsonObject().get("discount-price").getAsString() : null;
                fieldval.add("**" + name + "**: " + (sale ? "~~$" + original_price + "~~ <a:boostx2:809172442957217812> $" + discount_price : "$" + original_price));
            }
            builder.addField(emotes.get(category.getKey()) + " " + category.getKey(), String.join("\n", fieldval) + "\n\u200b", false);
        }
    
        ArrayList<String> fieldval = new ArrayList<>();
        for(JsonElement purchase : json.getAsJsonArray("recent-purchases")) {
            fieldval.add(" **" + purchase.getAsJsonObject().get("player").getAsString() + "** bought **" + purchase.getAsJsonObject().get("item").getAsString() + "**");
        }
        builder.addField(":partying_face: Recent Purchases", String.join("\n", fieldval), false);
        
        return builder;
    }
}
