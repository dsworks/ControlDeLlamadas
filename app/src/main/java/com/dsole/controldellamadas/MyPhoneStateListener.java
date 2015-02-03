package com.dsole.controldellamadas;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dsole on 24/12/2014.
 */
public class MyPhoneStateListener extends PhoneStateListener {
    Context context;

    public MyPhoneStateListener(Context context) {
        this.context = context;
    }

    public void onCallStateChanged(int state, String incomingNumber) {

        Log.d("ControlDeLlamadas", state + "   incoming no:" + incomingNumber);

        /*
        Si me llaman:
        state = 1 llamando y incomingNumber lleno
        state = 2 llamada cogida y incomingNumber lleno
        state = 0 fin llamada y incomingNumber lleno

        Si llamo:
        state = 1
        state = 2 llamando y llamada cogida? incomingNumber lleno
        state = 0 fin llamada y incomingNumber lleno



         */

        if (state == 2) {
            Log.d("ControlDeLlamadas", "Enviado Broadcast para consultar");
            Intent i = new Intent("com.dsole.controldellamadas.CONSULTA_LIMITE");
            context.sendBroadcast(i);
            //String msg = "New Phone Call Event. Incomming Number : " + incomingNumber;

            //int duration = Toast.LENGTH_LONG;
            //Toast toast = Toast.makeText(this.context, msg, duration);
            //toast.show();

            /*
            Intent intent2open = new Intent(context, MainActivity.class);
            intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            String name = "KEY";
            String value = "El número " + incomingNumber + " te está llamando!";
            intent2open.putExtra(name, value);
            context.startActivity(intent2open);
            */
        }
    }
}
