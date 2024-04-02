package complexTest;

import java.util.ArrayList;

public class Person extends Animal {
    private String name;
    private int age;
    private transient String gender;
    private Person spouse;
    private Pet pet;
    private static final String city = "Novosibirsk";
    public Person[] friends;
    public ArrayList<String> readBooks;


    public Person(Planet planet,
                  String type,
                  String name,
                  int age,
                  String gender,
                  Person spouse,
                  Pet pet,
                  Person[] friends,
                  ArrayList<String> readBooks) {
        super(planet, type);
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.spouse = spouse;
        this.pet = pet;
        this.friends = friends;
        this.readBooks = readBooks;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public void setFriends(Person[] friends) {
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Person getSpouse() {
        return spouse;
    }

    public Pet getPet() {
        return pet;
    }

    public Person[] getFriends() {
        return friends;
    }

    public ArrayList<String> getReadBooks() {
        return readBooks;
    }
}