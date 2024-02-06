package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    @Autowired
    ArticuloService articuloService;

    @GetMapping({"/", "/index", "/inicio", "/home"})
    public String inicio(Model model){
        model.addAttribute("listaArticulos", articuloService.findAll());
        return "index";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable long id, Model model){
        model.addAttribute("articulo", articuloService.findById(id));
        //model.addAttribute("comentario", new Comentario)
        return "detalle";
    }

}
