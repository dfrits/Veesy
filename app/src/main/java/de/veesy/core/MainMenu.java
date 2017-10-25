package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import de.veesy.R;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }

    public void bShareClicked(View view) {
        startActivity(new Intent(this, ShareActivity.class));
    }

    public void bContactsClicked(View view) {
    }

    public void bSettingsClicked(View view) {
    }
}
