package com.dsole.controldellamadas.classes;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.dsole.controldellamadas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsole on 04/02/2015.
 */
public class Meses {

    private static String[] meses;

    public static List<String> setLista(Context context) {
        List<String> list = new ArrayList<String>();
        list.add(context.getString(R.string.Enero));
        list.add(context.getString(R.string.Febrero));
        list.add(context.getString(R.string.Marzo));
        list.add(context.getString(R.string.Abril));
        list.add(context.getString(R.string.Mayo));
        list.add(context.getString(R.string.Junio));
        list.add(context.getString(R.string.Julio));
        list.add(context.getString(R.string.Agosto));
        list.add(context.getString(R.string.Septiembre));
        list.add(context.getString(R.string.Octubre));
        list.add(context.getString(R.string.Noviembre));
        list.add(context.getString(R.string.Diciembre));

        return list;
    }

    public static int setPosicionMes(ArrayAdapter<String> dataAdapter, int mes, Context context) {
        meses = new String[12];
        meses[0] = context.getString(R.string.Enero);
        meses[1] = context.getString(R.string.Febrero);
        meses[2] = context.getString(R.string.Marzo);
        meses[3] = context.getString(R.string.Abril);
        meses[4] = context.getString(R.string.Mayo);
        meses[5] = context.getString(R.string.Junio);
        meses[6] = context.getString(R.string.Julio);
        meses[7] = context.getString(R.string.Agosto);
        meses[8] = context.getString(R.string.Septiembre);
        meses[9] = context.getString(R.string.Octubre);
        meses[10] = context.getString(R.string.Noviembre);
        meses[11] = context.getString(R.string.Diciembre);

        int ret = dataAdapter.getPosition(meses[mes - 1]);

        return ret;
    }

    public static int setPosicionMes(String mes, Context context) {
        int ret = 0;
        meses = new String[12];
        meses[0] = context.getString(R.string.Enero);
        meses[1] = context.getString(R.string.Febrero);
        meses[2] = context.getString(R.string.Marzo);
        meses[3] = context.getString(R.string.Abril);
        meses[4] = context.getString(R.string.Mayo);
        meses[5] = context.getString(R.string.Junio);
        meses[6] = context.getString(R.string.Julio);
        meses[7] = context.getString(R.string.Agosto);
        meses[8] = context.getString(R.string.Septiembre);
        meses[9] = context.getString(R.string.Octubre);
        meses[10] = context.getString(R.string.Noviembre);
        meses[11] = context.getString(R.string.Diciembre);


        for (int i = 0; i < meses.length; i++)
            if (meses[i].equals(mes))
                ret = i + 1;
        return ret;
    }
}
