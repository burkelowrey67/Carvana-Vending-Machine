public class testing {
    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine(5, 4);
        vendingMachine.loadCarData("src/cars1.txt");
        vendingMachine.displayVendingMachine();
        vendingMachine.retriveCarFromInventory(1, 3);
        vendingMachine.retriveCarFromInventory(1, 2);
        vendingMachine.retriveCarFromInventory(10, 1);
        vendingMachine.printSortedInventoryPrice();
        vendingMachine.printSortedInventoryYear();
        vendingMachine.exit();
    }
}
