package com.dsole.controldellamadas.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dsole.controldellamadas.listeners.MyPhoneStateListener;

public class ReceptorLlamadas extends BroadcastReceiver {

    public ReceptorLlamadas() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ControlDeLlamadas", "onReceive(): " + intent.getAction());

        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        MyPhoneStateListener PhoneListener = new MyPhoneStateListener(context);
        tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        Log.d("ControlDeLlamadas",intent.getStringExtra(TelephonyManager.EXTRA_STATE));

        //
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_RINGING)) {

            // Phone number
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            // Ringing state
            // This code will execute when the phone has an incoming call
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            // This code will execute when the call is answered or disconnected
            Log.d("ControlDeLlamadas","llamada contestada o desconectado");
        }

    }
}
