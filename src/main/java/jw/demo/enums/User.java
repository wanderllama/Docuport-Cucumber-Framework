package jw.demo.enums;

import jw.demo.managers.FileReaderManager;

public enum User {
    CLIENT("client"),
    EMPLOYEE("employee"),
    SUPERVISOR("supervisor"),
    ADVISOR("advisor");

    public final String user;
    private final String email = "@gmail.com";
    private final String group;

    User(String user) {
        this.user = user;
        this.group = FileReaderManager.getInstance().getConfigReader().getGroup();
    }

    public String get() {
        return user;
    }

    public String email() {
        return group + '_' + user + email;
    }
}
