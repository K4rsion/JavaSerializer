package ru.nsu.kgurin;

import framework.JsonValue;

import java.util.ArrayList;

public class Person extends Animal {
    private String name;
    private Dog dog;
    private Person spouse;
    public ArrayList<Integer> data;
    private static final String city = "Kyzyl";
    private transient String gender;

    public Person(Planet p, String s) {
        super(p, s);
    }

    public Person(@JsonValue("planet") Planet planet,
                  @JsonValue("species") String species,
                  @JsonValue("name") String name,
                  @JsonValue("dog") Dog dog,
                  @JsonValue("spouse") Person spouse,
                  @JsonValue("gender") String gender,
                  @JsonValue("data") ArrayList<Integer> data) {
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

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }
}