package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.Articulo;
import com.example.registrationlogindemo.entity.Comentario;
import com.example.registrationlogindemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    /*@Query("SELECT * FROM comentarios")
    public List<Comentario> verTodos();*/
    public Comentario findById(long id);

    public List<Comentario> findByArticulo(Articulo articulo);
    public List<Comentario> findByAutor(User autor);
}
