package com.example.registrationlogindemo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
public class Articulo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user;

    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String contenido;

    //private LocalDate fecha;
    private LocalDateTime fecha; // Con LocalDateTime crea el día y fecha de creación, pero con el método de crearFecha
    private LocalTime hora; // crea la hora, pero con el método de obtenerHora
    @PrePersist
    protected void crearFecha() {
        this.fecha = LocalDateTime.now();
        this.hora = this.fecha.toLocalTime(); // Extrae la hora de la fecha actual
    }
    public LocalTime obtenerHora() {
        if (this.fecha != null) {
            return this.fecha.toLocalTime();
        } else {
            // Manejar la situación donde la fecha no se ha establecido aún
            return null;
        }
    }

    private String imagen;
    private String categoria;

}
