package model;

public class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void greet() {
        System.out.println("Welcome, " + name + "!");
    }
}
