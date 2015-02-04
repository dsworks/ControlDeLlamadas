package com.dsole.controldellamadas;

import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * Created by dsole on 03/02/2015.
 */
public class Contacto {

    private Bitmap imagen;
    private String numero;
    private String nombre;
    private int totalLlamadas;
    private String totalMinutos;
    private int totalSegundos;

    public Contacto() {
        this.imagen = null;
        this.nombre = "";
        this.numero = "";
        this.totalLlamadas = 0;
        this.totalMinutos = "";
        this.totalSegundos = 0;
    }

    public Contacto(Bitmap imagen, String nombre, String numero, int totalLlamadas, String totalMinutos, int totalSegundos) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.numero = numero;
        this.totalLlamadas = totalLlamadas;
        this.totalMinutos = totalMinutos;
        this.totalSegundos = totalSegundos;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public String getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTotalLlamadas() {
        return totalLlamadas;
    }

    public String getTotalLlamadasString() {
        return String.valueOf(totalLlamadas);
    }

    public String getTotalMinutos() {
        return totalMinutos;
    }

    public int getTotalSegundos() {
        return totalSegundos;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public void setNumero(String numero) {
        if(numero == "" || numero == null) this.numero = "Ningún contacto";
        else this.numero = numero;
    }

    public void setNombre(String nombre) {
        if(nombre == "" || nombre == null) this.nombre = "Ningún contacto";
        else this.nombre = nombre;
    }

    public void setTotalLlamadas(int totalLlamadas) {
        this.totalLlamadas = totalLlamadas;
    }

    public void setTotalMinutos(String totalMinutos) {
        this.totalMinutos = totalMinutos;
    }

    public void setTotalSegundos(int totalSegundos) {
        this.totalSegundos = totalSegundos;
    }
}
