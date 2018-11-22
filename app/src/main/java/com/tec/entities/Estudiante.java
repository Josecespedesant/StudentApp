package com.tec.entities;

import java.util.LinkedList;

/**
 * Representa estudiante.
 *
 * @author David Azofeifa H.
 */
public class Estudiante {

    private String nombre;
    private String contrasena;
    private String carnet;
    private LinkedList<Conductor> amigos;
    private int viajesRealizados;
    private int numCalificaciones;
    private double posLongitud;
    private double posLatitud;
    private double promedio;

    public Estudiante(String nombre, String contrasena, String carnet, double posLongitud, double posLatitud) {
        this.nombre = nombre;
        this.carnet =carnet;
        this.viajesRealizados = 0;
        this.numCalificaciones = 0;
        this.amigos = new LinkedList<Conductor>();
        this.contrasena = contrasena;
        this.posLatitud = posLatitud;
        this.posLongitud = posLongitud;
    }

    public Estudiante(String nombre, String contrasena, String carnet, int numCalificaciones, int viajesRealizados,
                      LinkedList<Conductor> amigos, double promedio, double posLatitud, double posLongitud) {
        this.nombre = nombre;
        this.carnet = carnet;
        this.numCalificaciones = numCalificaciones;
        this.viajesRealizados = viajesRealizados;
        this.contrasena = contrasena;
        this.amigos = amigos;
        this.promedio = promedio;
        this.posLongitud = posLongitud;
        this.posLatitud = posLatitud;
    }

    public int getViajesRealizados() {
        return viajesRealizados;
    }

    public void setViajesRealizados(int viajesRealizados) {
        this.viajesRealizados = viajesRealizados;
    }

    public int getNumCalificaciones() {
        return numCalificaciones;
    }

    public void setNumCalificaciones(int numCalificaciones) {
        this.numCalificaciones = numCalificaciones;
    }

    public double getPosLongitud() {
        return posLongitud;
    }

    public void setPosLongitud(double posLongitud) {
        this.posLongitud = posLongitud;
    }

    public double getPosLatitud() {
        return posLatitud;
    }

    public void setPosLatitud(double posLatitud) {
        this.posLatitud = posLatitud;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getCarnet() {
        return carnet;
    }

    public LinkedList<Conductor> getAmigos() {
        return amigos;
    }
}

