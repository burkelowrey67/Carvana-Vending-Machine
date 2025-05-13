import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class Application {

    private static VendingMachine vendingMachine;
    private static Scanner userInputScanner;
    private static boolean running;

    public static void main(String args[]) {

        int command;
        userInputScanner = new Scanner(System.in);
        running = true;

        setup();

        while (running) {
            displayMenu();
            command = getCommand();
            runCommand(command);
        }
    }

    private static void displayMenu() {

        System.out.println();
        System.out.println("=== Car Vending Machine Menu ===");
        System.out.println("1. Load Car Data from File");
        System.out.println("2. Display Vending Machine");
        System.out.println("3. Retrieve a Car by Location (Floor, Space)");
        System.out.println("4. Print Sorted Inventory (Price)");
        System.out.println("5. Print Sorted Inventory (Year)");
        System.out.println("6. Search for Cars (Manufacturer & Type)");
        System.out.println("7. Add Car to Wash Queue");
        System.out.println("8. Process Car Wash Queue");
        System.out.println("9. Sell Car");
        System.out.println("10. Exit");
        System.out.println();
    }

    // This method sets up the vending machine.
    private static void setup() {

        int floors;
        int spaces;

        System.out.println("=== Car Vending Machine Setup ===");

        System.out.print("Enter Number of Floors > ");
        floors = userInputScanner.nextInt();

        System.out.print("Enter Number of Spaces > ");
        spaces = userInputScanner.nextInt();

        vendingMachine = new VendingMachine(floors, spaces);
    }

    // This method loads a file from a local directory and stores the cars into a 2D-Array.
    private static void loadCarData(VendingMachine vendingMachine, String filePath) {
    
        int floor;
        int space;
        int year;
        double price;
        String make;
        String model;
        String type;

        File userFile;
        Scanner scanner = null;

        try {
                    
            userFile = new File(filePath);
            scanner = new Scanner(userFile);

            while (scanner.hasNextLine()) {

                type = scanner.next(); 
                floor = scanner.nextInt();
                space = scanner.nextInt();
                year = scanner.nextInt();
                price = scanner.nextDouble();
                make = scanner.next();
                model = scanner.next();

                if (type.equalsIgnoreCase("p")) {
                    vendingMachine.addCar(new PremiumCar(year, price, make, model), floor, space);
                }
                
                else if (type.equalsIgnoreCase("b")) {
                    vendingMachine.addCar(new BasicCar(year, price, make, model), floor, space);
                }
            }
        }

        // If the file is not found, the method throws an exception.
        catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }   

    // This method gets a command from the user.
    private static int getCommand() {
        int command = -1;
        while (!isValidCommand(command)) {
            System.out.print("> ");

            try {
                command = userInputScanner.nextInt();

                if (!isValidCommand(command)) {
                    throw new InputMismatchException();
                }
            }

            catch (InputMismatchException e) {
                System.out.println("Error: enter a valid command");
            }


            finally {
                userInputScanner.nextLine();
            }
        }
            
        return command;
    }

    // This method returns true if the command is between 0 and 10.
    private static boolean isValidCommand(int command) {
        return command > 0 && command <= 10;
    }

    // This method runs a command.
    private static void runCommand(int command) {

        if (command == 1) {
            String filePath = getFilePath();
            loadCarData(vendingMachine, filePath);
        }

        else if (command == 2) {
            vendingMachine.printInventory();
        }

        else if (command == 3) {
            int[] pos = getPos();
            vendingMachine.testDrive(pos[0], pos[1]);
        }

        else if (command == 4) {
            vendingMachine.printSortedInventory("Price");
        }

        else if (command == 5) {
            vendingMachine.printSortedInventory("Year");
        }

        else if (command == 6) {
            String manufacturer = getManufacuturer();
            String type = getType();
            vendingMachine.printCarsByMakeType(manufacturer, type);
        }

        else if (command == 7) {
            int[] pos = getPos();
            vendingMachine.addCarToWash(pos[0], pos[1]);
        }

        else if (command == 8) {
            vendingMachine.processCarWashQueue();
        }

        else if (command == 9) {
            int[] pos = getPos();
            vendingMachine.sellCar(pos[0], pos[1]);
        }

        else if (command == 10) {
            userInputScanner.close();
            running = false;
        }

        else {
            System.out.print("Enter a Valid Command >");
        }
    }

    // This method gets a position in the car vending machine from the user.
    private static int[] getPos() {
        int floor;
        int space;

        System.out.print("Enter Floor > ");
        floor = userInputScanner.nextInt();
  

        System.out.print("Enter Space > ");
        space = userInputScanner.nextInt();

        return new int[] { floor, space };
    }

    // This method gets the file path from the user.
    private static String getFilePath() {
        String filePath;

        System.out.println("Enter File Path > ");
        filePath = userInputScanner.nextLine();

        return filePath;
    }

    private static String getManufacuturer() {
        String manufacturer;

        System.out.print("Enter Manufacturer > ");
        manufacturer = userInputScanner.nextLine();

        return manufacturer;
    }

    private static String getType() {
        String type;

        System.out.print("Enter Type (B) basic, (P) premium > ");
        type = userInputScanner.nextLine();

        return type;
    }
}





class VendingMachine {
    
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

    // This method sells a car given a location and prints a sold message.
    public void sellCar(int floor, int space) {

        Car car = getCar(floor, space);

        if (car != null) {
            removeCar(floor, space);
            System.out.println("Car Sold: " + car.toString());
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

    // This method prints the cars in the inventory that match the make and type. It prints
    // a message if a car is not found.
    public void printCarsByMakeType(String make, String type) {

        List<Car> matchedCars = new ArrayList<>();

        for (Car car : carsByManufacturer.get("make")) {
            if ((type.equals("P") && car instanceof PremiumCar) || (type.equals("B") && car instanceof BasicCar)) {
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





abstract class Car {
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
    
    // This method returns a string of information regarding the car.
    public abstract String toString();
}





class BasicCar extends Car {
    
    public BasicCar(int year, double price, String make, String model) {
        super(year, price, make, model);
    }
    
    @Override
    public String toString() {
        return "Premium Car: " + make + " " + model + " " + year + " - $" + price + "0";
    }
}





class PremiumCar extends Car{
    
    public PremiumCar(int year, double price, String make, String model) {
        super(year, price, make, model);
    }
    
    @Override
    public String toString() {
        return "Premium Car: "  + year + make + " " + model + " " + " - $" + price + "0";
    }
}