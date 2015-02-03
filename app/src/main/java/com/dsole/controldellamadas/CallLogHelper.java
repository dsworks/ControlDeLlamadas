package com.dsole.controldellamadas;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dsole on 29/12/2014.
 */
public class CallLogHelper {

    public static ArrayList<CallLog> getAllCallLogs(ContentResolver cr, String fechaInicio, String fechaFinal) {
        // reading all data in descending order according to DATE
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");

        fechaInicio = fechaInicio + " 00:00:00";
        fechaFinal = fechaFinal + " 23:59:59";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date date2 = null;
        try {
            date = sdf.parse(fechaInicio);
            date2 = sdf.parse(fechaFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dInicial = String.valueOf(date.getTime());
        String dFinal = String.valueOf(date2.getTime());

        String where = android.provider.CallLog.Calls.DATE + ">=" + dInicial + " AND " +
                android.provider.CallLog.Calls.DATE + "<=" + dFinal;

        Cursor cur = cr.query(callUri, null, where, null, strOrder);

        /*
        Cursor cur = cr.query(clientesUri,
                projection, //Columnas a devolver
                null,       //Condición de la query
                null,       //Argumentos variables de la query
                null);      //Orden de los resultados
        */


        ArrayList<CallLog> callLogs = new ArrayList<CallLog>();


        cur.moveToFirst();

        while (!cur.isAfterLast()) {
            CallLog callLog = new CallLog();
            callLog.setNumber(cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER)));
            callLog.setName(cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME)));

            String callDate = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
            String dateString = formatter.format(new Date(Long.parseLong(callDate)));
            callLog.setDate(dateString);

            callLog.setType(cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.TYPE)));
            callLog.setTime(cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION)));

            callLogs.add(callLog);
            cur.moveToNext();
        }

        return callLogs;
    }

    public static int getSobrepasadoLimite(ContentResolver cr, int minutosLimite, int minutosAviso, String fechaInicio, String fechaFinal) {

        int ret = 0;

        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");

        fechaInicio = fechaInicio + " 00:00:00";
        fechaFinal = fechaFinal + " 23:59:59";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date date2 = null;
        try {
            date = sdf.parse(fechaInicio);
            date2 = sdf.parse(fechaFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dInicial = String.valueOf(date.getTime());
        String dFinal = String.valueOf(date2.getTime());

        String where = android.provider.CallLog.Calls.DATE + ">=" + dInicial + " AND " +
                android.provider.CallLog.Calls.DATE + "<=" + dFinal + " AND " +
                android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.OUTGOING_TYPE;

        Cursor cur = cr.query(callUri, null, where, null, strOrder);

        cur.moveToFirst();

        int duracionSegundos = 0;

        while (!cur.isAfterLast()) {
            duracionSegundos += cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));

            cur.moveToNext();
        }

        Log.d("ControlDeLlamadas", "Duracion llamadas: " + Ciclo.format2Minutos(duracionSegundos) + "s Minutos aviso: " +
                String.valueOf(minutosAviso) + " Minutos límite: " + String.valueOf(minutosLimite));
        if (duracionSegundos >= minutosAviso * 60) {
            ret = 1;
            if (duracionSegundos >= minutosLimite * 60) {
                ret = 2;
            }
        }

        return ret;
    }

    public static int getSegundosConsumidos(ContentResolver cr, String fechaInicio, String fechaFinal) {

        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");

        fechaInicio = fechaInicio + " 00:00:00";
        fechaFinal = fechaFinal + " 23:59:59";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date date2 = null;
        try {
            date = sdf.parse(fechaInicio);
            date2 = sdf.parse(fechaFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dInicial = String.valueOf(date.getTime());
        String dFinal = String.valueOf(date2.getTime());

        String where = android.provider.CallLog.Calls.DATE + ">=" + dInicial + " AND " +
                android.provider.CallLog.Calls.DATE + "<=" + dFinal + " AND " +
                android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.OUTGOING_TYPE;

        Cursor cur = cr.query(callUri, null, where, null, strOrder);

        cur.moveToFirst();

        int duracionSegundos = 0;

        while (!cur.isAfterLast()) {
            duracionSegundos += cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));

            cur.moveToNext();
        }

        return duracionSegundos;
    }

}
