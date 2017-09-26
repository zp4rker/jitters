package me.zp4rker.discord.jitters.cmd;

import me.zp4rker.discord.core.command.ICommand;
import me.zp4rker.discord.core.command.RegisterCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import java.time.Instant;
import java.util.Arrays;

/**
 * @author ZP4RKER
 */
public class MuteCommand implements ICommand {

    @RegisterCommand(aliases = "mute", showInHelp = false)
    public void onMuteCommand(Message message, String[] args) {
        message.getTextChannel()
                .deleteMessages(Arrays.asList(message, message.getChannel().sendMessage("`").complete())).queue();
        if (!isStaff(message.getMember())) return;
        if (message.getMentionedUsers().size() != 1) return;
        if (args.length < 1) return;

        Member member = message.getGuild().getMember(message.getMentionedUsers().get(0));

        if (!mute(member)) return;
        muteLog(message, true);
    }

    @RegisterCommand(aliases = "unmute", showInHelp = false)
    public void onUnmuteCommand(Message message, String[] args) {
        message.getTextChannel()
                .deleteMessages(Arrays.asList(message, message.getChannel().sendMessage("`").complete())).queue();
        if (!isStaff(message.getMember())) return;
        if (message.getMentionedUsers().size() != 1) return;
        if (args.length < 1) return;

        Member member = message.getGuild().getMember(message.getMentionedUsers().get(0));

        if (!unmute(member)) return;
        muteLog(message, false);
    }

    private boolean mute(Member member) {
        try {
            Role role = null;

            if (member.getGuild().getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("muted"))) {
                role = member.getGuild().getRolesByName("Muted", false).get(0);
            } else {
                role = member.getGuild().getController().createRole()
                        .setName("Muted").setMentionable(false).setHoisted(false).submit().get();
            }

            if (role == null) return false;

            for (TextChannel c : member.getGuild().getTextChannels()) {
                c.createPermissionOverride(role).complete();
                c.getPermissionOverride(role).getManager()
                        .deny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ATTACH_FILES).queue();
            }

            member.getGuild().getController().addSingleRoleToMember(member, role).queue();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean unmute(Member member) {
        if (!alreadyMuted(member)) return false;
        try {
            member.getGuild().getController().removeSingleRoleFromMember(member,
                    member.getGuild().getRolesByName("Muted", false).get(0)).queue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isStaff(Member member) {
        return member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("staff"));
    }

    private boolean alreadyMuted(Member member) {
        return member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("muted"));
    }

    private void muteLog(Message message, boolean muted) {
        TextChannel channel = message.getGuild().getTextChannelById(314654582183821312L);
        User toMute = message.getMentionedUsers().get(0);
        User author = message.getAuthor();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setThumbnail(toMute.getEffectiveAvatarUrl())
                .setFooter("Muted by " + author.getName() + "#" + author.getDiscriminator(), null)
                .setTimestamp(Instant.now());
        if (muted) embedBuilder.setTitle("**" + toMute.getName() + "#" + toMute.getDiscriminator() + "** has been muted!");
        else embedBuilder.setTitle("**" + toMute.getName() + "#" + toMute.getDiscriminator() + "** has been unmuted!");

        MessageEmbed embed = embedBuilder.build();
        channel.sendMessage(embed).queue();
        message.getChannel().sendMessage(embed).queue();
    }

}