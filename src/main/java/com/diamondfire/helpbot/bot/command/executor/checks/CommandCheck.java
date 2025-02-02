package com.diamondfire.helpbot.bot.command.executor.checks;

import com.diamondfire.helpbot.bot.command.reply.PresetBuilder;
import com.diamondfire.helpbot.bot.events.command.CommandEvent;

// Command checks are run before a command is executed.
public interface CommandCheck {
    
    /**
     * Checks whether a command event should be run.
     * @param event The command event to check.
     * @return Whether the command should be run.
     */
    boolean check(CommandEvent event);
    
    default PresetBuilder buildMessage(CommandEvent event) {
        return null;
    }
}
