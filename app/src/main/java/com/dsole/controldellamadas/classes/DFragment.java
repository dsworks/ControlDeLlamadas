package com.dsole.controldellamadas.classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.dsole.controldellamadas.R;

/**
 * Created by dsole on 28/01/2015.
 */
public class DFragment extends DialogFragment {
    private Context context;

    public void setContext (Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.drawable.ic_launcher)
                        // Set Dialog Title
                .setTitle(context.getString(R.string.bienvenido))
                        // Set Dialog Message
                .setMessage(context.getString(R.string.mensaje_bienvenida))

                        // Positive button
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                }).create();
    }
}
