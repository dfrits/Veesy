package de.veesy.introduction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import java.util.List;
import java.util.Vector;

import de.veesy.R;

/**
 * Created by dfritsch on 22.12.2017.
 * veesy.de
 * hs-augsburg
 *
 * Verwaltet die einzelnen Seiten der Einführung.
 */

public class IntroductionActivity extends FragmentActivity {
    private PagerAdapter pagerAdapter;
    private ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.introduction);
        initialisePaging();
    }

    /**
     * Fügt die einzelnen Frames zum PagerAdapter hinzu. Die Layouts werden dann von den jeweiligen
     * Tabs selber verwaltet. Tab6 ist die letzte und eine leere Seite, damit das Beenden
     * automatisch erfolgen kann. Einfach vor Tab6 einen neuen Frame hinzufügen oder entfernen. Rest
     * erfolgt automatisch.
     */
    private void initialisePaging() {
        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this, Tab1.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab2.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab3.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab4.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab5.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab6.class.getName()));
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

        pager = findViewById(R.id.introPager);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float posOff, int posOffPix) {}

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {
                // Wurde die letzte Seite erreicht, wird automatisch beendet
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int currentItem = pager.getCurrentItem();
                    if (currentItem == pagerAdapter.getCount() - 1) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        });
    }

    /**
     * Wird von den einzelnen Frames aufgerufen und schaltet einen Frame weiter, solang noch nicht
     * der letzte erreicht wurde. Die onClick-Methode muss in den Layout der Frames angegeben
     * unbedingt werden.
     * @param view .
     */
    public void onPagerClicked(View view) {
        int currentItem = pager.getCurrentItem();
        if (currentItem < pagerAdapter.getCount() - 1) {
            pager.setCurrentItem(currentItem + 1);
        }
    }
}
