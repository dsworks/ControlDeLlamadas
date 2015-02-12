package com.dsole.controldellamadas.providers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.dsole.controldellamadas.classes.CallLog;
import com.dsole.controldellamadas.classes.Ciclo;
import com.dsole.controldellamadas.classes.Contacto;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by dsole on 29/12/2014.
 */
public class CallLogHelper {

    public static ArrayList<CallLog> getCallLogs(ContentResolver cr, String fechaInicio, String fechaFinal,
                                                 String numero, Boolean realizadas, Boolean recibidas,
                                                 Boolean perdidas, int minutosDesde, int minutosHasta) {
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

        String tipo = "";

        if (realizadas) {
            tipo += "X " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.OUTGOING_TYPE;
        }
        if (recibidas) {
            tipo += "X " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.INCOMING_TYPE;
        }
        if (perdidas) {
            tipo += "X " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.MISSED_TYPE;
        }

        if(!tipo.equals("")) {
            int count = countOccurrences(tipo, 'X');

            if(count == 1) {
                tipo = tipo.replace('X', ' ');
            }
            else {
                String aux = "";

                aux = tipo.substring(tipo.indexOf("X", 1));

                aux = aux.replace("X" , " OR " );

                tipo = tipo.substring(0, tipo.indexOf("X", 1)) + aux;
                tipo = tipo.replace('X', ' ');
            }

            where += " AND (" + tipo + ") ";
        }

        where += " AND " + android.provider.CallLog.Calls.DURATION + ">=" + minutosDesde;
        where += " AND " + android.provider.CallLog.Calls.DURATION + "<=" + minutosHasta;

        if(!numero.equals("")) {
            where += " AND " + android.provider.CallLog.Calls.NUMBER + "==" + numero.replaceAll("\\s","");
        }

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
            callLog.setTime(cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION)));

            String contactId = getContactId(cr, cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER)));

            if (contactId != "") {
                Bitmap photo = null;

                try {
                    long id = Long.valueOf(contactId).longValue();
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(cr,
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id), true);

                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                        callLog.setImagen(photo);

                        assert inputStream != null;
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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

    public static int[] getTotalTipoLlamadas(ContentResolver cr, String fechaInicio, String fechaFinal) {

        int[] llamadas = new int[3];
        int entrantes = 0;
        int salientes = 0;
        int perdidas = 0;

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

        cur.moveToFirst();

        while (!cur.isAfterLast()) {
            int tipo = cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.TYPE));

            switch (tipo) {
                case android.provider.CallLog.Calls.INCOMING_TYPE:
                    entrantes++;
                    break;
                case android.provider.CallLog.Calls.OUTGOING_TYPE:
                    salientes++;
                    break;
                case android.provider.CallLog.Calls.MISSED_TYPE:
                    perdidas++;
                    break;
                default:
            }
            cur.moveToNext();
        }

        llamadas[0] = entrantes;
        llamadas[1] = salientes;
        llamadas[2] = perdidas;

        return llamadas;
    }

    public static String[] getTotalMinutos(ContentResolver cr, String fechaInicio, String fechaFinal) {

        String[] minutos = new String[3];
        int segundosEntrantes = 0;
        int segundosSalientes = 0;

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
                android.provider.CallLog.Calls.DATE + "<=" + dFinal + " AND (" +
                android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.OUTGOING_TYPE + " OR " +
                android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.INCOMING_TYPE + ")";

        Cursor cur = cr.query(callUri, null, where, null, strOrder);

        cur.moveToFirst();

        while (!cur.isAfterLast()) {
            int tipo = cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.TYPE));

            switch (tipo) {
                case android.provider.CallLog.Calls.INCOMING_TYPE:
                    segundosEntrantes += cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
                    break;
                case android.provider.CallLog.Calls.OUTGOING_TYPE:
                    segundosSalientes += cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
                    break;
                default:
            }
            cur.moveToNext();
        }

        minutos[0] = Ciclo.formatSegundos(segundosEntrantes);
        minutos[1] = Ciclo.formatSegundos(segundosSalientes);

        return minutos;
    }

    public static Contacto getTopContacto(ContentResolver cr, String fechaInicio, String fechaFinal, String tipo) {
        String strOrder = android.provider.CallLog.Calls.NUMBER + " DESC";
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

        if (tipo == "SALIENTE") {
            where += " AND " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.OUTGOING_TYPE;
        } else {
            where += " AND " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.INCOMING_TYPE;
        }

        Cursor cur = cr.query(callUri, null, where, null, strOrder);


        ArrayList<Contacto> contactos = new ArrayList<Contacto>();

        //Contacto contacto = new Contacto();

        cur.moveToFirst();

        String numeroUltimo = "primeraVez";
        String numero = "";
        String nombre = "";
        int segundos = 0;
        int numLlamadas = 0;
        while (!cur.isAfterLast()) {

            numero = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER));

            if (numeroUltimo != "primeraVez") {
                if (!numero.equals(numeroUltimo)) {
                    Contacto contacto = new Contacto();

                    contacto.setNumero(numeroUltimo);
                    contacto.setNombre(nombre);
                    contacto.setTotalSegundos(segundos);
                    contacto.setTotalLlamadas(numLlamadas);

                    contactos.add(contacto);

                    numLlamadas = 0;
                    segundos = 0;
                }
            }

            nombre = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            segundos += cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            numLlamadas++;

            numeroUltimo = numero;
            cur.moveToNext();

            if (cur.isAfterLast()) {
                Contacto contacto = new Contacto();

                contacto.setNumero(numeroUltimo);
                contacto.setNombre(nombre);
                contacto.setTotalSegundos(segundos);
                contacto.setTotalLlamadas(numLlamadas);

                contactos.add(contacto);
            }
        }


        Collections.sort(contactos, new Contacto.TotalLlamadasComparator());

        Contacto contactoEncontrado = new Contacto();

        if (contactos.size() != 0) {

            contactoEncontrado = contactos.get(0);
            String contactId = getContactId(cr, contactoEncontrado.getNumero());

            if (contactId != "") {
                Bitmap photo = null;

                try {
                    //Cursor cur2 = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    //        null, null, null,null);

                    long id = Long.valueOf(contactId).longValue();
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(cr,
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id), true);

                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                        contactoEncontrado.setImagen(photo);

                        assert inputStream != null;
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            contactoEncontrado.setTotalMinutos(Ciclo.formatSegundos(contactoEncontrado.getTotalSegundos()));
        } else {
            contactoEncontrado.setNombre("");
            contactoEncontrado.setNumero("");
            contactoEncontrado.setTotalMinutos(Ciclo.formatSegundos(0));
        }

        return contactoEncontrado;
    }

    public static Contacto getTopContactoMasMinutos(ContentResolver cr, String fechaInicio, String fechaFinal) {
        String strOrder = android.provider.CallLog.Calls.DURATION + " DESC";
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

        Contacto contacto = new Contacto();

        cur.moveToFirst();

        while (!cur.isAfterLast()) {

            contacto.setNombre(cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME)));
            contacto.setNumero(cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER)));
            contacto.setTotalSegundos(cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION)));
            break;
        }

        if (contacto.getNumero() != "") {

            //saber el número de llamadas de un contacto

            //int totalLlamadasRealizadas = getTotalLlamadasContacto(cr, fechaInicio, fechaFinal, contacto.getNumero(), "SALIENTE");
            //int totalLlamadasRecibidas = getTotalLlamadasContacto(cr, fechaInicio, fechaFinal, contacto.getNumero(), "ENTRANTE");

            //contacto.setTotalLlamadas(totalLlamadasRealizadas+totalLlamadasRecibidas);

            String contactId = getContactId(cr, contacto.getNumero());

            if (contactId != "") {
                Bitmap photo = null;

                try {
                    //Cursor cur2 = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    //        null, null, null,null);

                    long id = Long.valueOf(contactId).longValue();
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(cr,
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id), true);

                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                        contacto.setImagen(photo);

                        assert inputStream != null;
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            contacto.setTotalMinutos(Ciclo.formatSegundos(contacto.getTotalSegundos()));
        } else {
            contacto.setNumero("");
            contacto.setNombre("");
            contacto.setTotalMinutos(Ciclo.formatSegundos(0));
        }

        return contacto;
    }

    public static Contacto getTopContactoMasRato(ContentResolver cr, String fechaInicio, String fechaFinal) {
        String strOrder = android.provider.CallLog.Calls.NUMBER + " DESC";
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


        ArrayList<Contacto> contactos = new ArrayList<Contacto>();

        //Contacto contacto = new Contacto();

        cur.moveToFirst();

        String numeroUltimo = "primeraVez";
        String numero = "";
        String nombre = "";
        int segundos = 0;
        int numLlamadas = 0;
        while (!cur.isAfterLast()) {

            numero = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER));

            if (numeroUltimo != "primeraVez") {
                if (!numero.equals(numeroUltimo)) {
                    Contacto contacto = new Contacto();

                    contacto.setNumero(numeroUltimo);
                    contacto.setNombre(nombre);
                    contacto.setTotalSegundos(segundos);
                    contacto.setTotalLlamadas(numLlamadas);

                    contactos.add(contacto);

                    numLlamadas = 0;
                    segundos = 0;
                }
            }

            nombre = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            segundos += cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            numLlamadas++;

            numeroUltimo = numero;
            cur.moveToNext();

            if (cur.isAfterLast()) {
                Contacto contacto = new Contacto();

                contacto.setNumero(numeroUltimo);
                contacto.setNombre(nombre);
                contacto.setTotalSegundos(segundos);
                contacto.setTotalLlamadas(numLlamadas);

                contactos.add(contacto);
            }
        }

        Collections.sort(contactos);
        /*
        Collections.sort(contactos, new Comparator<Contacto>() {
            public int compare(Contacto c1, Contacto c2) {
                return c1.getTotalSegundosString().compareTo(c2.getTotalSegundosString());
            }
        });
        */

        Contacto contactoEncontrado = new Contacto();

        if (contactos.size() != 0) {

            contactoEncontrado = contactos.get(0);
            String contactId = getContactId(cr, contactoEncontrado.getNumero());

            if (contactId != "") {
                Bitmap photo = null;

                try {
                    //Cursor cur2 = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    //        null, null, null,null);

                    long id = Long.valueOf(contactId).longValue();
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(cr,
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id), true);

                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                        contactoEncontrado.setImagen(photo);

                        assert inputStream != null;
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            contactoEncontrado.setTotalMinutos(Ciclo.formatSegundos(contactoEncontrado.getTotalSegundos()));
        } else {
            contactoEncontrado.setNombre("");
            contactoEncontrado.setNumero("");
            contactoEncontrado.setTotalMinutos(Ciclo.formatSegundos(0));
        }

        return contactoEncontrado;
    }

    public static String getTotalMinutosContacto(ContentResolver cr, String fechaInicio, String fechaFinal, String numero, String tipo) {
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
                android.provider.CallLog.Calls.NUMBER + "==" + numero;

        if (tipo == "SALIENTE") {
            where += " AND " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.OUTGOING_TYPE;
        } else {
            where += " AND " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.INCOMING_TYPE;
        }

        Cursor cur = cr.query(callUri, null, where, null, strOrder);

        cur.moveToFirst();

        int totalSegundos = 0;

        while (!cur.isAfterLast()) {
            totalSegundos += cur.getInt(cur.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            cur.moveToNext();
        }

        return Ciclo.formatSegundos(totalSegundos);
    }

    public static int getTotalLlamadasContacto(ContentResolver cr, String fechaInicio, String fechaFinal, String numero, String tipo) {
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
                android.provider.CallLog.Calls.NUMBER + "==" + numero;

        if (tipo == "SALIENTE") {
            where += " AND " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.OUTGOING_TYPE;
        } else {
            where += " AND " + android.provider.CallLog.Calls.TYPE + "==" + android.provider.CallLog.Calls.INCOMING_TYPE;
        }

        Cursor cur = cr.query(callUri, null, where, null, strOrder);

        cur.moveToFirst();

        int totalLlamadas = 0;

        while (!cur.isAfterLast()) {
            totalLlamadas++;
            cur.moveToNext();
        }

        return totalLlamadas;
    }

    /*
    private static String getContactIdFromNumber(ContentResolver cr, String number) {
        String[] projection = new String[]{Contacts.Phones._ID};
        Uri contactUri = Uri.withAppendedPath(Contacts.Phones.CONTENT_FILTER_URL, Uri.encode(number));
        Cursor c = cr.query(contactUri, projection, null, null, null);
        if (c.moveToFirst()) {
            String contactId = c.getString(c.getColumnIndex(Contacts.Phones._ID));
            return contactId;
        }
        return null;
    }
*/
    private static String getContactId(ContentResolver cr, String number) {
        String contactId = "";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                cr.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                //String contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                //Log.d("hola5", "contactMatch name: " + contactName);
                //Log.d("hola5", "contactMatch id: " + contactId);
            }
            cursor.close();
        }

        return contactId;
    }

    public static String loadContact(ContentResolver contentResolver, Uri contactUri) {
        long contactId = -1;
        String number = "";
        // Primero buscamos la ID del contacto
        Cursor cursor = contentResolver.query(contactUri,
                new String[]{ContactsContract.Contacts._ID}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                contactId = cursor.getLong(0);
            }
        } finally {
            cursor.close();
        }
        // A partir de la ID obtenemos su número
        cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY);
        try {
            if (cursor.moveToFirst()) {
                number = cursor.getString(0);
            }
        } finally {
            cursor.close();
        }
        return number;
    }

    private static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i=0; i < haystack.length(); i++)
        {
            if (haystack.charAt(i) == needle)
            {
                count++;
            }
        }
        return count;
    }
}
