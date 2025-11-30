package com.navify.unibook;

public class Materia {
    private int id;
    private String nombre;
    private String profesor;
    private String color;
    private String icono;

    public Materia(int id, String nombre, String profesor, String color, String icono) {
        this.id = id;
        this.nombre = nombre;
        this.profesor = profesor;
        this.color = color;
        this.icono = icono;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getColor() {
        return color;
    }

    public String getIcono() {
        return icono;
    }
}
