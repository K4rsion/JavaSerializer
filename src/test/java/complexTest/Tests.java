package complexTest;

import framework.Deserializer;
import framework.JsonFilter;
import framework.JsonReader;
import framework.JsonWriter;
import framework.Operator;
import framework.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Tests {

    @Test
    void serializeTest() {
        Pet cat = new Pet(null, "Cat", "Vasya");
        Person ivan = new Person(new Planet("Earth"), "Human", "Ivan", 21, "M", null, cat, null, null);
        HashMap<String, Object> ivanSerialized = Serializer.serialize(ivan);


        Assertions.assertEquals(((HashMap<String, Object>) ivanSerialized.get("planet")).get("planet"), ivan.getPlanet().getPlanet());
        Assertions.assertEquals(ivanSerialized.get("type"), ivan.getType());
        Assertions.assertEquals(ivanSerialized.get("name"), ivan.getName());
        Assertions.assertEquals(ivanSerialized.get("age"), ivan.getAge());
        Assertions.assertEquals(ivanSerialized.get("spouse"), ivan.getSpouse());
        Assertions.assertEquals(((HashMap<String, Object>) ivanSerialized.get("pet")).get("name"), ivan.getPet().getName());
        Assertions.assertEquals(((HashMap<String, Object>) ivanSerialized.get("pet")).get("planet"), ivan.getPet().getPlanet());
        Assertions.assertEquals(((HashMap<String, Object>) ivanSerialized.get("pet")).get("type"), ivan.getPet().getType());
        Assertions.assertEquals(ivanSerialized.get("friends"), ivan.getFriends());
        Assertions.assertEquals(ivanSerialized.get("readBooks"), ivan.getReadBooks());
    }

    @Test
    void deserializeTest() {
        Pet cat = new Pet(null, "Cat", "Vasya");
        Person ivan = new Person(new Planet("Earth"), "Human", "Ivan", 21, "M", null, cat, null, null);
        HashMap<String, Object> ivanSerialized = Serializer.serialize(ivan);
        Person ivanDeserialized = (Person)Deserializer.deserialize(Person.class, ivanSerialized);

        Assertions.assertEquals(ivanDeserialized.getPlanet().getPlanet(), ivan.getPlanet().getPlanet());
        Assertions.assertEquals(ivanDeserialized.getType(), ivan.getType());
        Assertions.assertEquals(ivanDeserialized.getName(), ivan.getName());
        Assertions.assertEquals(ivanDeserialized.getAge(), ivan.getAge());
        Assertions.assertEquals(ivanDeserialized.getSpouse(), ivan.getSpouse());
        Assertions.assertEquals(ivanDeserialized.getPet().getName(), ivan.getPet().getName());
        Assertions.assertEquals(ivanDeserialized.getPet().getPlanet(), ivan.getPet().getPlanet());
        Assertions.assertEquals(ivanDeserialized.getPet().getType(), ivan.getPet().getType());
        Assertions.assertEquals(ivanDeserialized.getFriends(), ivan.getFriends());
        Assertions.assertEquals(ivanDeserialized.getReadBooks(), ivan.getReadBooks());
    }

    @Test
    void readWriteJsonTest() {
        Pet cat = new Pet(null, "Cat", "Vasya");
        Person ivan = new Person(new Planet("Earth"), "Human", "Ivan", 21, "M", null, cat, null, null);
        HashMap<String, Object> ivanSerialized = Serializer.serialize(ivan);

        JsonWriter.writeObject("./ivanSerialized.json", ivanSerialized);
        HashMap<String, Object> ivanRead = JsonReader.readObject("./ivanSerialized.json");

        Person ivanDeserialized = (Person)Deserializer.deserialize(Person.class, ivanRead);

        Assertions.assertEquals(ivanDeserialized.getPlanet().getPlanet(), ivan.getPlanet().getPlanet());
        Assertions.assertEquals(ivanDeserialized.getType(), ivan.getType());
        Assertions.assertEquals(ivanDeserialized.getName(), ivan.getName());
        Assertions.assertEquals(ivanDeserialized.getAge(), ivan.getAge());
        Assertions.assertEquals(ivanDeserialized.getSpouse(), ivan.getSpouse());
        Assertions.assertEquals(ivanDeserialized.getPet().getName(), ivan.getPet().getName());
        Assertions.assertEquals(ivanDeserialized.getPet().getPlanet(), ivan.getPet().getPlanet());
        Assertions.assertEquals(ivanDeserialized.getPet().getType(), ivan.getPet().getType());
        Assertions.assertEquals(ivanDeserialized.getFriends(), ivan.getFriends());
        Assertions.assertEquals(ivanDeserialized.getReadBooks(), ivan.getReadBooks());


        Person person1 = new Person(null, null, "person1", 10, null, null, null, null, null);
        Person person2 = new Person(null, null, "person2", 20, null, null, null, null, null);
        Person person3 = new Person(null, null, "person3", 30, null, null, null, null, null);

        ArrayList<Person> people = new ArrayList<>();
        people.add(person1);
        people.add(person2);
        people.add(person3);
        ArrayList<HashMap<String, Object>> peopleSerialized = Serializer.serializeList(people) ;
        JsonWriter.writeObjects("./peopleSerialized", peopleSerialized);
        ArrayList<HashMap<String, Object>> peopleRead = JsonReader.readObjects("./peopleSerialized");
        ArrayList<Object> peopleDeserialized = Deserializer.deserializeList(Person.class, peopleRead);

        for(int i = 0; i < people.size(); i++) {
            Person personCast = (Person)peopleDeserialized.get(i);
            Assertions.assertEquals(personCast.getName(), people.get(i).getName());
            Assertions.assertEquals(personCast.getAge(), people.get(i).getAge());
        }
    }

    @Test
    void filterTest() {
        Planet earth = new Planet("Earth");
        Pet cat = new Pet(null, "Cat", "Vasya");
        Pet dog = new Pet(null, "Dog", "Bobik");
        Person person1 = new Person(earth, "Human", "person1", 10, "M", null, cat, null, null);
        Person person2 = new Person(earth, "Human", "person2", 20, "F", null, dog, null, null);
        Person person3 = new Person(earth, "Human", "person3", 30, "M", null, null, null, null);

        ArrayList<Person> people = new ArrayList<>();
        people.add(person1);
        people.add(person2);
        people.add(person3);
        ArrayList<HashMap<String, Object>> peopleSerialized = Serializer.serializeList(people) ;
        JsonWriter.writeObjects("./peopleSerialized", peopleSerialized);
        ArrayList<HashMap<String, Object>> peopleFiltered = JsonFilter.filter("./peopleSerialized", "name", Operator.EQUALS, "person2");
        ArrayList<Object> peopleDeserialized = Deserializer.deserializeList(Person.class, peopleFiltered);

        Assertions.assertTrue(peopleDeserialized.size() == 1);

        Person filteredPerson2 = (Person) peopleDeserialized.get(0);
        Assertions.assertEquals(filteredPerson2.getPlanet().getPlanet(), person2.getPlanet().getPlanet());
        Assertions.assertEquals(filteredPerson2.getType(), person2.getType());
        Assertions.assertEquals(filteredPerson2.getName(), person2.getName());
        Assertions.assertEquals(filteredPerson2.getAge(), person2.getAge());
        Assertions.assertEquals(filteredPerson2.getSpouse(), person2.getSpouse());
        Assertions.assertEquals(filteredPerson2.getPet().getType(), person2.getPet().getType());
        Assertions.assertEquals(filteredPerson2.getPet().getPlanet(), person2.getPet().getPlanet());
        Assertions.assertEquals(filteredPerson2.getPet().getName(), person2.getPet().getName());
        Assertions.assertEquals(filteredPerson2.getFriends(), person2.getFriends());
        Assertions.assertEquals(filteredPerson2.getReadBooks(), person2.getReadBooks());

    }

    @Test
    void arrayOfObjectsTest() {
        Pet cat = new Pet(null, "Cat", "Vasya");
        Person ivan = new Person(new Planet("Earth"), "Human", "Ivan", 21, "M", null, cat, null, null);

        Person person1 = new Person(null, null, "person1", 10, null, null, null, null, null);
        Person person2 = new Person(null, null, "person2", 20, null, null, null, null, null);
        Person person3 = new Person(null, null, "person3", 30, null, null, null, null, null);

        ivan.setFriends(new Person[]{person1, person2, person3});

        HashMap<String, Object> ivanSerialized = Serializer.serialize(ivan);
        Person ivanDeserialized = (Person) Deserializer.deserialize(Person.class, ivanSerialized);

        Assertions.assertEquals(ivan.getFriends().length, ivanDeserialized.getFriends().length);
    }

    @Test
    void cyclicTest() {
        Person ivan = new Person(null, "Human", "Ivan", 21, "M", null, null, null, null);
        Person katya = new Person(null, "Human", "Katya", 21, "F", ivan, null, null, null);
        ivan.setSpouse(katya);

        HashMap<String, Object> ivanSerialized = Serializer.serialize(ivan);
        Person ivanDeserialized = (Person) Deserializer.deserialize(Person.class, ivanSerialized);

        Assertions.assertEquals(ivanDeserialized.getSpouse().getName(), ivan.getSpouse().getName());
        Assertions.assertEquals(ivanDeserialized.getSpouse().getAge(), ivan.getSpouse().getAge());
        Assertions.assertEquals(ivanDeserialized.getSpouse().getType(), ivan.getSpouse().getType());
        Assertions.assertEquals(ivanDeserialized.getSpouse().getPlanet(), ivan.getSpouse().getPlanet());
    }

    @Test
    void emptyObjectTest() {
        Person empty = new Person(null, null, null, 0, null, null, null, null, null);
        HashMap<String, Object> emptySerialized = Serializer.serialize(empty);
        Person emptyDeserialized = (Person) Deserializer.deserialize(Person.class, emptySerialized);

        Assertions.assertEquals(empty.getAge(), emptyDeserialized.getAge());
        Assertions.assertEquals(empty.getPlanet(), emptyDeserialized.getPlanet());
        Assertions.assertEquals(empty.getName(), emptyDeserialized.getName());
        Assertions.assertEquals(empty.getSpouse(), emptyDeserialized.getSpouse());
        Assertions.assertEquals(empty.getPet(), emptyDeserialized.getPet());
        Assertions.assertEquals(empty.getFriends(), emptyDeserialized.getFriends());
        Assertions.assertEquals(empty.getReadBooks(), emptyDeserialized.getReadBooks());
    }
}