package com.diamondfire.helpbot.bot.command.impl.stats.graph;

import com.diamondfire.helpbot.bot.command.argument.ArgumentSet;
import com.diamondfire.helpbot.bot.command.argument.impl.types.*;
import com.diamondfire.helpbot.bot.command.impl.Command;
import com.diamondfire.helpbot.bot.command.permissions.Permission;
import com.diamondfire.helpbot.bot.events.command.CommandEvent;
import com.diamondfire.helpbot.sys.graph.generators.*;

public abstract class AbstractGraphCommand<T> extends Command {
    
    @Override
    public ArgumentSet compileArguments() {
        return new ArgumentSet()
                .addArgument("mode",
                        new DefinedObjectArgument<>(TimeMode.values()))
                .addArgument("amount",
                        new ClampedIntegerArgument(1, 99999999));
    }
    
    @Override
    public Permission getPermission() {
        return Permission.USER;
    }
    
    @Override
    public void run(CommandEvent event) {
        T context = createContext(event);
        
        event.getReplyHandler().replyFile("", getGraphGenerator().createGraph(context), "graph.png");
    }
    
    public abstract GraphGenerator<T> getGraphGenerator();
    
    public abstract T createContext(CommandEvent event);
    
}


