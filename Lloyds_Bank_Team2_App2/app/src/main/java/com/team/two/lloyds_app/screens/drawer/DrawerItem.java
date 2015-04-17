package com.team.two.lloyds_app.screens.drawer;

/**
 * Author: Daniel Baranowski
 * Date: 18/03/2015
 * Purpose: Drawer code
 */
class DrawerItem {
    private final String ItemName;
    private final int imgResID;

    public DrawerItem(String itemName, int imgResID) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
    }

    public String getItemName() {
        return ItemName;
    }
    public int getImgResID() {
        return imgResID;
    }

}