package de.veesy.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static de.veesy.connection.MESSAGE.DEVICE_FOUND;
import static de.veesy.connection.MESSAGE.DISCOVERABILITY_OFF;
import static de.veesy.connection.MESSAGE.DISCOVERABILITY_ON;
import static de.veesy.connection.MESSAGE.START_DISCOVERING;
import static de.veesy.connection.MESSAGE.STOP_DISCOVERING;

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
        initShareActivity_permission_denied();
    }

    //region GUI Handling

    /**
     * This method loads the default content for this activity
     * which is described in share_permission_denied
     */
    private void initShareActivity_permission_denied(){
        initConnectionManager();
        setContentView(R.layout.share_permission_denied);
    }

    /**
     * This method loads the content for this activity
     * which is described in share_permission_granted
     */
    private void initShareActivity_permission_granted(){
        startConnectionManager();
        setContentView(R.layout.share_permission_granted);
        initListView();
        initAnimation();
        initListView();
        setRefreshListener();
        //setList();
        //animationView.setVisibility(VISIBLE);
    }

    //endregion

    //region ConnectionManager

    private void initConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.addObserver(this);
        connectionManager.registerReceiver(this);
        connectionManager.startBluetoothIntent(this, 100);
    }

    private void startConnectionManager(){
        connectionManager.btCheckPermissions(this);
        connectionManager.discoverBluetoothDevices();
        connectionManager.btStartListeningForConnectionAttempts();
    }

    public void update(Observable o, Object arg) {

        switch ((Integer) arg) {
            case DEVICE_FOUND:
                adapter.setDeviceNames(connectionManager.btGetAvailableDeviceNames());
                break;
            case DISCOVERABILITY_ON:
                initShareActivity_permission_granted();
                break;
            case DISCOVERABILITY_OFF:
                animationView.setVisibility(INVISIBLE);
                break;
            case START_DISCOVERING:
                animationView.setVisibility(VISIBLE);
                break;
            case STOP_DISCOVERING:
                animationView.setVisibility(INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("onRequestPermissionsResult called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //endregion

    //region Animation, List, Refresh

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
                animationView.setVisibility(VISIBLE);
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

    //endregion


    protected void onDestroy() {
        connectionManager.unregisterReceiver(this);
        connectionManager.deleteObserver(this);
        super.onDestroy();
    }







}
