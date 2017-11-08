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

import de.veesy.MESSAGE;
import de.veesy.R;
import de.veesy.connection.ConnectionManager;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */


public class ShareActivity extends WearableActivity implements Observer {
    private final Context context = this;

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

        startConnectionManager();

        //TODO hier den ein layout erstellen, welches dem user nahe legt bluetooth zu aktivieren und ihn entweder zu home zurück schickt, oder dann nochmal den BT visible Intent aufruft
        // das feedback layout is nur zu test zwecken drin.
        // also standard mäßig wird davon ausgegangen, dass der bluetooth adapter nicht visible gesetzt wurde und deswegen
        // soll ein neues layout (neuer Screen) "schalt mal Bluetooth visible oder geh zurück zu home" erstellt werden und hier dann aufgerufen werden
        setContentView(R.layout.feedback_act);
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
        connectionManager.startBluetoothIntent(this, 100);
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

        switch ((Integer) arg) {
            case MESSAGE.DEVICE_FOUND:
                adapter.setDeviceNames(connectionManager.btGetAvailableDeviceNames());
                break;
            case MESSAGE.DISCOVERABILITY_ON:

                //TODO falls der user bestätigt hat und disco on is kommt hier was
                //wenn er nicht bestätigt, kommt auch nichts...
                System.out.println("DISCO ON, You Rock!!!");

                setContentView(R.layout.share);
                initListView();
                setList();

                break;
            case MESSAGE.DISCOVERABILITY_OFF:
                //hier kommt nur dann was an, wenn sich der status ändert (von on zu off beispielsweiße)
                System.out.println("DISCO Off, You Suck!!!");
                break;
            default:
                break;
        }
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
     *
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
