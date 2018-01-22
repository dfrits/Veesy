package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;
import de.veesy.listview_util.AdapterObject;
import de.veesy.listview_util.ListItemCallback;
import de.veesy.listview_util.RoundListAdapter;
import de.veesy.listview_util.ScrollingLayoutCallback;
import de.veesy.util.Constants;
import de.veesy.util.Util;

import static android.view.View.INVISIBLE;
import static de.veesy.connection.MESSAGE.DEVICE_FOUND;
import static de.veesy.connection.MESSAGE.DISCOVERABILITY_OFF;
import static de.veesy.connection.MESSAGE.DISCOVERABILITY_ON;
import static de.veesy.connection.MESSAGE.START_DISCOVERING;
import static de.veesy.connection.MESSAGE.STOP_DISCOVERING;
import static de.veesy.core.FeedbackActivity.SUCCESS_FLAG;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class ShareActivity extends Activity implements Observer {
    private ConnectionManager connectionManager = null;
    private RoundListAdapter adapter;
    private ImageView animationView;
    private Animation radar_animation;
    private static List<String> DUMMY_DATA;

    private static int visibility_time = 60;

    private boolean exchangeActivityAlreadyStarted = false;

    static {
        DUMMY_DATA = new ArrayList<>();
        DUMMY_DATA.add("Martin Stadlmaier");
        DUMMY_DATA.add("Vivien Bardosi");
        DUMMY_DATA.add("Daniel Fridge");
        DUMMY_DATA.add("Tianyi Zhang");
        DUMMY_DATA.add("Noch kein Gerät gefunden!");
        DUMMY_DATA.add("Geräte werden weitergesucht ...");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initShareActivity_permission_denied();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(300);
        }
    }

    //Debug
    private void setList() {
        adapter.setData(getDataList(DUMMY_DATA));
    }

    //region GUI Handling

    /**
     * This method loads the default content for this activity
     * which is described in share_permission_denied
     */
    private void initShareActivity_permission_denied() {
        setContentView(R.layout.share_permission_denied);
        initConnectionManager();
    }

    /**
     * This method loads the content for this activity
     * which is described in share_permission_granted
     */
    private void initShareActivity_permission_granted() {
        setContentView(R.layout.share_permission_granted);
        initListView();
        initAnimation();
        startConnectionManager();
        setRefreshListener();

        //Debug
        if (Constants.DEBUGGING) {
            setList();
        }
    }

    //endregion

    //region ConnectionManager

    private void initConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.addObserver(this);
        connectionManager.registerReceiver(this);
        boolean already_discoverable = connectionManager.startBluetoothIntent(this, visibility_time);
        if (already_discoverable) initShareActivity_permission_granted();
    }

    private void startConnectionManager() {
        connectionManager.discoverBluetoothDevices();
        connectionManager.btStartListeningForConnectionAttempts();
    }

    public void update(Observable o, Object arg) {
        boolean startExchangeActivity_flag = false;

        switch ((Integer) arg) {
            case DEVICE_FOUND:
                adapter.setData(getDataList(connectionManager.btGetAvailableDeviceNames()));
                break;
            case DISCOVERABILITY_ON:
                initShareActivity_permission_granted();
                break;
            case DISCOVERABILITY_OFF:
                setContentView(R.layout.share_permission_denied);
                connectionManager.startBluetoothIntent(this, visibility_time);
                break;
            case START_DISCOVERING:
                if (animationView != null)
                    Util.runAnimationOnUiThread(this, animationView, radar_animation);
                break;
            case STOP_DISCOVERING:
                if (animationView != null) {
                    animationView.clearAnimation();
                    animationView.setVisibility(INVISIBLE);
                }
                break;
            case MESSAGE.PAIRING:
                startExchangeActivity_flag = true;
                break;
            case MESSAGE.RESPOND_AS_CLIENT:
                System.out.println("MESSAGE.RESPOND in Exchange Act");
                connectionManager.btSendData();
                startFeedbackActivity();
                break;
            default:
                break;
        }
        if (startExchangeActivity_flag) {
            startExchangeActivity();
        }
    }

    private void startFeedbackActivity() {
        Intent feedback_intent = new Intent(this, FeedbackActivity.class);
        feedback_intent.putExtra(SUCCESS_FLAG, true);
        startActivity(feedback_intent);
        finish();
    }

    //endregion

    //region Animation, List, Refresh

    private void initAnimation() {
        animationView = findViewById(R.id.suchanimation_view);
        radar_animation = AnimationUtils.loadAnimation(this, R.anim.rotate_radar);
    }

    private void setRefreshListener() {
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                connectionManager.discoverBluetoothDevices();
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void initListView() {
        WearableRecyclerView recyclerView = findViewById(R.id.lVDevices);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        final ScrollingLayoutCallback scrollingLayoutCallback =
                new ScrollingLayoutCallback();
        adapter = new RoundListAdapter(new ListItemCallback() {
            @Override
            public void onItemClicked(int position, String value) {
                onListItemClick(value);
            }

            @Override
            public void onItemLongClicked(int position, String value) {}
        });
        recyclerView.setLayoutManager(
                new WearableLinearLayoutManager(this, scrollingLayoutCallback));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Erstellt eine Liste für den ListAdapter. Dabei wird jedem String der Punkt hinzufügt. Ist
     * Liste <b>null</b>, dann werden die Dummydaten verwendet.
     * @param list Liste mit anzuzeigenden Daten oder null
     * @return Liste der übergebenen daten plus Punkt
     */
    private List<AdapterObject> getDataList(List<String> list) {
        List<AdapterObject> dataList = new ArrayList<>();
        Drawable punkt = getResources().getDrawable(R.drawable.share_list_point_img, null);

        List<String> temp = list == null ? DUMMY_DATA : list;
        for (String item : temp) {
            dataList.add(new AdapterObject(item, punkt));
        }
        return dataList;
    }

    protected void onListItemClick(String deviceName) {
        //debug
        if (Constants.DEBUGGING && deviceName.equals("Martin Stadlmaier")) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            intent.putExtra("SUCCESS_FLAG", false);
            startActivity(intent);
            finish();
            return;
        }
        startExchangeActivity();
        connectionManager.btConnectToDevice(deviceName);
    }

    private void startExchangeActivity() {
        if (exchangeActivityAlreadyStarted) return;
        exchangeActivityAlreadyStarted = true;
        Intent intent = new Intent(this, ExchangeActivity.class);
        startActivity(intent);
        finish();
    }

    //endregion

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            connectionManager.unregisterReceiver(this);
            connectionManager.deleteObserver(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void bShareClicked(View view) {
        connectionManager.startBluetoothIntent(this, visibility_time);
        finish();
    }
}
