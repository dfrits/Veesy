package de.veesy.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

public class ShareActivity extends Activity implements Observer {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share);


    }

    public void update(Observable o, Object arg) {

    }
}
