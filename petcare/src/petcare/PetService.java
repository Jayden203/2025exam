package petcare;

import java.util.ArrayList;
import java.util.List;

public class PetService {

    private final List<Enclosure> enclosures;

    public PetService() {
        this.enclosures = new ArrayList<>();
    }

    public void addEnclosure(Enclosure enclosure) {
        if (enclosure == null) {
            throw new IllegalArgumentException("Enclosure cannot be null.");
        }
        enclosures.add(enclosure);
    }

    public void printAllEnclosures() {
        for (Enclosure e : enclosures) {
            System.out.println(e);
        }
    }

    public boolean allocateAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }

        Enclosure best = null;

        for (Enclosure e : enclosures) {
            if (e.getOccupant() != null) {
                continue;
            }
            if (!e.checkCompatibility(animal)) {
                continue;
            }
            if (best == null || e.getRunningCosts() < best.getRunningCosts()) {
                best = e;
            }
        }

        if (best != null) {
            best.addAnimal(animal);
            return true;
        }

        return false;
    }

    public void removeAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }

        for (Enclosure e : enclosures) {
            Animal occ = e.getOccupant();
            if (occ != null && occ.equals(animal)) {
                e.removeAnimal();
                return;
            }
        }

        throw new IllegalArgumentException("Animal not found in any enclosure.");
    }
}

