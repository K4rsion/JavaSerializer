package ru.nsu.kgurin;

import framework.ConstructorParam;

public class Being {
    private final String system = "Solar System";
    Planet planet;

    public Being(@ConstructorParam Planet p) {
        planet = p;
    }
}
