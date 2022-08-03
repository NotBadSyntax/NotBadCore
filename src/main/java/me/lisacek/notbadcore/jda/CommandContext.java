package me.lisacek.notbadcore.jda;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.List;

@RequiredArgsConstructor
public class CommandContext {

    @Getter
    private final GuildMessageReceivedEvent event;
    @Getter
    private final List<String> args;

    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    public TextChannel getChannel() {
        return this.getEvent().getChannel();
    }

    public Message getMessage() {
        return this.getEvent().getMessage();
    }

    public User getAuthor() {
        return this.getEvent().getAuthor();
    }

    public Member getMember() {
        return this.getEvent().getMember();
    }

    public JDA getJDA() {
        return this.getEvent().getJDA();
    }

    public ShardManager getShardManager() {
        return this.getJDA().getShardManager();
    }

    public User getSelfUser() {
        return this.getJDA().getSelfUser();
    }

    public Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }
}
