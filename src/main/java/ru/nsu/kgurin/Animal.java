package ru.nsu.kgurin;

import framework.ConstructorParam;

public class Animal extends Being{
    private String species;

    public Animal(Planet p){
        super(p);
    }

    public Animal(@ConstructorParam Planet p, @ConstructorParam String s) {
        super(p);
        species = s;
    }
}
