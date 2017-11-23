package de.veesy.settings;

/**
 * Created by dfritsch on 22.11.2017.
 * veesy.de
 * hs-augsburg
 */

enum ESettingItems {

    MY_CARD("My Card"),
    BLUETOOTH_SETTING("Bluetooth Settings"),
    ABOUT("About Us"),
    REMOVE_DEVICES("Remove bonded devices");

    private String name;

    public String getName() {
        return name;
    }

    ESettingItems(String name) {
        this.name = name;
    }
}
