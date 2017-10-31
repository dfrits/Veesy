package de.veesy.core;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
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

public class ShareActivity extends WearableActivity implements Observer {
    private final List<String> DUMMY_DATA = new ArrayList<String>() {{
        add("Max Maier");
        add("Lisa Agathe");
        add("Bernd Ober");
    }};
    private final Context context = this;

    //TODO final, warum?
    private ConnectionManager connectionManager = null;

    private ShareAdapter adapter;

    public static final String CONTACT_DATA = "CONTACT_DATA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share);

        initListView();

        startConnectionManager();

        setList();
    }

    protected void onDestroy(){
        connectionManager.unregisterReceiver(this);
        connectionManager.deleteObserver(this);
        super.onDestroy();
    }

    private void startConnectionManager(){
        connectionManager = ConnectionManager.instance();
        connectionManager.addObserver(this);
        connectionManager.registerReceiver(this);
        connectionManager.discoverBluetoothDevices();
        connectionManager.startBluetoothIntent(this, 300);
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
        setList();
    }

    /**
     * Übergibt der Liste die neuen Daten
     */
    private void setList() {
        adapter.setDeviceNames(DUMMY_DATA);
    }

    protected void onListItemClick(int position, String deviceName) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(CONTACT_DATA, deviceName);
        startActivity(intent);
        finish();
    }

    /**
     * Aktion des Refresh-Buttons. Damit erneuert der Nutzer intentional.
     * @param view .
    */

    public void refresh(View view) {
        connectionManager.discoverBluetoothDevices();
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
