package de.veesy.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

//TODO hier wird nur über Strings gearbeitet, der ConnectionManager sollte egl eine Devicelist zurück geben und hier muss man sich dann die namen der devices holen/
// also sollte das kein List<String> objekt sein sonder ein List<BluetoothDevice>

public class ShareActivity extends WearableActivity implements Observer {
    private final Context context = this;

    //TODO final, warum?
    private ConnectionManager connectionManager = null;

    private ShareAdapter adapter;

    private static List<String> DUMMY_DATA;
    static {
        DUMMY_DATA = new ArrayList<>();
        DUMMY_DATA.add("Max Maier");
        DUMMY_DATA.add("Lisa Agathe");
        DUMMY_DATA.add("Bernd Ober");
    }

    public static final String CONTACT_DATA = "CONTACT_DATA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share);

        initListView();

        startConnectionManager();

        setList();
    }

    protected void onDestroy() {
        connectionManager.unregisterReceiver(this);
        connectionManager.deleteObserver(this);
        super.onDestroy();
    }

    private void startConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.btCheckPermissions(this);
        connectionManager.addObserver(this);
        connectionManager.registerReceiver(this);
        connectionManager.discoverBluetoothDevices();
        connectionManager.startBluetoothIntent(this, 300);
        connectionManager.btStartListeningForConnectionAttempts();
    }

    private void initListView() {
        SwipeRefreshLayout refreshView = findViewById(R.id.swiperefresh);
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(context, "Refreshing.......", Toast.LENGTH_LONG).show();
            }
        });

        WearableRecyclerView recyclerView = findViewById(R.id.lVDevices);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        final CustomScrollingLayoutCallback customScrollingLayoutCallback =
                new CustomScrollingLayoutCallback();
        adapter = new ShareAdapter(new ShareAdapter.Callback() {
            @Override
            public void onDeviceClicked(int position, String deviceName) {
                Toast.makeText(context, "Connecting with" + deviceName, Toast.LENGTH_SHORT).show();
                onListItemClick(position, deviceName);
            }
        });
        recyclerView.setLayoutManager(
                new WearableLinearLayoutManager(this, customScrollingLayoutCallback));
        recyclerView.setAdapter(adapter);
    }

    public void update(Observable o, Object arg) {
        adapter.setDeviceNames(connectionManager.btGetAvailableDeviceNames());
    }

    /**
     * Übergibt der Liste die neuen Daten
     */
    private void setList() {
        adapter.setDeviceNames(DUMMY_DATA);
    }


    protected void onListItemClick(int position, String deviceName) {
        connectionManager.btConnectToDevice(deviceName);

        //TODO uncomment to start feedback screen
        //Intent intent = new Intent(this, FeedbackActivity.class);
        //Intent.putExtra(CONTACT_DATA, deviceName);
        //startActivity(intent);
        //finish();
    }

    /**
     * Aktion des Refresh-Buttons. Damit erneuert der Nutzer intentional.
     * @param view .
     */
    public void refresh(View view) {
        connectionManager.discoverBluetoothDevices();
        // ist wsl unnötig, der Thread läuft ja immer
        //connectionManager.btStartListeningForConnectionAttempts();
    }


    /**
     * Activity beenden und zum Homescreen zurückkehren.
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(context, MainMenu.class));
        finish();
    }
}
