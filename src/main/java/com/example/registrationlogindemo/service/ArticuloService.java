package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.entity.Articulo;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.ArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloService {
    @Autowired
    ArticuloRepository articuloRepository;

    public Articulo findById(long id){
        return articuloRepository.findById(id);
    }

    public List<Articulo> findAll(){
        return articuloRepository.findAll();
    }

    public List<Articulo> findByUser(User user){
        return articuloRepository.findByUser(user);
    }

    public Articulo save(Articulo articulo){
        return articuloRepository.save(articulo);
    }

    public void deleteById(long id){
        articuloRepository.deleteById(id);
    }
}
