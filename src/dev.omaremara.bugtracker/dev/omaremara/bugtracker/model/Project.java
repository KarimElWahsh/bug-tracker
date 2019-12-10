package dev.omaremara.bugtracker.model;

public class Project {

    public  String name;

    public Project(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
