package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;
import de.veesy.util.Util;

import static de.veesy.core.FeedbackActivity.SUCCESS_FLAG;


/**
 * Created by dfritsch on 17.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class ExchangeActivity extends Activity implements Observer {

    public static final String ALREADY_PAIRED = "ALREADY_PAIRED";

    private ConnectionManager connectionManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initConnectionManager();

        boolean already_paired_flag = getIntent().getBooleanExtra(ALREADY_PAIRED, false);
        if (already_paired_flag) runExchangeActivity_paired();
        else runExchangeActivity_pairing();
    }


    private void initConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.addObserver(this);
        connectionManager.registerReceiver(this);
    }

    private void runExchangeActivity_not_paired() {
        setContentView(R.layout.exchange_not_paired);
    }

    private void runExchangeActivity_paired() {
        Util.setContentViewOnUiThread(this, R.layout.exchange_paired);
        runExchangeAnimation();
    }

    private void runExchangeActivity_pairing() {
        Util.setContentViewOnUiThread(this, R.layout.exchange_pairing);
        runPairingAnimation();
    }

    private void runExchangeAnimation() {
        ImageView exchangeAnimationView = findViewById(R.id.iVExchangeAnimation);
        Animation exchange_animation = AnimationUtils.loadAnimation(this, R.anim.rotate_exchange);
        Util.runAnimationOnUiThread(this, exchangeAnimationView, exchange_animation);
    }

    private void runPairingAnimation() {
        ImageView pairingAnimationView = findViewById(R.id.iVPairingAnimation);
        Animation pairing_animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_out);
        Util.runAnimationOnUiThread(this, pairingAnimationView, pairing_animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connectionManager != null) {
            connectionManager.unregisterReceiver(this);
            connectionManager.deleteObserver(this);
            connectionManager.btCloseConnection();
        }
    }

    private void startFeedbackActivity(boolean success) {
        Intent feedback_intent = new Intent(this, FeedbackActivity.class);
        feedback_intent.putExtra(SUCCESS_FLAG, success);
        startActivity(feedback_intent);
        if (success) finish();
    }

    public void bPairClicked(View view) {
        connectionManager.retryPairing();
    }

    public void bShareClicked(View view) {
        startActivity(new Intent(this, ShareActivity.class));
        finish();
    }

    public void bCancelClicked(View view) {
        startActivity(new Intent(this, ShareActivity.class));
        finish();
    }

    public void update(Observable observable, Object o) {
        switch ((Integer) o) {
            case MESSAGE.PAIRING:
                runExchangeActivity_pairing();
                break;
            case MESSAGE.PAIRED:
                runExchangeActivity_paired();
                connectionManager.startConnectionTimeOutHandler();
                break;
            case MESSAGE.ALREADY_PAIRED:
                runExchangeActivity_paired();
                break;
            case MESSAGE.NOT_PAIRED:
                runExchangeActivity_not_paired();
                break;
            case MESSAGE.CONNECTED:
                System.out.println("MESSAGE.CONNECTED in Exchange Act");
                if (!connectionManager.btIsInClientMode()) {
                    connectionManager.btSendData();
                }
                break;
            case MESSAGE.RESPOND_AS_CLIENT:
                System.out.println("MESSAGE.RESPOND in Exchange Act");
                connectionManager.btSendData();
                startFeedbackActivity(true);
                break;
            case MESSAGE.DATA_TRANSMISSION_SUCCESSFUL:
                System.out.println("MESSAGE.DATA_SUCCESS in Exchange Act");
                connectionManager.btCloseConnection();
                startFeedbackActivity(true);
                break;
            case MESSAGE.DATA_TRANSMISSION_FAILED:
                System.out.println("MESSAGE.DATA_FAILED in Exchange Act");
                //connectionManager.btCloseConnection();
                startFeedbackActivity(false);
                break;
            case MESSAGE.CONNECTION_ERROR:
                connectionManager.retryConnecting();
                break;
        }
    }

    //debug kram
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_DOWN) {
        }
        return true;
    }
}
