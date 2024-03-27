package lite.model;

import framework.JsonValue;

class Dog extends Animal{
    private String name;

    public Dog(@JsonValue("name") String name,
               @JsonValue("species") String species) {
        super(species);
        this.name = name;
    }
}
