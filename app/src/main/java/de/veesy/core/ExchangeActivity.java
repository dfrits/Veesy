package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;

import static de.veesy.core.FeedbackActivity.SUCCESS_FLAG;


/**
 * Created by dfritsch on 17.11.2017.
 * veesy.de
 * hs-augsburg
 */

public class ExchangeActivity extends Activity implements Observer {

    public static final String ALREADY_PAIRED = "ALREADY_PAIRED";

    private boolean already_paired_flag = true;
    private ConnectionManager connectionManager;
    private Animation exchange_animation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConnectionManager();

        already_paired_flag = getIntent().getBooleanExtra(ALREADY_PAIRED, false);
        if (already_paired_flag) {
            initExchangeActivity_paired();
        } else {
            initExchangeActivity_not_paired();
        }
    }

    private void initExchangeActivity_not_paired() {
        setContentView(R.layout.exchange_not_paired);
    }

    private void initExchangeActivity_paired() {
        setContentView(R.layout.exchange_paired);
        initExchangeAnimation();
    }

    private void initExchangeAnimation() {
        ImageView exchangeAnimationView = findViewById(R.id.iVExchangeAnimation);
        exchange_animation = AnimationUtils.loadAnimation(this, R.anim.rotate_exchange);
        exchangeAnimationView.startAnimation(exchange_animation);
    }

    private void initPairingAnimation() {
        final ImageView pairingAnimationView = findViewById(R.id.iVPairingAnimation);
        final Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeOutAnimation when fadeInAnimation ends (continue)
                pairingAnimationView.startAnimation(fadeOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeInAnimation when fadeOutAnimation ends (repeat)
                pairingAnimationView.startAnimation(fadeInAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });
        pairingAnimationView.startAnimation(fadeInAnimation);
    }

    private void initExchangeActivity_pairing() {
        setContentView(R.layout.exchange_pairing);
    }

    private void initConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.addObserver(this);
        connectionManager.registerReceiver(this);
    }

    public void update(Observable observable, Object o) {
        switch ((Integer) o) {
            case MESSAGE.PAIRING:
                initExchangeActivity_pairing();

                break;
            case MESSAGE.PAIRED:
                initExchangeActivity_paired();
                break;
            case MESSAGE.ALREADY_PAIRED:
                initExchangeActivity_paired();
                break;
            case MESSAGE.NOT_PAIRED:
                initExchangeActivity_not_paired();
                break;
            case MESSAGE.CONNECTING:
                break;
            case MESSAGE.CONNECTED:
                connectionManager.btSendData("Hallo i bims");
                break;
            case MESSAGE.DISCONNECTING:
                break;
            case MESSAGE.DISCONNECTED:
                break;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 265 && event.getAction() == KeyEvent.ACTION_DOWN) {
            startFeedbackActivity(true);
        }
        return true;
    }

    protected void onDestroy() {
        if (connectionManager != null) {
            connectionManager.unregisterReceiver(this);
            connectionManager.deleteObserver(this);
        }
        super.onDestroy();
    }

    private void startFeedbackActivity(boolean success) {
        Intent feedback_intent = new Intent(this, FeedbackActivity.class);
        feedback_intent.putExtra(SUCCESS_FLAG, already_paired_flag);
        startActivity(feedback_intent);
        finish();
    }

    public void bPairClicked(View view) {
        connectionManager.retryPairing();
    }

    public void bShareClicked(View view) {
        startActivity(new Intent(this, ShareActivity.class));
        finish();
    }

    public void bCancel(View view) {
        startActivity(new Intent(this, ShareActivity.class));
        finish();
    }
}
