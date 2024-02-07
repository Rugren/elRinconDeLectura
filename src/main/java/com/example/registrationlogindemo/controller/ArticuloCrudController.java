package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.Articulo;
import com.example.registrationlogindemo.service.ArticuloService;
import com.example.registrationlogindemo.service.UserService;
import com.example.registrationlogindemo.storage.StorageService;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

@Controller
@RequestMapping("/crud/articulos")
public class ArticuloCrudController {
    @Autowired
    ArticuloService articuloService;
    @Autowired
    UserService userService;
    @Autowired
    StorageService storageService;


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

    /* así sin las imágenes
    @PostMapping("/altas/submit")
    public String guardarDatosFormulario(@ModelAttribute Articulo articulo){
        articuloService.save(articulo);
        return "redirect:/crud/articulos/altas";
    }
    */

    @PostMapping("/altas/submit")
    public String guardarDatosFormulario(@ModelAttribute Articulo articulo, @RequestParam("file") MultipartFile file){
        // Crea solo la fecha con la hora y la hora para el otro campo de la BD. Pero si tengo que ponerlo en /modificar/submit de abajo.

        // esto para que si el archivo está vacío, que nos coja la imagen que teníamos.
        if(!file.isEmpty()){
            String imagen = storageService.store(file, String.valueOf(articulo.getId()));
            System.out.println("La imagen a guardar es: "+ imagen);
            articulo.setImagen(MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "serveFile", imagen).build().toUriString());
        }
        articuloService.save(articulo);
        return "redirect:/crud/articulos/altas";
    }


    @GetMapping("/modificar/{id}")
    public String modificarArticulos(@PathVariable("id") long id, Model model){
        model.addAttribute("articulo", articuloService.findById(id));
        return "formulario-articulos";
    }

    /* Sin las imágenes
    @PostMapping("/modificar/submit")
    public String guardarModificaciones(@ModelAttribute Articulo articulo, Authentication authentication){
        articulo.setUser(userService.findByEmail(authentication.getName()));
        articulo.setFecha(LocalDateTime.now());

        // articulo.setFecha(toLocalTime()); o esto: articulo.setHora(toLocalTime());
        // no probado, probar (debería dar la hora, ver cuando cree un artículo, en la bd si me ha creado la hora.)

        articuloService.save(articulo);

        return "redirect:/crud/articulos";
    } */

    /* Con las imágenes */
    @PostMapping("/modificar/submit")
    public String guardarModificaciones(@ModelAttribute Articulo articulo, Authentication authentication, @RequestParam("file") MultipartFile file){
        articulo.setUser(userService.findByEmail(authentication.getName()));
        articulo.setFecha(LocalDateTime.now()); // Con esto modificamos la fecha y la hora y la guarda en la BD.
        articulo.setHora(LocalTime.now()); // Con esto modificamos solo la hora y la guarda en la BD. (Tenemos en la BD otro campo con hora)

        if (!file.isEmpty()) {
            String imagen = storageService.store(file, String.valueOf(articulo.getId()));
            /* En (articulo.getId())) tenía getTitulo y en "/altas/submit" también.
            Pero dejado .getId para que le de ese nombre a las imágenes en la BD. */
            System.out.println("La imagen a guardar es : " + imagen);
            articulo.setImagen(MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "serveFile", imagen).build().toUriString());
        }
        articuloService.save(articulo);
        return "redirect:/crud/articulos";
    }

    /* Esto buscado en Internet, pero cuando creo o modifico artículos no me deja avanzar
    @GetMapping("/modificar/{id}")
    public String modificarArticulos(@PathVariable("id") long id, Model model){
        Articulo articuloExistente = articuloService.findById(id);
        model.addAttribute("articulo", articuloExistente);
        return "formulario-articulos";
    }
    @PostMapping("/modificar/submit")
    public String guardarModificaciones(@ModelAttribute Articulo articuloModificado, Authentication authentication){
        Articulo articuloExistente = articuloService.findById(articuloModificado.getId());
        articuloExistente.setTitulo(articuloModificado.getTitulo());
        articuloExistente.setContenido(articuloModificado.getContenido());
        // Y otros campos a actualizar si los tuviésemos

        articuloExistente.setUser(userService.findByEmail(authentication.getName()));
        articuloService.save(articuloExistente);
        return "redirect:/crud/articulos";
    } */

    /* Otra manera, ejemplo con categorías:
    @PostMapping("/modificar/submit")
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













