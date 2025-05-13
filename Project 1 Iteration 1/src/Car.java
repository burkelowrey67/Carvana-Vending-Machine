public class Car {
    private int year;
    private double price;
    private String make;
    private String model;

    public Car(int year, double price, String make, String model) {
        this.year = year;
        this.price = price;
        this.make = make;
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public String toString() {
        return make + " " + model + " " + year + " - $" + price + "0";
    }
}
