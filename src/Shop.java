import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This is the main class - the program is run by java Shop
 * 
 * @author Mrs Miggins
 * @fixerupper Daniel Bloor
 * @version 1.968 "Scooby Doo" (17th March 2015)
 */

public class Shop {

    private String shopName;
    private static Scanner scan; // Required to take input
    private UKTill till; // Storage for money
    private ArrayList<Item> Stock; // Storage for items for the customer to buy
    private Item item; // Used to refer to items in the Stock ArrayList
    private int totalCostDue; // Store as pence

    // Both used for saving and loading the till and stock
    private static final String SHOP_STOCK_DATA_FILE = "./stock.txt";
    private static final String SHOP_TILL_DATA_FILE = "./till.txt";

    public Shop(String name) {
        shopName = name;
        till = new UKTill();
        scan = new Scanner(System.in);
        Stock = new ArrayList<Item>();
    }

    /**
     * Using stockShop an owner can add various goods to the system. We can then
     * can add an item, set the price and set the stock level.
     */
    public void stockShop() {
        runStockMenu(); // The sub-menu used for modifying stock
        String choice;
        do {
            choice = scan.nextLine();

            switch (choice) {
            case "1":
                System.out.println("\n" + "Stock status - " + "\n");
                stockStatus(Stock);
                break;
            case "2":
                addItem(scan);
                break;
            case "3":
                searchAndModify(3);
                break;
            case "4":
                searchAndModify(4);
                break;
            case "5":
                searchAndModify(5);
                break;
            case "6":
                searchAndModify(6);
                break;
            case "7":
                searchAndModify(7);
                break;
            case "8":
                System.out.println("Back to main menu - ");
                break;
            default:
                System.err.println("Incorrect choice entered");
            }
            runStockMenu();
        } while (!choice.equals("8"));
    }

    private void runStockMenu() {
        System.out.println("\n----------Stock Menu----------\n");
        System.out.println("What would you like to do?: ");
        System.out.println("1 - Stock status");
        System.out.println("2 - Add item");
        System.out.println("3 - Change stock level for existing item(s)");
        System.out.println("4 - Change id of existing item(s)");
        System.out.println("5 - Change name of existing item(s)");
        System.out.println("6 - Change price of existing item(s)");
        System.out.println("7 - Remove existing item(s)");
        System.out.println("8 - Back to main menu");
    }

    /**
     * searchAndModify combines a search operation in a loop with a switch
     * statement, which uses scanType integer to choose which operation to run
     * on the stock
     * 
     * @param scanType
     */
    private void searchAndModify(int scanType) {
        boolean notFound = true;
        do {
            int id = getInt("Please enter ID of item to modify: ");
            scan.nextLine();
            /*
             * Searches through the stock with the id value provided, and then
             * performs the scanType operation on it
             */
            for (Item stockItem : Stock) {

                if (stockItem.getIdentifier() == id) {
                    notFound = false;
                    System.out.println("Item: " + stockItem.getIdentifier() + " " + stockItem.getName());

                    switch (scanType) {
                    case 3:
                        System.out.println("Change stock quantity for existing item - \n");
                        stockItem.setQuantity(getInt("Please enter new quantity for item: "));
                        System.out.println("The stock quantity for '" + stockItem.getIdentifier() + " "
                                + stockItem.getName() + "' is now: " + stockItem.getQuantity());
                        break;
                    case 4:
                        System.out.println("Change item ID for existing item - \n");
                        System.out.println("Please enter new ID for this item: ");
                        stockItem.setID(scan.nextInt());
                        scan.nextLine();
                        System.out.println("The item ID for '" + stockItem.getName() + "' is now: "
                                + stockItem.getIdentifier());
                        break;
                    case 5:
                        System.out.println("Change item name for existing item - \n");
                        System.out.println("Please enter new name for this item: ");
                        stockItem.setName(scan.nextLine());
                        System.out.println("The item name for '" + stockItem.getIdentifier() + "' is now: "
                                + stockItem.getName());
                        break;
                    case 6:
                        System.out.println("Change item price for existing item - \n");
                        stockItem.setCost(getInt("Please enter new price for this item: "));
                        System.out.println("The item price for '" + stockItem.getIdentifier() + " "
                                + stockItem.getName() + "' is now: " + stockItem.getCost());
                        break;
                    case 7:
                        System.out.println("Remove existing items - \n");
                        Stock.remove(stockItem);
                        System.out.println("Item removed");
                        break;
                    default:
                        System.err.println("Incorrect choice entered; returning to Stock menu...");
                        stockShop();
                    }
                }
            }
            if (notFound == true) {
                System.out.println("Item not found");
            }
        } while (doContinue());
    }

    /**
     * Using startTill, an owner can add the various denomination floats to the
     * till specifying the name, value and quantity of each item (If she has 33
     * "10p pieces", each of which is worth 10, she enters "10p piece", 10, 33
     * (one separate lines)).
     */
    public void startTill() {
        do {
            // Asking the user for the coin/note, which is then stored for the
            // float
            UKDenomination coinType = getDenominationType();
            int noOfCoins = getInt("Number of these coins: ");
            DenominationFloat denom = new DenominationFloat(coinType, noOfCoins);
            till.addFloat(denom);

            System.out.println("Denomination floats entered into till: " + denom);
        } while (doContinue());
    }

