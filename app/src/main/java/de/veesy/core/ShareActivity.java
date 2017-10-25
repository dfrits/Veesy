package de.veesy.core;

import android.app.Activity;
import android.os.Bundle;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;

/**
 * Created by Martin on 24.10.2017.
 */

public class ShareActivity extends Activity implements Observer {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_act);
    }



    public void update(Observable o, Object arg) {

    }
}
