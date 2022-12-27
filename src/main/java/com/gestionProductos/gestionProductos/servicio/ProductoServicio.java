package com.gestionProductos.gestionProductos.servicio;

import com.gestionProductos.gestionProductos.entidad.Producto;
import com.gestionProductos.gestionProductos.repositorio.ProductoRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public List<Producto> listAll(String palabraClave) {
        if(palabraClave != null){
                return productoRepositorio.findAll(palabraClave); 
                }
        return productoRepositorio.findAll();
    }

    public void save(Producto producto) {
        productoRepositorio.save(producto);
    }

    public Producto get(Long id) {
        return productoRepositorio.findById(id).get();
    }

    public void delete(Long id) {
        productoRepositorio.deleteById(id);
    }

    public Producto bajaAlta(Long id) {
        Optional<Producto> optinalProducto = productoRepositorio.findById(id);
        Producto producto = new Producto();
        if (optinalProducto.isPresent()) {
            producto = optinalProducto.get();
            if (producto.getEliminado() == false) {
                producto.setEliminado(Boolean.TRUE);
                productoRepositorio.save(producto);
            } else {
                producto.setEliminado(Boolean.FALSE);
                productoRepositorio.save(producto);
            }
        }
        return producto;
    }
}
