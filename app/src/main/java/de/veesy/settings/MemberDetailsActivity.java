package de.veesy.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import de.veesy.R;

/**
 * Created by Daniel on 02.01.2018.
 * veesy.de
 * hs-augsburg
 */

public class MemberDetailsActivity extends Activity {
    static final String EXTRA_KEY = "EXTRA_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.about_team_view);

        init();
    }

    private void init() {
        String[] details = new String[0];
        ImageView image = findViewById(R.id.member_img);
        switch (getIntent().getIntExtra(EXTRA_KEY, 4)) {
            case 0:
                details = getResources().getStringArray(R.array.details_vivi);
                image.setImageResource(R.drawable.about_vivi);
                break;
            case 1:
                details = getResources().getStringArray(R.array.details_martin);
                image.setImageResource(R.drawable.about_martin);
                break;
            case 2:
                details = getResources().getStringArray(R.array.details_tianyi);
                image.setImageResource(R.drawable.about_tianyi);
                break;
            case 3:
                details = getResources().getStringArray(R.array.details_daniel);
                image.setImageResource(R.drawable.about_daniel);
                break;
        }
        if (details.length != 0) {
            TextView view = findViewById(R.id.tFirstName);
            view.setText(details[0]);
            view = findViewById(R.id.tLastName);
            view.setText(details[1]);
            view = findViewById(R.id.tOccupation);
            view.setText(details[2]);
            view = findViewById(R.id.tDegree);
            view.setText(details[3]);
            view = findViewById(R.id.tUni);
            view.setText(details[4]);
            view = findViewById(R.id.tSlogan);
            view.setText(details[5]);
        }
    }

    public void bReturnClicked(View view) {
        finish();
    }
}
