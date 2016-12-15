package com.teamSuperior.core.model;

/**
 * Created by Domestos Maximus on 15-Dec-16.
 */
public class Position {

    private int id, accessLevel;
    private String name;

    public Position(int accessLevel, String name) {
        this.accessLevel = accessLevel;
        this.name = name;
        id = 0;
    }

    public Position(int id, int accessLevel, String name) {
        this.id = id;
        this.accessLevel = accessLevel;
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Position: id='" + id + "', name='" + name + "', accessLevel='" + accessLevel + "'\n";
    }
}
