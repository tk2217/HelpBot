package com.diamondfire.helpbot.bot.command.impl.codeblock;

import com.diamondfire.helpbot.bot.command.help.*;
import com.diamondfire.helpbot.bot.command.permissions.Permission;
import com.diamondfire.helpbot.bot.command.reply.handler.ReplyHandler;
import com.diamondfire.helpbot.bot.events.command.CommandEvent;
import com.diamondfire.helpbot.df.codeinfo.codedatabase.db.datatypes.*;
import com.diamondfire.helpbot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.function.BiConsumer;


public class TagsCommand extends AbstractSingleQueryCommand {
    
    private static void sendTagMessage(CodeObject data, ReplyHandler replyHandler) {
        EmbedBuilder builder = new EmbedBuilder();
        ActionData actionData;
        
        // Handle codeblocks that have actions associated with them.
        if (data instanceof CodeBlockData && ((CodeBlockData) data).getAssociatedAction() != null) {
            actionData = ((CodeBlockData) data).getAssociatedAction();
        } else if (data instanceof ActionData) {
            actionData = (ActionData) data;
        } else {
            builder.setTitle("Invalid data!");
            builder.setDescription("What you have searched for is not a valid action!");
            replyHandler.reply(builder);
            return;
        }
        
        if (actionData.getTags().length == 0) {
            builder.setTitle("No tags!");
            builder.setDescription("This action does not contain any tags!");
            replyHandler.reply(builder);
            return;
        }
        
        for (CodeBlockTagData tag : actionData.getTags()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String option : tag.getStringOptions()) {
                stringBuilder.append("\n\\> ").append(option.equals(tag.getDefaultValue()) ? String.format("**%s**", option) : option);
            }
            builder.addField(tag.getName(), stringBuilder.toString(), true);
        }
        
        
        builder.setTitle("Tags for: " + data.getName());
        
        String customHead = data.getItem().getHead();
        if (customHead == null) {
            builder.setThumbnail("attachment://item.png");
            replyHandler.replyFile(builder, Util.fetchMinecraftTextureFile(data.getItem().getMaterial().toUpperCase()), "icon.png");
        } else {
            builder.setThumbnail(customHead);
            replyHandler.reply(builder);
        }
    }
    
    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Lists all tags that a certain codeblock has.")
                .category(CommandCategory.CODE_BLOCK)
                .addArgument(
                        new HelpContextArgument()
                                .name("action name")
                );
    }
    
    @Override
    public String getName() {
        return "tags";
    }
    
    @Override
    public String[] getAliases() {
        return new String[]{"codetags"};
    }
    
    @Override
    public Permission getPermission() {
        return Permission.USER;
    }
    
    @Override
    public BiConsumer<CodeObject, ReplyHandler> onDataReceived() {
        return TagsCommand::sendTagMessage;
    }
}
