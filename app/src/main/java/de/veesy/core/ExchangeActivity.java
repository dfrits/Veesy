package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
    private Runnable pairing_animation;
    private AlphaAnimation pairing_alpha_animation;
    private ImageView animationView;
    private RelativeLayout animationFrame;
    private float x1, x2;

    private final boolean[] stopit = {false};

    private static final int MIN_DISTANCE = 150;


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
        initPairingAnimation();
        //startAnimation();
    }

    private void initPairingAnimation() {
        animationFrame = findViewById(R.id.animation_frame);
        /*final ImageView view1 = findViewById(R.id.pairing_animation_view_1);
        final ImageView view2 = findViewById(R.id.pairing_animation_view_2);
        pairing_animation = new Runnable() {
            @Override
            public void run() {
                while (!stopit[0]) {
                    try {
                        view1.setVisibility(View.VISIBLE);
                        Thread.sleep(500);
                        view2.setVisibility(View.VISIBLE);
                        Thread.sleep(500);
                        view1.setVisibility(View.INVISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        stopit[0] = true;
                    }
                }
            }
        };*/
        XmlResourceParser anim = getResources().getAnimation(R.anim.blink_pairing);
        pairing_alpha_animation = new AlphaAnimation(this, anim);
        animationView = findViewById(R.id.pairing_animation_view_1);
        animationView.startAnimation(pairing_alpha_animation);
    }

    private void initExchangeActivity_paired() {
        setContentView(R.layout.exchange_paired);
        initExchangeAnimation();
    }

    private void initExchangeAnimation() {
        ImageView exchangeAnimationView = findViewById(R.id.exchange_animation_view);
        exchange_animation = AnimationUtils.loadAnimation(this, R.anim.rotate_exchange);
        exchangeAnimationView.startAnimation(exchange_animation);
    }

    /*private void initExchangeActivity_pairing() {
        setContentView(R.layout.exchange_pairing);
    }*/

    private void initConnectionManager() {
        connectionManager = ConnectionManager.instance();
        connectionManager.addObserver(this);
        connectionManager.registerReceiver(this);
    }

    public void update(Observable observable, Object o) {
        switch ((Integer) o) {
            case MESSAGE.PAIRING:
                //initExchangeActivity_pairing();
                //startAnimation();
                animationView.startAnimation(pairing_alpha_animation);
                break;
            case MESSAGE.PAIRED:
                //stopAnimation();
                animationView.clearAnimation();
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
                break;
            case MESSAGE.DISCONNECTING:
                break;
            case MESSAGE.DISCONNECTED:
                break;
        }
    }

    private void startAnimation() {
        stopit[0] = false;
        animationFrame.setVisibility(View.VISIBLE);
        runOnUiThread(pairing_animation);
    }

    private void stopAnimation() {
        stopit[0] = true;
        animationFrame.setVisibility(View.INVISIBLE);
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
