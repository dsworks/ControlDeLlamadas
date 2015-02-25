package com.dsole.controldellamadas.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dsole.controldellamadas.providers.CallLogHelper;
import com.dsole.controldellamadas.classes.Ciclo;
import com.dsole.controldellamadas.ui.MainActivity;
import com.dsole.controldellamadas.R;

import java.util.Calendar;

public class ReceptorLlamadasConsulta extends BroadcastReceiver {
    private static Boolean consultandoLog = false;
    private static final int NOTIFICATION_ID = 0;

    public ReceptorLlamadasConsulta() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ControlDeLlamadas", "onReceive(): " + intent.getAction() + " consultando: " + consultandoLog.toString());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        if(sp.getBoolean("PREF_ENVIAR_NOTIFICACIONES", false)) {

            if (consultandoLog == false) {
                consultandoLog = true;

                consultarLimite(context);

                consultandoLog = false;
            }
        }
    }

    private void consultarLimite(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int limiteMinutos = Integer.parseInt(sp.getString("PREF_LIMITE_MINUTOS", "100"));
        int limiteAviso = Integer.parseInt(sp.getString("PREF_AVISO_LIMITE_MINUTOS", "90"));
        int primerDiaCiclo = Integer.parseInt(sp.getString("PREF_DIA_CICLO", "1"));

        String fechaInicio = Ciclo.fechaPrimerDiaCiclo(primerDiaCiclo, Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.YEAR));
        String fechaFinal = Ciclo.fechaUltimoDiaCiclo(primerDiaCiclo, Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));

        int i = CallLogHelper.getSobrepasadoLimite(context.getContentResolver(), limiteMinutos, limiteAviso, fechaInicio, fechaFinal);

        switch (i) {
            case 0:
                //No envíamos nada
                break;
            case 1:
                //enviamos notificación
                //todo saber si hay una notificacion ya enviada para no enviar mas
                enviarNotificacion(context, context.getString(R.string.mensaje_notificacion_llegando_limite));
                break;
            case 2:
                //enviamos notificación
                //todo saber si hay una notificacion ya enviada para no enviar mas
                enviarNotificacion(context, context.getString(R.string.mensaje_notificacion_limite_superado));
                break;
            default:
                //no envíamos nada
        }
    }

    private void enviarNotificacion(Context context, String texto) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Control de llamadas");
        mBuilder.setContentText(texto);
        mBuilder.setTicker("Control de llamadas");
        mBuilder.setAutoCancel(true);
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        // Definimos el PendingIntent y decimos que es de un solo uso
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        // Lo añadimos a nuestro constructor
        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
