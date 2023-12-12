package top.kuoer.parameter;

public enum Role {

    USER("user"),
    SYSTEM("system");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
