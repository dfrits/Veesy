package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

/*
 * Wir müssen uns noch überlegen, wie wir der Feedback Activity mitteilen, ob alles geklappt hat. Macht das der ConnectionManager?
 *              ---> Martin: Feedback Activity wird dann über die update Methode von der ShareActivitiy gestartet (vorerst)
 *
 *
 * An sich muss diese Activity gestartet werden und bereits wissen, ob alles funkioniert hat.
 *
 * Dann kann man der ImageView die entsprechende Source zuweißen und somit entweder einen grünen Haken oder ein rotes Kreuz reinladen
 *
 * Falls der ConnectionManager dies macht, dann muss dieser (sobald man sich gepaired hat) die ShareActivity beenden, einen Intent starten,
 * der sozusagen die Animation zu Datenaustausch anzeigt, und sobald dieser vollführt ist, die FeedbackActivity starten
 *
 *              ---> Martin: würde vorerst wie oben beschrieben von der ShareActivity starten
 *              Animation zum Daten austausch könnte man in der ShareActivity über nen anderen Content laden, ähnlihc wie das Radar
 *              is aber nur ein Vorschlag, müsste man noch drüber nachdenken
 *
 */
public class FeedbackActivity extends Activity implements Observer {
    public static final String FAILURE = "FAILURE";
    public static final String SUCCESS = "SUCCESS";

    ImageView feedback_img = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_act);

        feedback_img = findViewById(R.id.feedback_imgV);
        if (getIntent().getStringExtra(FAILURE).equals(FAILURE)) {
            //feedback_img.setImageDrawable(R.drawable.); TODO X-Bild einfügen
        }

    }

    protected void onDestroy(){
        super.onDestroy();
    }


    public void bHomeClicked(View view){
        startActivity(new Intent(this, MainMenu.class));
        finish();
    }

    public void bDetailsClicked(View view){
    }


    @Override
    public void update(Observable observable, Object o) {

    }
}



