/*
package de.veesy.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.veesy.R;

*/
/**
 * Created by dfritsch on 08.11.2017.
 * veesy.de
 * hs-augsburg
 * <p>
 * FrageDialog mit beliebigen Titel und 2 Bestätigen-Button. Wird einer der beiden Button gedrückt,
 * wird die entsprechende Methode vom Callback aufgerufen.
 * <p>
 * Funktion:<br>
 * QuestionDialog dialog = AlertDialog.newInstance("message", new QuestionDialogCallback(this));<br>
 * dialog.show();
 * <p>
 * Reagieren kann man dann, indem man das Interface implementiert.
 *//*


public class QuestionDialog extends DialogFragment implements View.OnClickListener {
    private static String message;
    private static QuestionDialogCallback callback;

    public interface QuestionDialogCallback {
        void positiveButtonClicked();

        void negativeButtonClicked();
    }

    */
/**
     * Erstellt einen neuen AlertDialog.
     * @param message Nachricht, die angezeigt werden soll
     * @return Neuen AlertDialog
     *//*

    static QuestionDialog newInstance(String message, QuestionDialogCallback callback) {
        QuestionDialog.message = message;
        QuestionDialog.callback = callback;
        return new QuestionDialog();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_1) {
            callback.positiveButtonClicked();
            dismiss();
        } else if (view.getId() == R.id.button_2) {
            callback.negativeButtonClicked();
            dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog_wearable, container, false);
        TextView messageText = view.findViewById(R.id.text1);
        messageText.setText(message);
        View button = view.findViewById(R.id.button_1);
        button.setOnClickListener(this);
        button = view.findViewById(R.id.button_2);
        button.setOnClickListener(this);
        return view;
    }
}
*/
