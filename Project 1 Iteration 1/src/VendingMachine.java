import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;

class VendingMachine {
    private Car[][] cars;

    public VendingMachine(int floors, int spaces) {
        this.cars = new Car[floors][spaces];
    }

    public void loadCarData(String filePath) {
        
        int floor;
        int space;
        int year;
        double price;
        String make;
        String model;
        File userFile;
        Scanner scanner = null;

        try {
                    
            userFile = new File(filePath);
            scanner = new Scanner(userFile);

            while (scanner.hasNextLine()) {

                floor = scanner.nextInt();
                space = scanner.nextInt();
                year = scanner.nextInt();
                price = scanner.nextDouble();
                make = scanner.next();
                model = scanner.next();

                if (cars[floor][space] == null) {
                    cars[floor][space] = new Car(year, price, make, model);
                }

                else {
                    throw new Exception();
                }
            }
        }
            
        catch (Exception e) {   
            
            if (e instanceof FileNotFoundException) {
                System.out.println("Error: File not Found.");
            }

            else if (e instanceof ArrayIndexOutOfBoundsException) {
                System.out.println("Error: Attempted to Place Car in an Invalid Space");
            }

            else if (e instanceof InputMismatchException || e instanceof NoSuchElementException) {
                System.out.println("Error: Invalid File Format.");
            }

            else {
                System.out.println("Error: Attempted to Place Car in an Occupied Space");
            }
        }
        
        finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public void displayVendingMachine() {

        boolean isEmpty = true;
        
        for (int floor = 1; floor <= cars.length; floor++) {
            for (int space = 1; space <= cars[floor - 1].length; space++) {
                
                if (cars[floor - 1][space - 1] != null) {
                    isEmpty = false;
                }
            }
        }

        if (isEmpty) {
            System.out.println("Vending Machine is Empty.");
        }

        else {
            for (int floor = 1; floor <= cars.length; floor++) {
                System.out.println("Floor " + floor + ":");
                
                for (int space = 1; space <= cars[floor - 1].length; space++) {
                    System.out.print("  Space" + space + ": ");
                    
                    if (cars[floor - 1][space - 1] != null) {
                        System.out.println(cars[floor - 1][space - 1].toString());
                    }
    
                    else {
                        System.out.println("EMPTY");
                    }
                }
            }
        }
    }

    public void printSortedInventoryPrice() {
        
        ArrayList<Car> cars = new ArrayList<>();
        boolean isEmpty = true;

        for (int floor = 0; floor < this.cars.length; floor++) {
            for (int space = 0; space < this.cars[floor].length; space++) {

                if (this.cars[floor][space] != null) {
                    isEmpty = false;
                    cars.add(this.cars[floor][space]);
                }
            }
        }

        if (isEmpty) {
            System.out.println("Vending Machine is Empty.");
        }

        else {
            if (cars.size() > 2) {

                while (true) {
                    boolean swap = false;

                    for (int i = cars.size() - 1; i > 0; i--) {

                        if (cars.get(i).getPrice() < cars.get(i - 1).getPrice()) {
                            Car temp = cars.get(i);
                            cars.set(i, cars.get(i - 1));
                            cars.set(i - 1, temp);
                            swap = true;
                        }
                    }

                    if (!swap) {
                        break;
                    }
                }
            }

            for (Car car : cars) {
                System.out.println(car.toString());
            }
		}
    }

    public void printSortedInventoryYear() {
                
        ArrayList<Car> cars = new ArrayList<>();
        boolean isEmpty = true;

        for (int floor = 0; floor < this.cars.length; floor++) {
            for (int space = 0; space < this.cars[floor].length; space++) {

                if (this.cars[floor][space] != null) {
                    isEmpty = false;
                    cars.add(this.cars[floor][space]);
                }
            }
        }

        if (isEmpty) {
            System.out.println("Vending Machine is Empty.");
        }

        else {
            if (cars.size() > 2) {

                while (true) {
                    boolean swap = false;

                    for (int i = cars.size() - 1; i > 0; i--) {

                        if (cars.get(i).getPrice() < cars.get(i - 1).getPrice()) {
                            Car temp = cars.get(i);
                            cars.set(i, cars.get(i - 1));
                            cars.set(i - 1, temp);
                            swap = true;
                        }
                    }

                    if (!swap) {
                        break;
                    }
                }
            }

            for (Car car : cars) {
                System.out.println(car.toString());
            }
		}
    } 

    public void retriveCarFromInventory(int floor, int space) {

        try {
            cars[floor - 1][space - 1].toString();
        }

        catch (Exception e) {

            if (e instanceof ArrayIndexOutOfBoundsException) {
                System.out.println("Error: Attempted to Access an Invalid Space.");
            }

            else if (e instanceof NullPointerException) {
                System.out.println("Error: Attempted to Access an Empty Space");
            }
        }
    }

    public void displayMenu() {
        System.out.println("=== Car Vending Machine Menu ===");
        System.out.println("1. Load Car Data");
        System.out.println("2. Display Vending Machine");
        System.out.println("3. Retrieve a Car");
        System.out.println("4. Print Sorted Inventory (Price)");
        System.out.println("5. Print Sorted Inventory (Year)");
        System.out.println("6. Exit");

    }

    public void exit() {
        System.exit(0);
    }
}
