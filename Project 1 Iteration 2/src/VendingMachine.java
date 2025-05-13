import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class VendingMachine {
    
    public double bankroll;
    
    private int floors;
    private int spaces;

    private List<Car> cars;
    private HashMap<List<Integer>, Car> carsByPos;
    private HashMap<String, List<Car>> carsByManufacturer;
    private Queue<Car> carWashQueue;

    // The constructor that initializes the data structures and data members. It takes two ints
    // that define the size of the vending machine.
    public VendingMachine(int floors, int spaces) {
        
        this.floors = floors;
        this.spaces = spaces;
        cars = new LinkedList<>();
        carsByPos = new HashMap<>();
        carsByManufacturer = new HashMap<>();
        carWashQueue = new LinkedList<>();
    }

    // This method adds a car if the space isn't occupied and within the bounds of the vending machine.
    public void addCar(Car car, int floor, int space) {

        if (isEmptySpace(floor, space) && isValidSpace(floor, space)) {
            cars.add(car);

            List<Integer> pos = asList(floor, space);

            carsByPos.put(pos, car);
            String make = car.getMake();

            if (!hasManufacturer(make)) {
                carsByManufacturer.put(make, new ArrayList<Car>());
            }

            carsByManufacturer.get(make).add(car);
        }
    }

    // This method sells a car given a location.
    public void sellCar(int floor, int space) {

        Car car = getCar(floor, space);

        if (car != null) {
            bankroll += car.getPrice();
            removeCar(floor, space);
        }
    }

    // This method prints a message if a car is found at the given location.
    public void testDrive(int floor, int space) {
        
        Car car = getCar(floor, space);
        
        if (car != null) {
            System.out.println("Car retrieved: " + car.toString());
        }
    }

    // This method prints out the inventory.
    public void printInventory() {
        printCarList(cars);
    }
 
    // This method prints out the inventory sorted by price or by year.
    public void printSortedInventory(String condition) {

        List<Car> sortedCars = getSorted(cars, condition);
        printCarList(sortedCars);
    }

    // This method prints the cars in the inventory that match the make and type.
    public void printCarsByMakeType(String make, String type) {

        List<Car> matchedCars = new ArrayList<>();

        for (Car car : carsByManufacturer.get("make")) {
            if ((type.equals("P") && car instanceof PremiumCar) || 
                (type.equals("B") && car instanceof BasicCar)) {
                matchedCars.add(car);
            }
        }

        if (matchedCars.size() == 0) {
            System.out.println("No Cars Matched the Description.");
        }

        printCarList(matchedCars);
    }

    // This method adds a car to the wash queue given a location.
    public void addCarToWash(int floor, int space) {

        Car car = getCar(floor, space);

        if (car != null) {
            carWashQueue.add(car);
        }
    }

    // This method processes the car wash queue.
    public void processCarWashQueue() {
        
        if (carWashQueue.size() == 0) {
            System.out.println("No cars in the wash queue.");
            return;
        }

        while (carWashQueue.size() > 0) {
            Car car = carWashQueue.poll();
            System.out.println("Washing: " + car.toString());
        }
    }

    // This method returns a car at a given location. It prints an error message if a car is not found and returns null.
    private Car getCar(int floor, int space) {
        if (isEmptySpace(floor, space)) {
            System.out.println("Car not located at (" + floor + ", " + space + ").");
            return null;
        }

        if (!isValidSpace(floor, space)) {
            System.out.println("Invalid space at (" + floor + ", " + space + ").");
            return null;
        }

        return carsByPos.get(asList(floor, space));
    }   

    // This method removes a car in a given space from all data structures.
    private void removeCar(int floor, int space) {

        List<Integer> pos = asList(floor, space);
        Car carToRemove = carsByPos.get(pos);

        cars.remove(carToRemove);
        carsByPos.remove(pos);
        carsByManufacturer.remove(carToRemove.getMake());
    }

    // This method returns a sorted linked list by price or by year or by .
    private List<Car> getSorted(List<Car> cars, String condition) {

        if (condition.equalsIgnoreCase("price")) {
            cars.sort(Comparator.comparingDouble(Car::getPrice));
            return cars;
        }
        
        if (condition.equalsIgnoreCase("year")){
            cars.sort(Comparator.comparingInt(Car::getYear));
            return cars;
        }

        if (condition.equalsIgnoreCase("alphabetical")) {
            cars.sort(Comparator.comparing(Car::getMake).thenComparing(Car::getModel));
            return cars;
        }

        return null;
    }

    // This method prints out a list of cars.
    private void printCarList(List<Car> cars) {
        
        if (cars.size() == 0) {
            return;
        }

        for (Car car : cars) {
            printCar(car);
        }
    }

    // This method prints the information about the car and its position in the vending machine.
    private void printCar(Car car) {
        List<Integer> pos = getCarPos(car);
        System.out.println(car.toString() + " (" + pos.get(0) + ", " + pos.get(1) + ")");
    }

    // This method gets the location of a car.
    private List<Integer> getCarPos(Car car) {

        for (Map.Entry<List<Integer>, Car> entry : carsByPos.entrySet()) {
            if (car.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return null;
    }

    // This method returns true if the space is within the bounds of the vending machine.
    private boolean isValidSpace(int floor, int space) {
        return 0 < floor && floor <= floors && 0 < spaces && space < spaces;
    }

    // This method returns true if the space is not occupied.
    private boolean isEmptySpace(int floor, int space) {
        List<Integer> pos = asList(floor, space);
        return !carsByPos.keySet().contains(pos);
    }

    // This method returns true if the vending machine has at least one car with a given make.
    private boolean hasManufacturer(String make) {
        return carsByManufacturer.keySet().contains(make);
    }

    // This helper method allows us to instantiate and initialize values of an ArrayList in the same line.
    private List<Integer> asList(int x, int y) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(x);
        list.add(y);
        return list;
    }
}

