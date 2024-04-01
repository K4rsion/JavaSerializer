package ru.nsu.kgurin;

public class Animal extends Being {
    private String species;

    public Animal(Planet p) {
        super(p);
    }

    public Animal(Planet p, String s) {
        super(p);
        species = s;
    }
}
