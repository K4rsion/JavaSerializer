package lite.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import framework.JsonValue;

class Animal {
    private String species;

    public Animal(String species) {
        this.species = species;
    }
}