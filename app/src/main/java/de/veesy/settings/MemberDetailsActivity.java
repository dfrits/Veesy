package de.veesy.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
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
        setContentView(R.layout.about_team_view);

        init();
    }

    private void init() {
        String[] details = new String[0];
        // Entweder als Hintergrund oder eigenes Bild. Je nach dem umkommentieren (hier und im layout)
//        ScrollView background = findViewById(R.id.team_member_details_bg);
        ImageView image = findViewById(R.id.member_img);
        switch (getIntent().getIntExtra(EXTRA_KEY, 4)) {
            case 0:
                details = getResources().getStringArray(R.array.details_vivi);
//                background.setBackgroundResource(R.drawable.about_vivi);
                image.setImageResource(R.drawable.about_vivi);
                break;
            case 1:
                details = getResources().getStringArray(R.array.details_martin);
//                background.setBackgroundResource(R.drawable.about_martin);
                image.setImageResource(R.drawable.about_martin);
                break;
            case 2:
                details = getResources().getStringArray(R.array.details_tianyi);
//                background.setBackgroundResource(R.drawable.about_tianyi);
                image.setImageResource(R.drawable.about_tianyi);
                break;
            case 3:
                details = getResources().getStringArray(R.array.details_daniel);
//                background.setBackgroundResource(R.drawable.about_daniel);
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