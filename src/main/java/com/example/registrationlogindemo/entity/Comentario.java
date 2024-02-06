package com.example.registrationlogindemo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String contenido;
    @ManyToOne
    private User autor;

    @ManyToOne
    private Articulo articulo;
}
