package petcare;

import java.util.Objects;

public class Animal {

    private String name;
    private AnimalSize size;
    private int comfortTempLower;
    private int comfortTempUpper;

    public Animal(String name, AnimalSize size, int comfortTempLower, int comfortTempUpper) {
        setName(name);
        setSize(size);
        setComfortTempRange(comfortTempLower, comfortTempUpper);
    }

    public String getName() {
        return name;
    }

    public AnimalSize getSize() {
        return size;
    }

    public int getComfortTempLower() {
        return comfortTempLower;
    }

    public int getComfortTempUpper() {
        return comfortTempUpper;
    }

    public void setName(String name) {
        if (name == null || name.trim().length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters.");
        }
        this.name = name.trim();
    }

    public void setSize(AnimalSize size) {
        if (size == null) {
            throw new IllegalArgumentException("Size cannot be null.");
        }
        this.size = size;
    }

    public void setComfortTempRange(int lower, int upper) {
        if (lower < 0 || lower > 50 || upper < 0 || upper > 50) {
            throw new IllegalArgumentException("Temperature bounds must be between 0 and 50.");
        }
        if (lower > upper) {
            throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
        }
        this.comfortTempLower = lower;
        this.comfortTempUpper = upper;
    }

    public void setComfortTempLower(int lower) {
        setComfortTempRange(lower, this.comfortTempUpper);
    }

    public void setComfortTempUpper(int upper) {
        setComfortTempRange(this.comfortTempLower, upper);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", comfortTempLower=" + comfortTempLower +
                ", comfortTempUpper=" + comfortTempUpper +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;
        Animal animal = (Animal) o;
        return Objects.equals(name, animal.name) && size == animal.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size);
    }
}
