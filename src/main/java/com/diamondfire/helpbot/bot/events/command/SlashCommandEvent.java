package com.diamondfire.helpbot.bot.events.command;

import com.diamondfire.helpbot.bot.command.impl.Command;
import com.diamondfire.helpbot.bot.command.reply.*;
import com.diamondfire.helpbot.bot.command.reply.handler.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

import java.util.Map;

public class SlashCommandEvent implements CommandEvent {
    
    private final net.dv8tion.jda.api.events.interaction.SlashCommandEvent internalEvent;
    private final InteractionReplyHandler interactionReplyHandler;
    private Map<String, ?> argumentMap;
    private Command command;
    
    public SlashCommandEvent(net.dv8tion.jda.api.events.interaction.SlashCommandEvent internalEvent) {
        this.internalEvent = internalEvent;
        this.interactionReplyHandler = new InteractionReplyHandler(internalEvent);
    }
    
    public void putArguments(Map<String, ?> input) {
        argumentMap = input;
    }
    
    @Override
    public Command getCommand() {
        return command;
    }
    
    @Override
    public void setCommand(Command command) {
        this.command = command;
    }
    
    @Override
    public void reply(PresetBuilder preset) {
        interactionReplyHandler.reply(preset);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getArgument(String code) {
        return (T) argumentMap.get(code);
    }
    
    @Override
    public Map<String, ?> getArguments() {
        return argumentMap;
    }
    
    @Override
    public ReplyHandler getReplyHandler() {
        return interactionReplyHandler;
    }
    
    @Override
    public String getAliasUsed() {
        return command.getName();
    }
    
    @Override
    public JDA getJDA() {
        return internalEvent.getJDA();
    }
    
    @Override
    public Guild getGuild() {
        return internalEvent.getGuild();
    }
    
    @Override
    public Member getMember() {
        return internalEvent.getMember();
    }
    
    @Override
    public User getAuthor() {
        return internalEvent.getUser();
    }
    
    @Override
    public TextChannel getChannel() {
        return internalEvent.getTextChannel();
    }
    
    public net.dv8tion.jda.api.events.interaction.SlashCommandEvent getInternalEvent() {
        return internalEvent;
    }
}