    /**
     * Using runTill, an owner can sell items. Customers put in their order, the
     * system then tells her how much to charge.
     */
    public void runTill() {
        do {
            int id = getInt("Enter id of item to scan: ");
            scan.nextLine();

            // Iterates through the stock
            for (int stockItem = 0; stockItem < Stock.size(); stockItem++) {

                // If it finds the item we're after...
                if (Stock.get(stockItem).getIdentifier() == id) {
                    System.out.println("Item: " + Stock.get(stockItem));

                    // As long as it's in stock...
                    if (Stock.get(stockItem).getQuantity() >= 1) {

                        // Add the cost to how much is due...
                        totalCostDue += Stock.get(stockItem).getCost();

                        // ...and remove one from stock
                        Stock.get(stockItem).setQuantity((Stock.get(stockItem)).getQuantity() - 1);
                        break;

                    } else {

                        // If it's not in stock, print this
                        System.out.println("Item out of stock");
                        break;
                    }
                }
                if (stockItem == Stock.size() - 1) {

                    // If the ID isn't found, print this
                    System.out.println("Item not found");
                }
            }
        } while (doContinue());
        displayCost("Total due: ", totalCostDue);
    }

    /**
     * Takes information about an item that the user wishes to add to the stock,
     * and then it adds it in the stock array list
     */
    private void addItem(Scanner input) {
        do {
            System.out.println("Add item - \n");
            item = new Item();
            item.readKeyboard(input);
            if (item.getIdentifier() == 0) {
                System.out.println("Sorry, id cannot be 0; please id that isn't 0");
            } else {
                Stock.add(item);
            }
        } while (doContinue());
    }

    /**
     * Using getChange, an owner can tell the system how much of each
     * denomination she has been given by the customer and the till tells her
     * what to give back.
     */
    public void getChange() {
        displayCost("The cost of your order is: ", totalCostDue);
        do {
            // Gets the denomination from the user
            UKDenomination coinType = getDenominationType();

            // Gets the quantity of the denomination from the user
            int noOfCoins = getInt("Number of these coins: ");

            // Creates denominationFloat from the above 2 variables
            DenominationFloat denom = new DenominationFloat(coinType, noOfCoins);

            // Adds these coins to the till
            till.addFloat(denom);

            // Takes it off of the total that the customer has to pay
            totalCostDue -= coinType.getValue() * noOfCoins;
            displayCost("Remaining due: ", totalCostDue);
        } while (totalCostDue > 0);

        if (totalCostDue == 0) {
            System.out.println("You provided the exact amount, thank you!");
        } else {
            DenominationFloat[] change = till.getChange(Math.abs(totalCostDue));
            System.out.println("Here is your change:");
            for (DenominationFloat denom : change) {
                if (denom != null) {
                    System.out.println(denom);
                }
            }
        }
    }

    /**
     * Using getBalance it tells the owner what is left in the till
     */
    public void getBalance() {
        System.out.println(till);
    }

    /**
     * runMenu provides the main menu to the shop allowing a user to select
     * their required operation
     */
    public void runMenu() {
        // This is the main menu which runs the whole shop

        String choice;
        do {
            printMenu();
            choice = scan.nextLine();

            switch (choice) {
            case "1":
                stockShop();
                break;
            case "2":
                startTill();
                break;
            case "3":
                runTill();
                break;
            case "4":
                getChange();
                break;
            case "5":
                getBalance();
                break;
            case "6":
                System.out.println("Thank you for running " + shopName + " program");
                break;
            default:
                System.err.println("Incorrect choice entered");
            }
        } while (!choice.equals("6"));
    }

    /**
     * doContinue provides the ability to repeat the operation on another item,
     * or returns the user back to a menu, for example
     * 
     * @return
     */
    private boolean doContinue() {
        System.out.println("Continue? (Y/N)");
        String answer = scan.next().toUpperCase();
        scan.nextLine();
        return answer.equals("Y");
    }

    /**
     * getInt provides a test on the input to make sure that it is an integer,
     * otherwise it throws an exception.
     * 
     * @param message
     * @return
     */
    public static int getInt(String message) {
        // Makes the loop keep going until correct is true, forcing an int to be
        // given before continuing
        boolean correct = false;
        int result = 0;
        do {
            System.out.println(message);
            try {
                result = scan.nextInt();
                if (result < 0) {
                    System.out.println("Please provide a non-negative value.");
                } else {
                    scan.nextLine();
                    correct = true;
                }
            } catch (InputMismatchException ime) {
                System.err.println("Please enter an number");
                scan.nextLine();
            }
        } while (!correct);
        return result;
    }

    public static void displayCost(String message, int amountInPence) {
        // Displays a float that appears in the format of pounds and pence
        System.out.format("%s %d.%02d\n", message, amountInPence / 100, amountInPence % 100);
    }

