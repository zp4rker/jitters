package me.zp4rker.discord.jitters.lstnr;

import me.zp4rker.discord.jitters.Jitters;
import me.zp4rker.discord.jitters.cmd.InfoCommand;
import me.zp4rker.discord.jitters.cmd.MuteCommand;
import me.zp4rker.discord.jitters.cmd.SAssignCommand;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

/**
 * @author ZP4RKER
 */
public class ReadyListener {

    @SubscribeEvent
    public void onReady(ReadyEvent event) {
        Jitters.handler.registerCommand(new InfoCommand());
        Jitters.handler.registerCommand(new SAssignCommand());
        Jitters.handler.registerCommand(new MuteCommand());

        Jitters.jda.getTextChannelById("314654582183821312")
                .sendMessage("Jitters " + Jitters.VERSION + " started successfully!").queue();
    }

}