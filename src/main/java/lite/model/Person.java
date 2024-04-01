package lite.model;

class Person extends Animal {
    private int age;
    private String name;
    private Dog dog;
    private Person spouse;

    // objenesis
    public Person(int age,
                  String name,
                  Dog dog,
                  String species,
                  Person spouse
    ) {
        super(species);
        this.age = age;
        this.name = name;
        this.dog = dog;
        this.spouse = spouse;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public Dog getDog() {
        return dog;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }
}