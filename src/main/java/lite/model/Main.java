package lite.model;

import framework.Deserializer;
import framework.JsonReader;
import framework.JsonWriter;
import framework.Serializer;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        Dog dog = new Dog("Bobby", "Dog");
        Person husband = new Person(22, "John", dog, "Human", null);
        Person wife = new Person(21, "Matilda", dog, "Human", husband);
        husband.setSpouse(wife);

        HashMap<String, Object> map =  Serializer.serialize(husband);
        JsonWriter.writeObject("./output.json", map);

        HashMap<String, Object> map2 = JsonReader.readObject("./output.json");

        Person person2 = (Person) Deserializer.deserialize(Person.class, map2);

        System.out.println();
    }
}
