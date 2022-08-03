package me.lisacek.notbadcore;

import lombok.Getter;

public class NotBadCore {

    private static final NotBadCore INSTANCE = new NotBadCore();

    @Getter
    private final String version = "1.0.1-SNAPSHOT";

    public static NotBadCore getInstance() {
        return INSTANCE;
    }

}
