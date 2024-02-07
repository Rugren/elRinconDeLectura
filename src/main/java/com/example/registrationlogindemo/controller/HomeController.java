package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.Articulo;
import com.example.registrationlogindemo.entity.Comentario;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.ArticuloService;
import com.example.registrationlogindemo.service.ComentarioService;
import com.example.registrationlogindemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    ArticuloService articuloService;
    @Autowired
    UserService userService;
    @Autowired
    ComentarioService comentarioService;

    @GetMapping({"/", "/index", "/inicio", "/home"})
    public String inicio(Model model){
        model.addAttribute("listaArticulos", articuloService.findAll());
        return "index";
    }
    // para poder acceder a articulos-creados también tengo que hacer esto:
    @GetMapping("/articulos-creados")
    public String articulosCreados(Model model){
        model.addAttribute("losArticulosCreados", articuloService.findAll());
        return "articulos-creados";
    }


    /* Así estaba antes del hecho de los comentarios:
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable long id, Model model){
        model.addAttribute("articulo", articuloService.findById(id));
        // model.addAttribute("comentario", new Comentario)
        return "detalle";
    } */

    // @GetMapping("/detalle/{id}") y @PostMapping("/detalle/submit") T0DO esto sería para que nos guarde los comentarios.
    // @PostMapping("/detalle/submit") este creado.
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable long id, Model model, Principal principal) {
        // Verifica si el usuario está autenticado
        if (principal != null) {
            // Obtén el nombre de usuario del usuario autenticado
            String username = principal.getName();
            // Encuentra al usuario autenticado por su email (o id, según lo que estés utilizando para autenticar al usuario)
            User usuarioAutenticado = userService.findByEmail(username); // O userService.findById(id) si estás usando el id para autenticar
            // Agrega el usuario autenticado al modelo
            model.addAttribute("usuarioAutenticado", usuarioAutenticado);
        }
        // Agrega el artículo al modelo
        model.addAttribute("articulo", articuloService.findById(id));
        // Agrega un nuevo comentario al modelo
        model.addAttribute("comentario", new Comentario());


        // (Para mostrar los comentarios) Cogemos el artículo por su ID
        Articulo articulo = articuloService.findById(id);
        // (Para mostrar los comentarios) Cogemos la lista de comentarios asociados al ID del artículo (Sin esto no podemos mostrar los comentarios)
        List<Comentario> listadoComentario = comentarioService.findByArticulo(articulo);
        model.addAttribute("listadoComentario", listadoComentario);


        // Devuelve la vista "detalle"
        return "detalle";
    }

    @PostMapping("/detalle/submit")
    public String guardarComentario(@ModelAttribute("comentario") Comentario comentario, @RequestParam("idArticulo") Long idArticulo, @RequestParam("autorId") Long autorId) {
        // Establecer el artículo del comentario
        Articulo articulo = articuloService.findById(idArticulo); // Suponiendo que tienes un servicio de artículos con un método findById
        comentario.setArticulo(articulo);

        // Establecer el autor del comentario si es necesario
        User autor = userService.findById(autorId); // Suponiendo que tienes un servicio de usuarios con un método findById
        comentario.setAutor(autor);

        // Guardar el comentario
        comentarioService.save(comentario);

        // Redirigir a la página de detalle del artículo
        return "redirect:/detalle/" + idArticulo; // Suponiendo que "/detalle/" es el endpoint correcto para la página de detalle
    }



}
