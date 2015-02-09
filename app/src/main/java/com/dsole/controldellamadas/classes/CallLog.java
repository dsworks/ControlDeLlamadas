package com.dsole.controldellamadas.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dsole on 29/12/2014.
 */
public class CallLog {
    private String name;
    private String number;
    private String time;
    private String date;
    private String type;

    public CallLog(){

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

    public void setName(String name) {
        if(name == null) this.name = "Desconocido";
        else this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTime(String time) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);

        this.time = df.format(new Date(Integer.valueOf(time)*1000L));
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
        if (type.equals("1")) this.type = "Llamada entrante";
        else if (type.equals("2")) this.type = "Llamada saliente";
        else this.type = "Llamada perdida";
    }
}
