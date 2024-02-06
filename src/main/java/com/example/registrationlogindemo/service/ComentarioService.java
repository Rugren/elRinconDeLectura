package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.entity.Articulo;
import com.example.registrationlogindemo.entity.Comentario;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {
    @Autowired
    ComentarioRepository comentarioRepository;

    public Comentario findById(long id){
        return comentarioRepository.findById(id);
    }

    public List<Comentario> findAll(){
        return comentarioRepository.findAll();
    }

    public List<Comentario> findByAutor(User autor){
        return comentarioRepository.findByAutor(autor);
    }

    public List<Comentario> findByArticulo(Articulo articulo){
        return comentarioRepository.findByArticulo(articulo);
    }

}
