package com.taniafontcuberta.basketball.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Alfredo on 28/02/2016.
 */
public class Atleta {
    private Long id;
    @SerializedName("nombre")
    private String name;
    private String apellido;
    private String nacionalidad;
    private Date fechaNacimiento;

    public Atleta() {
    }

    public Atleta(Long id, String name, String apellido, String nacionalidad, Date fechaNacimiento) {
        this.id = id;
        this.name = name;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Atleta atleta = (Atleta) o;

        if (id != null ? !id.equals(atleta.id) : atleta.id != null) return false;
        if (name != null ? !name.equals(atleta.name) : atleta.name != null) return false;
        if (apellido != null ? !apellido.equals(atleta.apellido) : atleta.apellido != null)
            return false;
        if (nacionalidad != null ? !nacionalidad.equals(atleta.nacionalidad) : atleta.nacionalidad != null)
            return false;
        return fechaNacimiento != null ? fechaNacimiento.equals(atleta.fechaNacimiento) : atleta.fechaNacimiento == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (apellido != null ? apellido.hashCode() : 0);
        result = 31 * result + (nacionalidad != null ? nacionalidad.hashCode() : 0);
        result = 31 * result + (fechaNacimiento != null ? fechaNacimiento.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Atleta{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apellido='" + apellido + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }
}
