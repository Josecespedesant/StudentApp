package com.tec.entities;

import com.tec.studentapp.Posicion;

import java.util.LinkedList;

/**
 * Representa conductor
 *
 * @author David Azofeifa H.
 */
public class Conductor {

    private String nombre;
    private String contrasena;
    private String carnet;
    private LinkedList<Estudiante> amigos;
    private int numCalificaciones;
    private Posicion posicionHogar;
    private double promedio;

    public Posicion getPosicionHogar() {
        return posicionHogar;
    }

    public void setPosicionHogar(Posicion posicionHogar) {
        this.posicionHogar = posicionHogar;
    }

    public Conductor(String nombre, String contrasena, String carnet, double posLongitud, double posLatitud) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.carnet = carnet;
        this.posicionHogar = new Posicion(posLongitud, posLatitud);
        this.numCalificaciones = 0;
        this.amigos = new LinkedList<Estudiante>();
    }

    public Conductor(String name, String contrasena, int numCalificaciones, String carnet,
                     LinkedList<Estudiante> amigos, double posLatitud, double posLongitud, double promedio) {
        this.nombre = name;
        this.numCalificaciones = numCalificaciones;
        this.contrasena = contrasena;
        this.carnet = carnet;
        this.amigos = amigos;
        this.posicionHogar = new Posicion(posLatitud, posLongitud);
        this.promedio = promedio;
    }

    public int getNumCalificaciones() {
        return numCalificaciones;
    }

    public void setNumCalificaciones(int numCalificaciones) {
        this.numCalificaciones = numCalificaciones;
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
}
