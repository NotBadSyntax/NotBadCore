package me.lisacek.notbadcore.jda;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public interface ICommand {

    void handle(CommandContext context) throws IOException;

    String getName();

    default List<String> getAliases() {
        return Collections.emptyList();
    }
}
