package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.Articulo;
import com.example.registrationlogindemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {
    public Articulo findById(long id);
    public List<Articulo> findByUser(User user);
}
