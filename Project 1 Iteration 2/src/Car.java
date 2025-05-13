public abstract class Car {
    protected String make;
    protected String model;
    protected int year;
    protected double price;

    public Car(int year, double price, String make, String model) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }
    
    public abstract String toString();
}
