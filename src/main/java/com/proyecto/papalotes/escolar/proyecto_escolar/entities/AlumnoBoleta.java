package com.proyecto.papalotes.escolar.proyecto_escolar.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "Alumno_Boleta")
public class AlumnoBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_boleta")
    private Boleta boleta;

    @ManyToOne
    @JoinColumn(name = "id_materia")
    private Materia materia;

    private Double  calificacion;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boleta getBoleta() {
        return boleta;
    }

    public void setBoleta(Boleta boleta) {
        this.boleta = boleta;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Double  getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double  calificacion) {
        this.calificacion = calificacion;
    }
}