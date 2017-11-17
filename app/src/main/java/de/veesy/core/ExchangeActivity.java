package de.veesy.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;


/**
 * Created by Martin on 17.11.2017.
 */

public class ExchangeActivity extends Activity implements Observer {

    public static final String ALREADY_PAIRED = "ALREADY_PAIRED";

    private boolean already_paired_flag = true;


    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    private ConnectionManager connectionManager = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        already_paired_flag = getIntent().getBooleanExtra(ALREADY_PAIRED, false);
        if (already_paired_flag) {
            initExchangeActivity_paired();
        } else {
            initExchangeActivity_not_paired();
            already_paired_flag = false;
        }
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        connectionManager.unregisterReceiver(this);
        //connectionManager.deleteObserver(this);
        super.onDestroy();
    }


    public void initExchangeActivity_not_paired() {
        initConnectionManager();
        setContentView(R.layout.exchange_not_paired);
    }

    public void initExchangeActivity_paired() {
        setContentView(R.layout.exchange_paired);
    }

    private void startFeedbackActivity(boolean success) {
        Intent feedback_intent = new Intent(this, FeedbackActivity.class);
        feedback_intent.putExtra("SUCCESS_FLAG", already_paired_flag);
        startActivity(feedback_intent);
    }

    private void initConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.addObserver(this);
        connectionManager.registerReceiver(this);
    }

    public void update(Observable observable, Object o) {
        switch ((Integer) o) {
            case MESSAGE.PAIRING:
                //TODO trying to pair
                break;
            case MESSAGE.PAIRED:
                initExchangeActivity_paired();
                break;
            case MESSAGE.ALREADY_PAIRED:
                initExchangeActivity_paired();
                break;
            case MESSAGE.CONNECTING:
                break;
            case MESSAGE.CONNECTED:

                break;
            case MESSAGE.DISCONNECTING:
                break;
            case MESSAGE.DISCONNECTED:
                break;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("keyCode: " + keyCode + " Event: " + event);
        if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_DOWN) {

          startFeedbackActivity(true);

        }
        return true;
    }

}
