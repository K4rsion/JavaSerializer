package complexTest;

public class Animal {
    private String type;
    private Planet planet;

    public Animal(Planet planet, String type) {
        this.planet = planet;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Planet getPlanet() {
        return planet;
    }
}
