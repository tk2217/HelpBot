package com.diamondfire.helpbot.bot.command.argument.impl.types;

import com.diamondfire.helpbot.bot.command.argument.impl.parsing.exceptions.*;
import com.diamondfire.helpbot.bot.events.command.CommandEvent;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.utils.MiscUtil;
import org.jetbrains.annotations.NotNull;

import java.util.regex.*;

public class DiscordUserArgument extends AbstractSimpleValueArgument<Long> {
    
    private final Pattern pattern = Pattern.compile("<@!?(\\d+)>");
    
    @Override
    public Long parse(@NotNull String msg, CommandEvent event) throws MalformedArgumentException {
        
        try {
            return Long.parseLong(msg);
        } catch (NumberFormatException ignored) {
        }
        
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            return MiscUtil.parseSnowflake(matcher.group(1));
        }
        throw new MalformedArgumentException("Bad user argument provided, must either be a ping or an id.");
    }
    
    @Override
    public OptionType optionType() {
        return OptionType.USER;
    }
    
    @Override
    public Long parseSlash(OptionMapping optionMapping, CommandEvent event) throws ArgumentException {
        return optionMapping.getAsLong();
    }
}
