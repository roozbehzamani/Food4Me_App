package com.shm_rz.ufoodapp.Model;

/**
 * Created by Roozbeh Zamani on 14/05/2018.
 */

public class Menu {
    int menuID;
    String menuTitle , menuType , menuImage;

    public Menu(String menuTitle, String menuImage) {
        this.menuTitle = menuTitle;
        this.menuImage = menuImage;
    }

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }
}
