package petcare;

public class Enclosure {

    private final AnimalSize size;
    private final int temperature;
    private final int runningCosts;
    private Animal occupant;

    public Enclosure(AnimalSize size, int temperature, int runningCosts) {
        if (size == null) {
            throw new IllegalArgumentException("Enclosure size cannot be null.");
        }
        this.size = size;
        this.temperature = temperature;
        this.runningCosts = runningCosts;
        this.occupant = null;
    }

    public AnimalSize getSize() {
        return size;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getRunningCosts() {
        return runningCosts;
    }

    public Animal getOccupant() {
        return occupant;
    }

    public boolean checkCompatibility(Animal animal) {
        if (animal == null) {
            return false;
        }
        boolean sizeOk = animal.getSize().ordinal() <= this.size.ordinal();
        boolean tempOk = this.temperature >= animal.getComfortTempLower()
                && this.temperature <= animal.getComfortTempUpper();
        return sizeOk && tempOk;
    }

    public void addAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }
        if (this.occupant != null) {
            throw new IllegalArgumentException("Enclosure already has an occupant.");
        }
        if (!checkCompatibility(animal)) {
            throw new IllegalArgumentException("Animal is not compatible with this enclosure.");
        }
        this.occupant = animal;
    }

    public void removeAnimal() {
        this.occupant = null;
    }

    @Override
    public String toString() {
        return "Enclosure{" +
                "size=" + size +
                ", temperature=" + temperature +
                ", runningCosts=" + runningCosts +
                ", occupant=" + occupant +
                '}';
    }
}
