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
 * AlertDialog mit beliebigen Titel und einem Bestätigen-Button. Wird der Button gedrückt, wird
 * der Dialog geschlossen. Darauf kann reagiert werden mit einem OnDissmissListener().
 * <p>
 * Funktion:<br>
 * AlertDialog dialog = AlertDialog.newInstance("message");<br>
 * dialog.show();
 * <p>
 * Reagieren kann man z.B. so:<br>
 * dialog.onDismiss(new DialogInterface() {<br>
 *      '@Override' public void cancel() {}<br>
 *      '@Override' public void dismiss() {}<br>
 * });
 *//*


public class AlertDialog extends DialogFragment implements View.OnClickListener {
    private static String message;

    */
/**
     * Erstellt einen neuen AlertDialog.
     * @param message Nachricht, die angezeigt werden soll
     * @return Neuen AlertDialog
     *//*

    public static AlertDialog newInstance(String message) {
        AlertDialog.message = message;
        return new AlertDialog();
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog_wearable, container, false);
        TextView messageText = view.findViewById(R.id.text1);
        messageText.setText(message);
        View button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
        return view;
    }
}
*/
