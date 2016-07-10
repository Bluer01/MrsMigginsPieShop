import java.util.Scanner;

/**
 * My first attempt at the Item class
 * 
 * @author Mrs Miggins
 * @fixerupper Daniel Bloor
 * @version 1.968 "Scooby Doo" (17th March 2015)
 * 
 *          Item represents a single item type on sale and will probably have a
 *          unique identifier, a name ("steak and kidney"), a cost (3.99) and a
 *          quantity (7)
 * 
 */

public class Item {
    private int identifier;
    private String name;
    private int cost;
    private int quantity;

    public Item(int id) {
        this(id, "", 0, 0);
    }

    public Item(int id, String n, int c) {
        this(id, n, c, 0);
    }

    public Item() {
        identifier = 0;
    }

    public Item(int id, String n, int c, int q) {
        identifier = id;
        name = n;
        cost = c;
        quantity = q;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public void setID(int i) {
        identifier = i;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item other = (Item) obj;
        if (identifier == 0) {
            if (other.identifier != 0)
                return false;
        } else if (!(identifier == (other.identifier)))
            return false;
        return true;
    }

    /**
     * Takes in the information from the user about the item to add to the stock
     */
    public void readKeyboard(Scanner input) {
        identifier = Shop.getInt("Identifier: ");
        System.out.println("Item-name: ");
        name = input.nextLine();
        cost = Shop.getInt("Cost (in pennies): ");
        quantity = Shop.getInt("Quantity-in-stock: ");
        System.out.println("Item added: " + identifier + " " + name);
    }

    @Override
    public String toString() {
        return "\nID: " + identifier + "; " + "Name: " + name + "; " + "Value: " + cost + "; " + "Quantity in stock: "
                + quantity;
    }
}