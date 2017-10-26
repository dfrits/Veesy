package de.veesy.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;


/**
 * Created by Martin on 24.10.2017.
 */

/**
 *
 * Solange wir keine Uhren zum Testn haben, ist diese Klasse ohne Funktion. Alle Referenzen darauf
 * sind auskommentiert.
 *
 *
 *
 * This class is observed by ShareActivity to update the items in the list
 *
 * **/



/**
 *
 *  --------------------------------- Nochmal drüber nachdenken ------------------------------------
 *
 * macht das so überhaupt sinn? Vllt sollte man den Broadcast und allgemein das ganze Bluetooth zeug
 * gleich in die ShareActivity packen. Ansonsten muss man immer den
 * context mit überreichen. Ist das egl nicht total umständlich?
 *
 * Auf Bluetooth muss ja an sich eh nur die ShareActivity zugreifen.
 *
 * man könnte sich dadurch auch den Observer Pattern sparen.
 *
 * die FeedbackActivity könnte man auch von ShareAct aus aufrufen.. solange der Datenaustausch erfolgt
 * Animation anzeigen
 *
 *
 */







public class ConnectionManager extends Observable {

    private static ConnectionManager unique = null;
    private static BluetoothAdapter btAdapter = null;
    private static final String TAG = "ConnectionManager";
    private static String btDeviceName = "default_name";
    private static String prefix_name = "[veesy]";
    private static ArrayList<String> newBluetoothDevices;


    // Singleton Pattern to ensure only one instance of ConnectionManager is used
    private ConnectionManager() {
        initBluetooth();
        renameDevice();
        newBluetoothDevices = new ArrayList<>();
    }

    public static ConnectionManager instance() {
        if (unique == null) unique = new ConnectionManager();
        return unique;
    }



    private static void initBluetooth(){
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            // Bluetooth is not supported
            Log.w(TAG, "Bluetooth is not supported on this device");
            return;
        }
        btDeviceName = btAdapter.getName();
    }

    private static void renameDevice(){
        btAdapter.setName(prefix_name+ btDeviceName);
    }


    /**
     *
     * this method enables the bluetooth and sets it visible for some time
     *
     * in order to start the User input dialog, we need to pass Context
     *
     */
    public void startBluetooth(Context context, int visibility_time){

        Intent discoverableIntent  = new Intent (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, visibility_time);

        context.startActivity(discoverableIntent);

    }

    public void stopBluetooth(){
        btAdapter.disable();
    }



    /**
     * this method searches for other Bluetooth devices
     */
    public boolean discoverBluetoothDevices(){
        if(btAdapter.isDiscovering()){
            btAdapter.cancelDiscovery();
        }
        return btAdapter.startDiscovery();
    }




    /**
     * if other Devices are found

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                // no we have to check, if it is a "veesy" device
                if(isVeesyDevice(deviceName)) {
                    // now we check if the devices have already been bonded, if not, then add them to the list of new devices
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        newBluetoothDevices.add(deviceName + ", Adress: "+deviceHardwareAddress );
                    }
                }
            }
        }
    };



    // this method determines, whether the name of a Device could be split by "-", and if,
    // is the first part [veesy]?
    private boolean isVeesyDevice(String deviceName){
        String [] strings = deviceName.split("-");
        return strings[0].equals(prefix_name);
    }


    public void registerReceiver(Context context){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
    }

    public void unregisterReceiver(Context context){
        if (btAdapter != null) {
            btAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        context.unregisterReceiver(mReceiver);
    }
     */
}
