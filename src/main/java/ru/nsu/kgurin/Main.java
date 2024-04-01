package ru.nsu.kgurin;

import framework.Deserializer;
import framework.JsonReader;
import framework.JsonWriter;
import framework.Serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        Planet earth = new Planet("Earth");
        Dog dog = new Dog(earth, "Dog", "Thea");

        Person person1 = new Person(earth, "Human", "person1", dog, null, "F", null);
        Person person2 = new Person(earth, "Human", "person2", dog, null, "F", null);
        Person person3 = new Person(earth, "Human", "person3", dog, null, "F", null);


        Person[] data = {person1, person2, person3};

        person1.setData(data);
        person2.setData(data);
        person3.setData(data);

        Person kirill = new Person(earth, "Human");
        Person altan = new Person(earth, "Human", "Altan", dog, kirill, "F", data);
        kirill.setName("Kirill");
        kirill.setDog(dog);
        kirill.setSpouse(altan);
        kirill.setGender("M");
        kirill.setData(data);

        ArrayList<Person> people = new ArrayList<>(Arrays.asList(person1, person2, person3));
        ArrayList<HashMap<String, Object>> maps = Serializer.serializeList(people);
        JsonWriter.writeObjects("./listObjects.json", maps);
        ArrayList<HashMap<String, Object>> maps2 = JsonReader.readObjects("./listObjects.json");
        ArrayList<Object> people1 = Deserializer.deserializeList(Person.class, maps);

        HashMap<String, Object> map = Serializer.serialize(kirill);
        JsonWriter.writeObject("./output.json", map);
        HashMap<String, Object> person = JsonReader.readObject("./output.json");
        Person newKirill = (Person) Deserializer.deserialize(Person.class, person);

        System.out.println(newKirill);
    }
}