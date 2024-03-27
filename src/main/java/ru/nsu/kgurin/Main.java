package ru.nsu.kgurin;

import framework.Deserializer2000;
import framework.JsonWriter;
import framework.Serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        Planet earth = new Planet("Earth");
        Dog dog = new Dog(earth, "Dog", "Thea");
        Person kirill = new Person(earth, "Human");
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(1, 2, 3));
        Person altan = new Person(earth, "Human", "Altan", dog, kirill, "F", data);
        kirill.setName("Kirill");
        kirill.setDog(dog);
        kirill.setSpouse(altan);
        kirill.setGender("M");
        kirill.setData(data);

        HashMap<String, Object> map = new HashMap<>();
        Serializer.serialize(kirill, map);

        JsonWriter.writeToJson(map);

        Person kirill2 = (Person) Deserializer2000.deserialize(Person.class, map);

        System.out.println(kirill2);
    }
}