package com.diamondfire.helpbot.bot.command.executor.checks;

import com.diamondfire.helpbot.bot.command.impl.other.mod.MuteCommand;
import com.diamondfire.helpbot.bot.command.reply.PresetBuilder;
import com.diamondfire.helpbot.bot.events.command.CommandEvent;

public class MutedCheck implements CommandCheck {
    
    @Override
    public boolean check(CommandEvent event) {
        return !event.getMember().getRoles().contains(event.getGuild().getRoleById(MuteCommand.ROLE_ID));
    }
}
