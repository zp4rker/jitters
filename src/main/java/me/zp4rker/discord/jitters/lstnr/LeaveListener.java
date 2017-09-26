package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.core.logger.ZLogger;
import me.zp4rker.discord.jitters.Jitters;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

/**
 * @author ZP4RKER
 */
public class LeaveListener {

    @SubscribeEvent
    public void onLeave(GuildMemberLeaveEvent event) {
        TextChannel channel = Jitters.jda.getTextChannelById("312571375598698507");
        Member member = event.getMember();

        ZLogger.debug("Searching for join message of " + member.getEffectiveName() + "...");
        channel.getHistory().getRetrievedHistory().forEach(message -> {
            if (message.getMentionedUsers().size() < 1) return;
            if (!message.getMentionedUsers().get(0).getId().equals(member.getUser().getId())) return;
            if (!message.getAuthor().getId().equals(member.getJDA().getSelfUser().getId())) return;
            ZLogger.debug("Found message!");

            message.delete().queue();
        });
    }

}