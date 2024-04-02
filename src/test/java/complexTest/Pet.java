package complexTest;

public class Pet extends Animal{
    String name;

    public Pet(Planet planet, String type, String name) {
        super(planet, type);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
