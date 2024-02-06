package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.Articulo;
import com.example.registrationlogindemo.service.ArticuloService;
import com.example.registrationlogindemo.service.UserService;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/crud/articulos")
public class ArticuloCrudController {
    @Autowired
    ArticuloService articuloService;
    @Autowired
    UserService userService;

    @GetMapping
    public String mostrarListadoArticulos(Model model){
        model.addAttribute("listaArticulos", articuloService.findAll());
        return "listado-articulos";
    }

    @GetMapping("/altas")
    public  String mostrarFormularioAltas(Model model, Authentication authentication){
        Articulo articulo = new Articulo();
        articulo.setUser(userService.findByEmail(authentication.getName()));
        model.addAttribute("articulo", articulo);
        return "formulario-articulos";
    }

    @PostMapping("/altas/submit")
    public String guardarDatosFormulario(@ModelAttribute Articulo articulo){
        articuloService.save(articulo);
        return "redirect:/crud/articulos/altas";
    }

    @GetMapping("/modificar/{id}")
    public String modificarArticulos(@PathVariable("id") long id, Model model){
        model.addAttribute("articulo", articuloService.findById(id));
        return "formulario-articulos";
    }

    /* asi es como estaba: (supuestamente hay que editar aquí
    para que nos guarde al modificar y no nos cree otro articulo (creo que es con el .save) */
    @PostMapping("/modificar/submit")
    public String guardarModificaciones(@ModelAttribute Articulo articulo, Authentication authentication){
        articulo.setUser(userService.findByEmail(authentication.getName()));
        articuloService.save(articulo);
        return "redirect:/crud/articulos";
    }

    /* aquí editando, cambiar el findByEmail por alguno que exista, o crearlo.
    @PostMapping("/modificar/submit")
    public String guardarModificaciones(@ModelAttribute Articulo articulo, Authentication authentication){
        articulo.setUser(articuloService.findById(authentication.getName()));
        articuloService.save(articulo);
        return "redirect:/crud/articulos";
    } */

    /*@PostMapping("/modificar/submit")
    public String guardarModificaciones(@ModelAttribute Producto producto, @RequestParam("categories") Long categoriaId) {
        // Fetch the category from the database
        Category category = categoryService.findById(categoriaId);

        // Set the category to the Producto
        producto.setCategories(Collections.singletonList(category)); // Use Collections.singletonList if a Producto has only one category

        // Save the modified Producto
        productoService.save(producto);

        return "redirect:/crud/articulos";
    }*/

    @GetMapping("/eliminar/{id}")
    public String eliminarArticulos(@PathVariable("id") long id){
        articuloService.deleteById(id);
        return "redirect:/crud/articulos";
    }


}













