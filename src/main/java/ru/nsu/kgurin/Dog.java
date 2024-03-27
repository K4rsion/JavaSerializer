package ru.nsu.kgurin;

import framework.JsonValue;

public class Dog extends Animal{
    String name;

    public Dog(Planet p, String s, String n) {
        super(p, s);
        name = n;
    }
}
