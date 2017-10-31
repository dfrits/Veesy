package de.veesy.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;



public class ConnectionManager extends Observable {

    //region Class members

    private static ConnectionManager unique = null;
    private static BluetoothAdapter btAdapter = null;
    private static final String TAG = "ConnectionManager";

    private static String btName_device = "no name";
    private static String btName_prefix = "[veesy]";
    private static String btName_splitter = "-";
    private static boolean btNameCorrect_flag = false;


    private static ArrayList<BluetoothDevice> availableVeesyBTDevices;

    //endregion

    // Singleton Pattern to ensure only one instance of ConnectionManager is used
    private ConnectionManager() {
        if (!initBluetooth()){
            //TODO implement Bluetooth init error
        }
        enableBluetooth();
        renameDevice();
        availableVeesyBTDevices = new ArrayList<>();
    }

    public static ConnectionManager instance() {
        if (unique == null) unique = new ConnectionManager();
        return unique;
    }

    //region Initializing
    // initialize BluetoothAdapter
    private static boolean initBluetooth() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            // Bluetooth is not supported
            Log.w(TAG, "Bluetooth is not supported on this device");
            return false;
        }
        btName_device = btAdapter.getName();
        Log.i(TAG, "Bluetooth initialized");
        Log.i(TAG, "Current Device name is: " + btName_device);
        return true;
    }

    //endregion

    // region Bluetooth - Renaming & Veesy environment

    // This method renaming the bluetooth device
    //
    // Therefore, Bluetooth has to be enabled (this needs some time)
    // First, we check, if the name is already correct in terms of a predefined style
    // if the name has the prefix [veesy] its correct
    // if not, we start a timeHandler which is delayed with 1s/  500ms
    // this handler waits for bluetooth to activate, and for the new name
    // to sink in (this also takes some time)

    // if something goes wrong, this method determines after 10s
    //
    private static void renameDevice() {

        if (!isVeesyDevice(btName_device)) {
            if (btAdapter != null) {
                enableBluetooth();
                final long timeOutMillis = System.currentTimeMillis() + 10000;
                final Handler timeHandler = new Handler();
                final String newName = btName_prefix + btName_splitter + btName_device;
                final long delayMillis = 500;

                timeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!btNameCorrect_flag) {
                            btAdapter.setName(newName);
                            if (btAdapter.getName().equals(newName)) {
                                Log.i(TAG, "Set BT name to: " + btAdapter.getName());
                                btNameCorrect_flag = true;
                            }
                            if (!(btAdapter.getName().equals(newName)) && System.currentTimeMillis() < timeOutMillis) {
                                timeHandler.postDelayed(this, delayMillis);
                                if (!btAdapter.isEnabled())
                                    Log.i(TAG, "Renaming bluetooth device . . . waiting for Bluetooth to enable");
                                else
                                    Log.i(TAG, "Renaming bluetooth device . . . waiting for name to sink in");
                            }
                        }
                    }
                }, delayMillis);
            }
        } else{
            btNameCorrect_flag = true;
            Log.i(TAG, "Device is already named correctly");
        }
    }

    // this method provides a possibility to track if a discovered BT device is
    // part of the veesy environment
    // this method determines, whether the name of a Device could be split by btName_splitter, and if,
    // is the first part btName_prefix?

    private static boolean isVeesyDevice(String deviceName) {
        try{
            return deviceName.split(btName_splitter)[0].equals(btName_prefix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isBtNameCorrect_flag() {
        return btNameCorrect_flag;
    }


    public static void logSomeShit() {
        Log.i(TAG, "Discovering devices" + btAdapter.isDiscovering());
    }

    //endregion

    //region Bluetooth - Enable & Discover

    /**
     * this method enables the bluetooth and sets it visible for some time
     * <p>
     * in order to start the User input dialog, we need to pass Context
     */
    public void startBluetoothIntent(Context context, int visibility_time) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, visibility_time);
        context.startActivity(discoverableIntent);
    }

    public static void enableBluetooth() {
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
        }
    }

    public static void stopBluetooth() {
        btAdapter.disable();
    }


    /**
     * this method searches for other Bluetooth devices
     */
    public boolean discoverBluetoothDevices() {
        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
        }
        //availableVeesyBTDevices.clear();
        Log.i(TAG, "restarting discovery");
        return btAdapter.startDiscovery();
    }


    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
    }

    public void unregisterReceiver(Context context) {
        if (btAdapter != null) {
            btAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        context.unregisterReceiver(mReceiver);
    }

    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            Log.i(TAG, "onReceive action:  .  .  .  .  "+action);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // A Bluetooth device was found
                // Getting device information from the intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();

                Log.i(TAG, "Device found: " + deviceName + "; MAC " + deviceHardwareAddress);

                if (isVeesyDevice(deviceName)) {

                    if(!availableVeesyBTDevices.contains(device)){
                        availableVeesyBTDevices.add(device);
                        setChanged();
                        notifyObservers();
                    }

                }
            }
        }
    };

    // endregion

    //region Bluetooth - Connecting & Datatransmission

    //endregion

    //region Getter & Settter
    public List<String> getAvailableBTDevicesNames() {
        List<String> list = new ArrayList<>();
        for(BluetoothDevice d : availableVeesyBTDevices){
            list.add(d.getName());
        }
        return list;
    }

    // endregion
}
