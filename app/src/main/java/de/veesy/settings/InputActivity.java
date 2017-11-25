package de.veesy.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.veesy.R;

import static de.veesy.util.Constants.INTENT_RESULT;

/**
 * Created by dfritsch on 21.11.2017.
 * veesy.de
 * hs-augsburg
 * <p>
 * Die Klasse ist zuständig für die Verwaltung einer Eingabe. Die einzelnen Elemente sind
 * individuell anpassbar. Einfach beim Starten der Activity den Text mit
 * {@link Intent#putExtra(String, String) putExtra()} mitgeben. Dabei aber beachten die
 * entsprechenden Namen zu verwenden.
 * <p>
 * Starten die Activity dann mit {@link Activity#startActivityForResult(Intent, int)
 * startActivityForResult(intent , INPUT_ACTIVITY_REQUEST_CODE);}
 * <p>
 * Das Ergebnis erhält man dann in der {@link Activity#onActivityResult onActivityResult-Methode()}.
 * Dort kann dann auf den RequestCode reagiert werden. Beispiel:
 * {@link de.veesy.contacts.ContactViewActivity#onActivityResult(int, int, Intent)
 * ContactViewActivity.onActivityResult()}
 */

public class InputActivity extends Activity {
    public static final String INPUT_TYPE_NUMBER = "INPUT_TYPE_NUMBER";
    public static final String INPUT_TYPE_TEXT = "INPUT_TYPE_TEXT";

    private static final String BUTTON_TEXT = "BUTTON_TEXT";
    private static final String INFO_TEXT = "INFO_TEXT";
    private static final String INPUT_TYPE = "INPUT_TYPE";

    private Intent intent;
    private EditText inputField;
    private Button button;

    public static Intent getIntent(Context context, int infoText,
                                   int buttonText, String inputType) {
        Intent intent = new Intent(context, InputActivity.class);
        intent.putExtra(INFO_TEXT, infoText);
        intent.putExtra(BUTTON_TEXT, buttonText);
        intent.putExtra(INPUT_TYPE, inputType);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_input);

        TextView infoText = findViewById(R.id.tInfo);
        inputField = findViewById(R.id.inputField);
        button = findViewById(R.id.bSave);

        intent = getIntent();

        // Den Text des Buttons ändern, falls einer mitgegeben wurde
        int extraButtonText = intent.getIntExtra(BUTTON_TEXT, 0);
        if (extraButtonText != 0) {
            button.setText(extraButtonText);
        }

        // Text des InfoTexts setzen
        int extraInfoText = intent.getIntExtra(INFO_TEXT, 0);
        if (extraInfoText != 0) {
            infoText.setText(extraInfoText);
        }

        //Listener an das Textfeld hängen, sodass der Button deaktiviert wird,
        //wenn das Textfeld leer ist
        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().trim().length() == 0) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //InputType vom InputFeld setzen
        String inputType = intent.getStringExtra(INPUT_TYPE);
        if (inputType != null && inputType.equals(INPUT_TYPE_NUMBER)) {
            inputField.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (inputType != null && inputType.equals(INPUT_TYPE_TEXT)) {
            inputField.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        } else {
            inputField.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    /**
     * Beendet die Activity und gibt den Wert vom Textfeld an die aufrufende Activity zurück.
     * @param view .
     */
    public void bSaveClicked(View view) {
        intent.putExtra(INTENT_RESULT, inputField.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
