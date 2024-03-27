package examples;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

class Animal {
    private String species;

    public Animal(@JsonProperty("species") String species) {
        this.species = species;
    }

    @JsonProperty("species")
    public String getSpecies() {
        return species;
    }
}

class Person extends Animal {
    private int age;
    private String name;
    private Dog dog;

    public Person(@JsonProperty("age") int age,
                  @JsonProperty("name") String name,
                  @JsonProperty("species") String species,
                  @JsonProperty("dog") Dog dog) {
        super(species);
        this.age = age;
        this.name = name;
        this.dog = dog;
    }

    @JsonProperty("age")
    public int getAge() {
        return age;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("dog")
    public Dog getDog() {
        return dog;
    }
}

class Dog extends Animal {
    private String name;

    public Dog(@JsonProperty("name") String name, @JsonProperty("species") String species) {
        super(species);
        this.name = name;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        Dog dog = new Dog("Bobby", "Dog");
        Person person1 = new Person(25, "John", "Human", dog);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(person1);

        System.out.println(json);

        Person person = mapper.readValue(json, Person.class);

        System.out.println("Species: " + person.getSpecies());
        System.out.println("Age: " + person.getAge());
        System.out.println("Name: " + person.getName());
        System.out.println("Dog: " + person.getDog());

        System.out.println(person1.equals(person));
    }
}
