package com.dsole.controldellamadas.classes;

import com.github.mikephil.charting.utils.ValueFormatter;

/**
 * Created by dsole on 18/02/2015.
 */
public class MyValueFormatter implements ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        //return "" + ((int) value);
        String aux = "" + ((int) value);
        //return aux;
        return aux;
    }
}
