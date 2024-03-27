package lite.model;

import framework.Deserializer;
import framework.Deserializer2000;
import framework.JsonWriter;
import framework.Serializer;
import org.objenesis.instantiator.ObjectInstantiator;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        Dog dog = new Dog("Bobby", "Dog");
        Person husband = new Person(22, "John", dog, "Human", null);
        Person wife = new Person(21, "Matilda", dog, "Human", husband);
        husband.setSpouse(wife);

        HashMap<String, Object> map = new HashMap<>();
        Serializer.serialize(husband, map);

        JsonWriter.writeToJson(map);

        Person person2 = (Person) Deserializer2000.deserialize(Person.class, map);
//        Person person2 = (Person) Deserializer.buildObject(Person.class, map);

        System.out.println();

//        ObjectInstantiator
    }
}
