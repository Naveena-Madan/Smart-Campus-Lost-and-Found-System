package model;

import exception.InvalidItemException;

public class Item {

    private String itemName;
    private String location;

    public Item(String itemName, String location)
            throws InvalidItemException {

        if (itemName == null || itemName.trim().isEmpty()) {
            throw new InvalidItemException("Item name cannot be empty.");
        }

        if (location == null || location.trim().isEmpty()) {
            throw new InvalidItemException("Location cannot be empty.");
        }

        this.itemName = itemName;
        this.location = location;
    }

    public void displayItem() {
        System.out.println("Item: " + itemName);
        System.out.println("Location: " + location);
    }
}