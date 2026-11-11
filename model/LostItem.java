package model;

import exception.InvalidItemException;

public class LostItem extends Item implements Reportable {

    private String lostDate;

    public LostItem(String itemName, String location, String lostDate)
            throws InvalidItemException {

        super(itemName, location);
        this.lostDate = lostDate;
    }

    @Override
    public void displayItem() {
        super.displayItem();
        System.out.println("Lost Date: " + lostDate);
        System.out.println("Type: Lost Item");
    }

    @Override
    public void displayReport() {
        System.out.println("Report: This item has been reported as lost.");
    }
}