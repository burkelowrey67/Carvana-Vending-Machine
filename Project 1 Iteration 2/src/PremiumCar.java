public class PremiumCar extends Car{
    
    public PremiumCar(int year, double price, String make, String model) {
        super(year, price, make, model);
    }
    
    @Override
    public String toString() {
        return "Premium Car: "  + year + make + " " + model + " " + " - $" + price + "0";
    }
}
