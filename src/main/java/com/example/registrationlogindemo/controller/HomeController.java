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
import java.util.ArrayList;
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


    /* Así estaba hecho antes de crear los comentarios:
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable long id, Model model){
        model.addAttribute("articulo", articuloService.findById(id));
        // model.addAttribute("comentario", new Comentario)
        return "detalle";
    } */

    // @GetMapping("/detalle/{id}") y @PostMapping("/detalle/submit") t0do esto sería para que nos guarde los comentarios.
    // @PostMapping("/detalle/submit") este creado.
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable long id, Model model, Principal principal) {
        // Verifica si el usuario está autenticado
        if (principal != null) {
            // Coge el nombre de usuario del usuario ya logeado
            String username = principal.getName();
            // Encuentra al usuario autenticado por su email (o id, según lo que estés utilizando para autenticar al usuario)
            User usuarioAutenticado = userService.findByEmail(username); // O userService.findById(id) si estás usando el id para autenticar
            // Agrega el usuario autenticado al modelo
            model.addAttribute("usuarioAutenticado", usuarioAutenticado);
        }
        // Aquí nos agrega el artículo al modelo
        model.addAttribute("articulo", articuloService.findById(id));
        // Aquí nos agrega un nuevo comentario al modelo
        model.addAttribute("comentario", new Comentario());


        // (Para mostrar los comentarios) Cogemos el artículo por su ID
        Articulo articulo = articuloService.findById(id);
        // (Para mostrar los comentarios) Cogemos la lista de comentarios asociados al ID del artículo (Sin esto no podemos mostrar los comentarios)
        List<Comentario> listadoComentario = comentarioService.findByArticulo(articulo);
        model.addAttribute("listadoComentario", listadoComentario);


        // Para mostrar los NOMBRES Y APELLIDOS de los usuarios: Almacenamos en una lista para los nombres y apellidos del usuario.
        // el nombreYApellidos poner bien enlazado en detalle.html para que funcione
        List<String> nombreYApellidos = new ArrayList<>();
        // Para cada comentario, cogemos el nombre y apell. del usuario que lo creó y lo agrega a la lista
        for (Comentario comentario : listadoComentario) { // el listadoComentario es el de antes (Para mostrar los comentarios)
            String nombreUsuario = comentario.getAutor().getName(); // Obtenemos el nombre del usuario asociado al comentario
            nombreYApellidos.add(nombreUsuario); // Agregar el nombre del usuario a la lista
        }
        // Agregar el ArrayListla de lista de nombres de usuario al modelo
        model.addAttribute("nombreYApellidos", nombreYApellidos);

        return "detalle";
    }

    @PostMapping("/detalle/submit")
    public String guardarComentario(@ModelAttribute("comentario") Comentario comentario, @RequestParam("idArticulo") Long idArticulo, @RequestParam("autorId") Long autorId) {
        // Pone el artículo del comentario
        Articulo articulo = articuloService.findById(idArticulo); // Suponiendo que tienes un servicio de artículos con un método findById
        comentario.setArticulo(articulo);

        // Pone el autor del comentario
        User autor = userService.findById(autorId); // Si ya tenemos un servicio de usuarios con un método findById (que ya está creado, sino crear)
        comentario.setAutor(autor);

        // Guardar el comentario
        comentarioService.save(comentario);

        return "redirect:/detalle/" + idArticulo;
    }



}
