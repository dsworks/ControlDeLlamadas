package com.dsole.controldellamadas;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsole on 04/02/2015.
 */
public class Meses {

    private static String[] meses;

    public static List<String> setLista() {
        List<String> list = new ArrayList<String>();
        list.add("Enero");
        list.add("Febrero");
        list.add("Marzo");
        list.add("Abril");
        list.add("Mayo");
        list.add("Junio");
        list.add("Julio");
        list.add("Agosto");
        list.add("Septiembre");
        list.add("Octubre");
        list.add("Noviembre");
        list.add("Diciembre");

        return list;
    }

    public static int setPosicionMes(ArrayAdapter<String> dataAdapter, int mes) {
        meses = new String[12];
        meses[0] = "Enero";
        meses[1] = "Febrero";
        meses[2] = "Marzo";
        meses[3] = "Abril";
        meses[4] = "Mayo";
        meses[5] = "Junio";
        meses[6] = "Julio";
        meses[7] = "Agosto";
        meses[8] = "Septiembre";
        meses[9] = "Octubre";
        meses[10] = "Noviembre";
        meses[11] = "Diciembre";

        int ret = dataAdapter.getPosition(meses[mes - 1]);

        return ret;
    }

    public static int setPosicionMes(String mes) {
        int ret = 0;

        meses = new String[12];
        meses[0] = "Enero";
        meses[1] = "Febrero";
        meses[2] = "Marzo";
        meses[3] = "Abril";
        meses[4] = "Mayo";
        meses[5] = "Junio";
        meses[6] = "Julio";
        meses[7] = "Agosto";
        meses[8] = "Septiembre";
        meses[9] = "Octubre";
        meses[10] = "Noviembre";
        meses[11] = "Diciembre";


        for (int i = 0; i < meses.length; i++)
            if (meses[i] == mes)
                ret = i + 1;
        return ret;
    }
}
