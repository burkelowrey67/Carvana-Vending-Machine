public class BasicCar extends Car {
    
    public BasicCar(int year, double price, String make, String model) {
        super(year, price, make, model);
    }
    
    @Override
    public String toString() {
        return "Basic Car: " + year + make + " " + model + " " + " - $" + price + "0";
    }
}
