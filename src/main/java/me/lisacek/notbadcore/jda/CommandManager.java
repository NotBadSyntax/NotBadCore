package me.lisacek.notbadcore.jda;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    @Getter
    private final List<ICommand> commands = new ArrayList<>();

    private final String prefix;

    public CommandManager(String prefix) {
        this.prefix = prefix;
    }

    private void addCommands(ICommand... commands) {
        for (ICommand command : commands) {
            boolean nameFound = this.commands.stream()
                    .anyMatch(it -> it.getName().equalsIgnoreCase(command.getName()));

            if (nameFound) {
                throw new IllegalArgumentException("A command with this name is already present");
            }

            this.commands.add(command);
        }
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event) throws IOException {

        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}
