package com.diamondfire.helpbot.bot.command.impl.stats.individualized;

import com.diamondfire.helpbot.bot.command.help.*;
import com.diamondfire.helpbot.bot.command.impl.stats.AbstractPlayerUUIDCommand;
import com.diamondfire.helpbot.bot.command.permissions.Permission;
import com.diamondfire.helpbot.bot.command.reply.PresetBuilder;
import com.diamondfire.helpbot.bot.command.reply.feature.MinecraftUserPreset;
import com.diamondfire.helpbot.bot.command.reply.feature.informative.*;
import com.diamondfire.helpbot.df.creator.CreatorLevel;
import com.diamondfire.helpbot.sys.database.SingleQueryBuilder;
import com.diamondfire.helpbot.bot.events.CommandEvent;
import com.diamondfire.helpbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;


public class CpCommand extends AbstractPlayerUUIDCommand {

    @Override
    public String getName() {
        return "cp";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"cpi", "cpinfo", "mycp", "cplevel"};
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Gets information on a certain player's CP.")
                .category(CommandCategory.STATS)
                .addArgument(
                        new HelpContextArgument()
                                .name("player|uuid")
                                .optional()
                );
    }

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Override
    protected void execute(CommandEvent event, String player) {
        PresetBuilder preset = new PresetBuilder()
                .withPreset(
                        new InformativeReply(InformativeReplyType.INFO, "CP Info", null)
                );
        EmbedBuilder embed = preset.getEmbed();

        new SingleQueryBuilder()
                .query("SELECT * FROM creator_rankings WHERE uuid = ? OR name = ?;", (statement) -> {
                    statement.setString(1, player);
                    statement.setString(2, player);
                })
                .onQuery(table -> {
                    int points = table.getInt("points");
                    int rank = table.getInt("cur_rank");
                    CreatorLevel level = CreatorLevel.getLevel(rank);
                    CreatorLevel nextLevel = CreatorLevel.getNextLevel(CreatorLevel.getLevel(rank));
                    int nextLevelReq = nextLevel.getRequirementProvider().getRequirement();

                    String formattedName = table.getString("name");
                    preset.withPreset(
                            new MinecraftUserPreset(formattedName)
                    );

                    embed.addField("Current Rank", level.display(true), false);
                    embed.addField("Current Points", StringUtil.formatNumber(points), false);
                    new SingleQueryBuilder()
                            .query("SELECT COUNT(*) + 1 AS place FROM creator_rankings WHERE points > ?", (statement) -> statement.setInt(1, points))
                            .onQuery((tableSet) -> embed.addField("Current Leaderboard Place", StringUtil.formatNumber(tableSet.getInt("place")), false)).execute();

                    if (level != CreatorLevel.DIAMOND) {
                        embed.addField("Next Rank", nextLevel.display(true), true);
                        embed.addField("Next Rank Points", StringUtil.formatNumber(nextLevelReq) + String.format(" (%s to go)", StringUtil.formatNumber(nextLevelReq - points)), false);
                    }

                })
                .onNotFound(() -> {
                    embed.clear();
                    preset.withPreset(new InformativeReply(InformativeReplyType.ERROR, "Player was not found."));
                }).execute();
        event.reply(preset);
    }

}


