package me.lisacek.notbadcore;

import lombok.Getter;

public class NotBadCore {

    private static final NotBadCore INSTANCE = new NotBadCore();

    @Getter
    private final String version = "${project.version}";

    public static NotBadCore getInstance() {
        return INSTANCE;
    }

}
