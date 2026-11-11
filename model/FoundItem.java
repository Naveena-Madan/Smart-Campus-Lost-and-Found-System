package model;

import exception.InvalidItemException;

public class FoundItem extends Item implements Reportable {

    private String foundDate;

    public FoundItem(String itemName, String location, String foundDate)
            throws InvalidItemException {

        super(itemName, location);
        this.foundDate = foundDate;
    }

    @Override
    public void displayItem() {
        super.displayItem();
        System.out.println("Found Date: " + foundDate);
        System.out.println("Type: Found Item");
    }

    @Override
    public void displayReport() {
        System.out.println("Report: This item has been reported as found.");
    }
}