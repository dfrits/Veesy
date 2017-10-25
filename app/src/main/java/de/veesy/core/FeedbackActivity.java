package de.veesy.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Observable;
import java.util.Observer;

import de.veesy.R;
import de.veesy.connection.ConnectionManager;

/**
 * Created by dfritsch on 24.10.2017.
 * veesy.de
 * hs-augsburg
 */

/**
 * Wir müssen uns noch überlegen, wie wir der Feedback Activity mitteilen, ob alles geklappt hat. Macht das der ConnectionManager?
 *
 * An sich muss diese Activity gestartet werden und bereits wissen, ob alles funkioniert hat.
 *
 * Dann kann man der ImageView die entsprechende Source zuweißen und somit entweder einen grünen Haken oder ein rotes Kreuz reinladen
 *
 * Falls der ConnectionManager dies macht, dann muss dieser (sobald man sich gepaired hat) die ShareActivity beenden, einen Intent starten,
 * der sozusagen die Animation zu Datenaustausch anzeigt, und sobald dieser vollführt ist, die FeedbackActivity starten
 *
 */
public class FeedbackActivity extends Activity implements Observer {

    ImageView feedback_img = null;
    //ConnectionManager cm = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_act);

        feedback_img = findViewById(R.id.feedback_imgV);

        //cm = ConnectionManager.instance();
        //cm.addObserver(this);
    }

    protected void onDestroy(){
        //cm.deleteObserver(this);
        super.onDestroy();
    }


    public void bHomeClicked(View view){
        onDestroy();
        startActivity(new Intent(this, MainMenu.class));
    }

    public void bDetailsClicked(View view){
    }


    @Override
    public void update(Observable observable, Object o) {

    }


}



