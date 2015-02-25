package com.dsole.controldellamadas.classes;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsole on 19/02/2015.
 */
public class Años {

    private static String[] años;

    public static List<String> setLista() {
        List<String> list = new ArrayList<>();
        list.add("2005");
        list.add("2006");
        list.add("2007");
        list.add("2008");
        list.add("2009");
        list.add("2010");
        list.add("2011");
        list.add("2012");
        list.add("2013");
        list.add("2014");
        list.add("2015");
        list.add("2016");
        list.add("2017");
        list.add("2018");
        list.add("2019");
        list.add("2020");
        list.add("2021");
        list.add("2022");
        list.add("2023");
        list.add("2024");
        list.add("2025");

        return list;
    }

    public static int setPosicionAño(ArrayAdapter<String> dataAdapter, int año) {

        //int posicion = año - 2005;

        años = new String[21];
        años[0] = "2005";
        años[1] = "2006";
        años[2] = "2007";
        años[3] = "2008";
        años[4] = "2009";
        años[5] = "2010";
        años[6] = "2011";
        años[7] = "2012";
        años[8] = "2013";
        años[9] = "2014";
        años[10] = "2015";
        años[11] = "2016";
        años[12] = "2017";
        años[13] = "2018";
        años[14] = "2019";
        años[15] = "2020";
        años[16] = "2021";
        años[17] = "2022";
        años[18] = "2023";
        años[19] = "2024";
        años[20] = "2025";


        int ret = setPosicionAño(Integer.toString(año));
        //int ret = dataAdapter.getPosition(años[posicion]);
        //int ret = dataAdapter.getPosition(años[posicion]);

        return ret;
    }

    public static int setPosicionAño(String año) {
        int ret = 0;

        años = new String[21];
        años[0] = "2005";
        años[1] = "2006";
        años[2] = "2007";
        años[3] = "2008";
        años[4] = "2009";
        años[5] = "2010";
        años[6] = "2011";
        años[7] = "2012";
        años[8] = "2013";
        años[9] = "2014";
        años[10] = "2015";
        años[11] = "2016";
        años[12] = "2017";
        años[13] = "2018";
        años[14] = "2019";
        años[15] = "2020";
        años[16] = "2021";
        años[17] = "2022";
        años[18] = "2023";
        años[19] = "2024";
        años[20] = "2025";


        for (int i = 0; i < años.length; i++)
            if (años[i].equals(año)) {
                ret = i;
                break;
            }
        return ret;
    }
}
