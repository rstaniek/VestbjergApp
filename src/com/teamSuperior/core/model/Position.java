package com.teamSuperior.core.model;

import javax.persistence.*;

/**
 * Position entity
 */
@Entity
@Table(name = "positions")
public class Position implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int accessLevel;
    private String name;

    public Position() {
    }

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

    @Override
    public String toJson() {
        return null;
    }
}
