package com.diamondfire.helpbot.bot.command.impl.codeblock;

import com.diamondfire.helpbot.bot.command.help.*;
import com.diamondfire.helpbot.bot.command.permissions.Permission;
import com.diamondfire.helpbot.bot.command.reply.handler.ReplyHandler;
import com.diamondfire.helpbot.bot.events.command.CommandEvent;
import com.diamondfire.helpbot.df.codeinfo.codedatabase.db.datatypes.CodeObject;
import com.diamondfire.helpbot.util.StringUtil;
import com.google.gson.*;
import net.dv8tion.jda.api.entities.TextChannel;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;


public class RawCommand extends AbstractSingleQueryCommand {
    
    private static void sendRawMessage(CodeObject data, ReplyHandler replyHandler) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data.getJson());
        
        replyHandler.replyFile("Data is attached below.", json.getBytes(StandardCharsets.UTF_8), "simpledata.json");
    }
    
    @Override
    public String getName() {
        return "rawcode";
    }
    
    @Override
    public String[] getAliases() {
        return new String[]{"coderaw"};
    }
    
    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Gets raw data of a codeblock/action/game value. (Anything within simpledata class)")
                .category(CommandCategory.CODE_BLOCK)
                .addArgument(
                        new HelpContextArgument()
                                .name("codeblock|action|game value")
                );
    }
    
    @Override
    public Permission getPermission() {
        return Permission.BOT_DEVELOPER;
    }
    
    @Override
    public BiConsumer<CodeObject, ReplyHandler> onDataReceived() {
        return RawCommand::sendRawMessage;
    }
    
}
