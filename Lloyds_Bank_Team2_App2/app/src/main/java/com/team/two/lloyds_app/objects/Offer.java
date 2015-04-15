package com.team.two.lloyds_app.objects;

/**
* Created by danielbaranowski on 15/04/15.
*/
public class Offer {
    int id;
    int icon;
    int barcode;
    String name;
    String description;
    int price;
    boolean active;

    public Offer(int id, int icon, String name, String description, int price, boolean active) {
        this.id = id;
        this.icon = icon;
        this.barcode = icon;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
    }

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
