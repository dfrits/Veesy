package de.veesy.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import de.veesy.R;

/**
 * Created by Daniel on 02.01.2018.
 * veesy.de
 * hs-augsburg
 */

public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.about_us);
    }

    public void bReturnClicked(View view) {
        finish();
    }

    public void bViviClicked(View view) {
        showMemberDetails(0);
    }

    public void bMartinClicked(View view) {
        showMemberDetails(1);
    }

    public void bTianyiClicked(View view) {
        showMemberDetails(2);
    }

    public void bDanielClicked(View view) {
        showMemberDetails(3);
    }

    /**
     * Startet die Detailansicht.
     * @param pos Welches Teammitglied angezeigt werden soll
     */
    private void showMemberDetails(int pos) {
        Intent intent = new Intent(this, MemberDetailsActivity.class);
        intent.putExtra(MemberDetailsActivity.EXTRA_KEY, pos);
        startActivity(intent);
    }
}
