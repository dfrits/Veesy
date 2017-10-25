package de.veesy.connection;

import java.util.Observable;

/**
 * Created by Martin on 24.10.2017.
 * veesy.de
 * hs-augsburg
 *
 * This class is observed by ShareActivity to update the items in the list
 */

/** TODO
 **
 *
 *
 * **/


public class ConnectionManager extends Observable {

    private static ConnectionManager unique = null;

    // Singleton Pattern to ensure only one instance of ConnectionManager is used
    private ConnectionManager() {
    }

    public static ConnectionManager instance() {
        if (unique == null) unique = new ConnectionManager();
        return unique;
    }



}
