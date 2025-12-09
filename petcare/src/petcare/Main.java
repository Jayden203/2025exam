package petcare;

public class Main {
    public static void main(String[] args) {
        PetService service = new PetService();

        Enclosure e1 = new Enclosure(AnimalSize.SMALL, 20, 50);
        Enclosure e2 = new Enclosure(AnimalSize.LARGE, 25, 30);
        service.addEnclosure(e1);
        service.addEnclosure(e2);

        Animal cat = new Animal("Mimi", AnimalSize.SMALL, 18, 26);

        System.out.println("Allocate cat: " + service.allocateAnimal(cat));
        service.printAllEnclosures();

        service.removeAnimal(cat);
        service.printAllEnclosures();
    }
}