    /**
     * getDenominationType automates the process of taking in a denomination
     * 
     * @return
     */
    private UKDenomination getDenominationType() {
        UKDenomination result;
        do {
            System.out.println("Enter the denomination type. One of: ");
            for (UKDenomination denom : UKDenomination.values()) {
                System.out.print(denom + " ");
            }
            String choice = scan.nextLine();
            result = UKDenomination.fromString(choice);
            if (result == null) {
                System.err.println("Incorrect denomination entered. Try again!");
            }
        } while (result == null);
        return result;
    }

    private void printMenu() {
        System.out.println("\n----------Main Menu----------\n");
        System.out.println("Welcome to " + shopName + ". Please enter choice:");
        System.out.println("1 - Modify stock");
        System.out.println("2 - Add coins to the till");
        System.out.println("3 - Process customer order");
        System.out.println("4 - Process customer payment");
        System.out.println("5 - Display till balance");
        System.out.println("6 - Exit shop program");
    }

    /**
     * stockStatus displays the items and their properties that are in the stock
     * 
     * @param Stock
     */
    private void stockStatus(ArrayList<Item> Stock) {
        for (Item item : Stock) {
            System.out.println("ID: " + item.getIdentifier() + "\n" + "Name: " + item.getName() + "\n"
                    + "Cost in pence: " + item.getCost() + "\n" + "Quantity in stock: " + item.getQuantity() + "\n");
        }
    }

    /**
     * Saves data to the shop database (stock and till) by calling stockSave and
     * tillSave
     * 
     * @exception IOException
     *                thrown when file problems occur
     */
    private void save() throws IOException {
        stockSave();
        tillSave();
    }

    // Loops through the stock, saving each value and the overall stock
    // arraylist size
    private void stockSave() throws IOException {
        try (PrintWriter stockSaveFile = new PrintWriter(new FileWriter(SHOP_STOCK_DATA_FILE))) {
            stockSaveFile.println(Stock.size());
            for (Item item : Stock) {
                stockSaveFile.println(item.getIdentifier());
                stockSaveFile.println(item.getName());
                stockSaveFile.println(item.getCost());
                stockSaveFile.println(item.getQuantity());
            }
        }
    }

    // Loops through the floats, saving them straight into the save file
    private void tillSave() throws IOException {
        try (PrintWriter tillSaveFile = new PrintWriter(new FileWriter(SHOP_TILL_DATA_FILE))) {
            DenominationFloat[] contents = till.emptyTill();
            for (DenominationFloat denom : contents) {
                tillSaveFile.println(denom);
            }
        }
    }

    /**
     * Loads data from the shop database (stock and till)
     * 
     * @exception IOException
     *                thrown when file problems occur
     */
    private void load() throws FileNotFoundException {
        Stock.clear();
        loadTill();
        loadStock();
    }

    private void loadTill() throws FileNotFoundException {
        try (Scanner tillLoadFile = new Scanner(new FileReader(SHOP_TILL_DATA_FILE))) {
            for (int line = 1; line <= 12; line++) {
                // Provides the amount of denominations to store for the
                // denomination
                int denomQuantity = tillLoadFile.nextInt();
                tillLoadFile.next(); // Consumes ready for denom
                // Stores the denom ready for making a float to store
                UKDenomination denom = UKDenomination.fromString(tillLoadFile.nextLine());
                // Creates the float from the denom and quantity variables
                DenominationFloat newDenom = new DenominationFloat(denom, denomQuantity);
                // Finally, adds the float to the till
                till.addFloat(newDenom);
            }
        }
    }

    /*
     * Similar process to loadTill, but a bit simpler to understand Loops
     * through the save file, taking the values in order and storing them
     * appropriately to what they represent for a stock item and then creates a
     * stock item out of the values, then adds it to the stock
     */
    private void loadStock() throws FileNotFoundException {
        try (Scanner stockLoadFile = new Scanner(new FileReader(SHOP_STOCK_DATA_FILE))) {
            int stockSize = Integer.parseInt(stockLoadFile.nextLine());
            for (int item = 0; item < stockSize; item++) {
                int identifier = stockLoadFile.nextInt();
                stockLoadFile.nextLine();
                String name = stockLoadFile.nextLine();
                int cost = Integer.parseInt(stockLoadFile.nextLine());
                int quantity = Integer.parseInt(stockLoadFile.nextLine());
                Item stockItem = new Item(identifier, name, cost, quantity);
                Stock.add(stockItem);
            }
        }
    }

    public static void main(String[] args) {
        // Don't touch any of this code
        Shop migginsPieShop = new Shop("Mrs Miggins Pie Shop");
        try {
            migginsPieShop.load();
        } catch (IOException e) {
            // Something went wrong so start a new shop
            System.err.println("Sorry but we were unable to load shop data: " + e.getMessage());
        }

        migginsPieShop.runMenu();

        try {
            migginsPieShop.save();
        } catch (IOException e) {
            System.err.println("Sorry but we just lost everything. Unable to save shop data: " + e.getMessage());
        }
    }

}
