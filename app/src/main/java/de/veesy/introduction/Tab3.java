package de.veesy.introduction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.veesy.R;

/**
 * Created by dfritsch on 22.12.2017.
 * veesy.de
 * hs-augsburg
 *
 * Seite 3 der Einführung.
 */

public class Tab3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.intro_tab_3, container, false);
    }
}
