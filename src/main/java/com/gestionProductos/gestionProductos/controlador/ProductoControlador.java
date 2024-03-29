package com.gestionProductos.gestionProductos.controlador;

import com.gestionProductos.gestionProductos.entidad.Producto;
import com.gestionProductos.gestionProductos.servicio.ProductoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductoControlador {

    @Autowired
    private ProductoServicio productoServico;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @RequestMapping("/productos")
    public String incio(Model modelo, @Param("palabraClave") String palabraClave) {
        List<Producto> listaProductos = productoServico.listAll(palabraClave);
        modelo.addAttribute("listaProductos", listaProductos);
        modelo.addAttribute("palabraClave", palabraClave);
        return "productos";
    }

    @RequestMapping("/nuevo")
    public String registrarProducto(Model modelo) {
        Producto producto = new Producto();
        modelo.addAttribute("producto", producto);
        return "registrar_producto";
    }

    @RequestMapping(value = "/guardar", method = RequestMethod.POST)
    public String guardar(@ModelAttribute("producto") Producto producto) {
        productoServico.save(producto);
        return "redirect:/productos";
    }

    @RequestMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable(name = "id") Long id) {
        ModelAndView modelo = new ModelAndView("editar_producto");
        Producto producto = productoServico.get(id);
        modelo.addObject("producto", producto);
        return modelo;
    }

    @RequestMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(name = "id") Long id) {
        productoServico.delete(id);
        return "redirect:/productos";
    }

    @RequestMapping("/altaBaja/{id}")
    public String altaBaja(@PathVariable(name = "id") Long id) {
        productoServico.bajaAlta(id);
        return "redirect:/productos";
    }
}
