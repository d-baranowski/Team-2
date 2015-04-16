package com.team.two.lloyds_app.objects;

/**
 * Author: Daniel Baranowski
 * Date: 15/04/15
 * Purpose: code to hold data about offers
*/
public class Offer {
    int id;
    int icon;
    int barcode;
    String name;
    String description;
    int price;
    boolean active;

	//Single constructor
    public Offer(int id, int icon, int barcode, String name, String description, int price, boolean active) {
        this.id = id;
        this.icon = icon;
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
    }

	//Getters for fields
    public int getId() {
        return id;
    }

    public int getIcon() {
        return icon;
    }

    public int getBarcode() { return barcode; }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean getActive() { return active; }
}
