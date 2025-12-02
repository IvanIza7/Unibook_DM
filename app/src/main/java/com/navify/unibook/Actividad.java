package com.navify.unibook;

public class Actividad {
    private int id;
    private String nombre;
    private String materiaNombre;
    private String materiaColor;
    private String materiaIcono;
    private String fechaEntrega;
    private String horaEntrega;
    private int porcentaje;

    public Actividad(int id, String nombre, String materiaNombre, String materiaColor, String materiaIcono, String fechaEntrega, String horaEntrega, int porcentaje) {
        this.id = id;
        this.nombre = nombre;
        this.materiaNombre = materiaNombre;
        this.materiaColor = materiaColor;
        this.materiaIcono = materiaIcono;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.porcentaje = porcentaje;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getMateriaNombre() { return materiaNombre; }
    public String getMateriaColor() { return materiaColor; }
    public String getMateriaIcono() { return materiaIcono; }
    public String getFechaEntrega() { return fechaEntrega; }
    public String getHoraEntrega() { return horaEntrega; }
    public int getPorcentaje() { return porcentaje; }
}
