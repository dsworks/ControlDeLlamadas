package com.dsole.controldellamadas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dsole on 16/01/2015.
 */
public class Ciclo {

    public static String fechaPrimerDiaCiclo(int primerDiaCiclo, int mes, int año) {
        String res;

        String d = "00" + String.valueOf(primerDiaCiclo);
        d = d.substring(d.length() - 2);

        String m = "00" + String.valueOf(mes);
        m = m.substring(m.length() - 2);

        String y = "0000" + String.valueOf(año);
        y = y.substring(y.length() - 4);


        return formatearFecha(d, m, y);
    }

    public static String fechaUltimoDiaCiclo(int primerDiaCiclo, int mes, int año) {
        String res = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaPrimerDiaCiclo = fechaPrimerDiaCiclo(primerDiaCiclo, mes, año);
        Date date = null;
        try {
            date = sdf.parse(fechaPrimerDiaCiclo);

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);

            String d = "00" + String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            d = d.substring(d.length() - 2);
            String m = "00" + String.valueOf(c.get(Calendar.MONTH) + 1);
            m = m.substring(m.length() - 2);
            String y = "0000" + String.valueOf(c.get(Calendar.YEAR));
            y = y.substring(y.length() - 4);

            return formatearFecha(d, m, y);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


    }

    private static String formatearFecha(String dia, String mes, String año) {

        return año + "-" + mes + "-" + dia;
    }

    public static String format2Minutos(int segundos) {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);

        return df.format(new Date(segundos * 1000L));
    }

    public static String formatSegundos(int segundos)
    {
        String res = "";
        int hours = (int) segundos / 3600;
        int remainder = (int) segundos - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        //no hace falta estos ifs pero los dejo por si vuelvo a cambiar el formato del texto
        res = String.valueOf(hours);
        if(hours == 1) res+= " h, ";
        else res+= " h, ";

        res += String.valueOf(mins);
        if(mins == 1) res+= " m, ";
        else res+= " m, ";

        res += String.valueOf(secs);
        if(secs == 1) res+= " s";
        else res+= " s";

        return res;
    }

    public static String formatFecha(String fechaInicio, String fechaFinal) {
        String res = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date date2 = null;
        try {
            date = sdf.parse(fechaInicio);
            date2 = sdf.parse(fechaFinal);

            sdf = new SimpleDateFormat("dd");
            int diaInicio = Integer.parseInt(sdf.format(date));

            sdf = new SimpleDateFormat("MMMMMMMMMMMMMMM");
            String mesInicio = sdf.format(date);

            sdf = new SimpleDateFormat("dd");
            int diaFinal = Integer.parseInt(sdf.format(date2));

            sdf = new SimpleDateFormat("MMMMMMMMMMMMMMM");
            String mesFinal = sdf.format(date2);

            if(mesFinal == mesFinal) {
                res = "del " + String.valueOf(diaInicio) + " al " + String.valueOf(diaFinal) + " de " + mesFinal;
            } else {
                res = "del " + String.valueOf(diaInicio) + " de " + mesInicio + " al " + String.valueOf(diaFinal) + " de " + mesFinal;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }
}
