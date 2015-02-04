package com.dsole.controldellamadas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dsole on 28/01/2015.
 */
public class DFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.drawable.ic_launcher)
                        // Set Dialog Title
                .setTitle("!Bienvenido!")
                        // Set Dialog Message
                .setMessage("Rellene las preferencias de usuario para que la aplicación le envíe notificaciones de cuando esté a " +
                        "punto de llegar al limite de minutos al mes")

                        // Positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                }).create();
    }
}
