package me.lisacek.notbadcore;

import me.lisacek.notbadcore.filters.LogFilter;

public class NotBadCore {

    private static final NotBadCore INSTANCE = new NotBadCore();

    private NotBadCore() {
    }

    public void hook() {
        LogFilter.registerFilter();
    }

    public static NotBadCore getInstance() {
        return INSTANCE;
    }

}
