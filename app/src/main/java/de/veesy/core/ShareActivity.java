package de.veesy.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.PermissionRequest;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.util.MESSAGE;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */


public class ShareActivity extends Activity implements Observer {
    private final Context context = this;

    private ConnectionManager connectionManager = null;
    private ShareAdapter adapter;
    private ImageView animationView;
    private static List<String> DUMMY_DATA;

    static {
        DUMMY_DATA = new ArrayList<>();
        DUMMY_DATA.add("Martin Stadlmaier");
        DUMMY_DATA.add("Vivien Bardosi");
        DUMMY_DATA.add("Daniel Fridge");
        DUMMY_DATA.add("Tianyi Zhang");
        DUMMY_DATA.add("Noch kein Gerät gefunden!");
        DUMMY_DATA.add("Geräte werden weitergesucht ...");
    }

    public static final String CONTACT_DATA = "CONTACT_DATA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        initAnimation();
        initListView();
        startConnectionManager();
        setRefreshListener();

        //animationView.setVisibility(View.VISIBLE);
        //setList();

        //TODO hier den ein layout erstellen, welches dem user nahe legt bluetooth zu aktivieren und ihn entweder zu home zurück schickt, oder dann nochmal den BT visible Intent aufruft
        // das feedback layout is nur zu test zwecken drin.
        // also standard mäßig wird davon ausgegangen, dass der bluetooth adapter nicht visible gesetzt wurde und deswegen
        // soll ein neues layout (neuer Screen) "schalt mal Bluetooth visible oder geh zurück zu home" erstellt werden und hier dann aufgerufen werden
        //setContentView(R.layout.feedback_act);
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

    private void initAnimation() {
        animationView = findViewById(R.id.suchanimation_view);
        final Animation a = AnimationUtils.loadAnimation(this, R.anim.rotate);
        animationView.startAnimation(a);
    }

    private void setRefreshListener() {
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                animationView.setVisibility(View.VISIBLE);
                connectionManager.discoverBluetoothDevices();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (refreshLayout.isRefreshing()) {
                            refreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
    }

    private void initListView() {
        WearableRecyclerView recyclerView = findViewById(R.id.lVDevices);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        final ScrollingLayoutCallback scrollingLayoutCallback =
                new ScrollingLayoutCallback();
        adapter = new ShareAdapter(new ShareAdapter.Callback() {
            @Override
            public void onDeviceClicked(int position, String deviceName) {
                Toast.makeText(context, "Connecting with" + deviceName, Toast.LENGTH_SHORT).show();
                onListItemClick(position, deviceName);
            }
        });
        recyclerView.setLayoutManager(
                new WearableLinearLayoutManager(this, scrollingLayoutCallback));
        recyclerView.setAdapter(adapter);
    }


    public void update(Observable o, Object arg) {

        switch ((Integer) arg) {
            case MESSAGE.DEVICE_FOUND:
                adapter.setDeviceNames(connectionManager.btGetAvailableDeviceNames());
                break;
            case MESSAGE.DISCOVERABILITY_ON:
                animationView.setVisibility(View.VISIBLE);
                //TODO falls der user bestätigt hat und disco on is kommt hier was
                //wenn er nicht bestätigt, kommt auch nichts...
                System.out.println("DISCO ON, You Rock!!!");
                //setContentView(R.layout.share);
                initListView();
                setList();

                break;
            case MESSAGE.DISCOVERABILITY_OFF:
                animationView.setVisibility(View.INVISIBLE);
                //hier kommt nur dann was an, wenn sich der status ändert (von on zu off beispielsweiße)
                System.out.println("DISCO Off, You Suck!!!");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //TODO permissions abfangen und darauf reagieren
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        Intent intent = new Intent(this, FeedbackActivity.class);
        // Intent.putExtra(CONTACT_DATA, deviceName);
        startActivity(intent);
        finish();
    }

    protected void onDestroy() {
        connectionManager.unregisterReceiver(this);
        connectionManager.deleteObserver(this);
        super.onDestroy();
    }
}
