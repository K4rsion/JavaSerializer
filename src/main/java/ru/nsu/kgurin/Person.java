package ru.nsu.kgurin;

import java.util.ArrayList;

public class Person extends Animal {
    private String name;
    private Dog dog;
    private Person spouse;
    public Person[] data;
    private static final String city = "Kyzyl";
    private transient String gender;

    public Person(Planet p, String s) {
        super(p, s);
    }

    public Person(Planet planet,
                  String species,
                  String name,
                  Dog dog,
                  Person spouse,
                  String gender,
                  Person[] data) {
        super(planet, species);
        this.name = name;
        this.dog = dog;
        this.spouse = spouse;
        this.gender = gender;
        this.data = data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setData(Person[] data) {
        this.data = data;
    }
}