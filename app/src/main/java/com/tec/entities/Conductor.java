package com.tec.entities;

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
        private double posLongitud;
        private double posLatitud;
        private double promedio;

        public Conductor(String nombre, String contrasena, String carnet, double posLongitud, double posLatitud) {
            this.nombre = nombre;
            this.contrasena = contrasena;
            this.carnet = carnet;
            this.posLatitud = posLatitud;
            this.posLongitud = posLongitud;
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
            this.posLongitud = posLongitud;
            this.posLatitud = posLatitud;
            this.promedio = promedio;
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
}
