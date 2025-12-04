package com.navify.unibook;

public class ActividadHome {
    private int id;
    private String titulo;
    private String fechaEntrega;
    private int porcentaje;
    private String nombreMateria;
    private String colorMateria;

    private String descripcion;
    private String fotoUri;

    public ActividadHome(int id, String titulo, String fechaEntrega, int porcentaje, String nombreMateria, String colorMateria) {
        this.id = id;
        this.titulo = titulo;
        this.fechaEntrega = fechaEntrega;
        this.porcentaje = porcentaje;
        this.nombreMateria = nombreMateria;
        this.colorMateria = colorMateria;
    }

    public String getDescripcion() { return descripcion;}
    public void setDescripcion(String descripcion) { this.descripcion = descripcion;}

    public String getFotoUri() { return fotoUri;}
    public void setFotoUri(String fotoUri) { this.fotoUri = fotoUri;}

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getFechaEntrega() { return fechaEntrega; }
    public int getPorcentaje() { return porcentaje; }
    public String getNombreMateria() { return nombreMateria; }
    public String getColorMateria() { return colorMateria; }
}