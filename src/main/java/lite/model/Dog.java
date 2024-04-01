package lite.model;

class Dog extends Animal{
    private String name;

    public Dog(String name,
               String species) {
        super(species);
        this.name = name;
    }
}
