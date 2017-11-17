package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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


    private ConnectionManager connectionManager = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExchangeActivity_not_paired();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        connectionManager.unregisterReceiver(this);
        connectionManager.deleteObserver(this);
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
        feedback_intent.putExtra("SUCCESS_FLAG", success);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // nur zu debug zwecken
                startFeedbackActivity(true);
                break;
        }
        return super.onTouchEvent(event);
    }

}
