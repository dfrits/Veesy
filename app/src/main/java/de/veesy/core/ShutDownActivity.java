package de.veesy.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;
import de.veesy.connection.MESSAGE;
import de.veesy.util.Util;

/**
 * Created by Daniel on 08.01.2018.
 * veesy.de
 * hs-augsburg
 */

public class ShutDownActivity extends Activity implements Observer {
    ConnectionManager connectionManager = ConnectionManager.instance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shut_down);

        Util.showToast(this, "Shutdown", Toast.LENGTH_SHORT);
        connectionManager.addObserver(this);
        connectionManager.unpairAllDevices();
        connectionManager.setBackOriginalDeviceName();
    }

    @Override
    public void update(Observable observable, Object o) {
        if ((Integer) o == MESSAGE.READY_TO_SHUTDOWN) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        // We need to do this because somehow it happens that the connection manager is still alive
        connectionManager.finish();
        super.onDestroy();
    }
}
