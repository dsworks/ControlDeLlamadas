package com.dsole.controldellamadas.classes;

import android.content.Context;
import android.graphics.Bitmap;

import com.dsole.controldellamadas.R;

/**
 * Created by dsole on 29/12/2014.
 */
public class CallLog {
    private String name;
    private String number;
    private String time;
    private String date;
    private String type;
    private Bitmap imagen;

    public CallLog(){
        this.imagen = null;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setName(String name, Context c) {
        if(name == null) this.name = c.getString(R.string.desconocido);
        else this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTime(int time) {
        /*
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);

        this.time = df.format(new Date(Integer.valueOf(time)*1000L));
        */
        this.time = Ciclo.formatSegundos(time);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type, Context c) {
        this.type = type;
        if (type.equals("1")) this.type = c.getString(R.string.llamada_entrante);
        else if (type.equals("2")) this.type = c.getString(R.string.llamada_saliente);
        else this.type = c.getString(R.string.llamada_perdida);
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
