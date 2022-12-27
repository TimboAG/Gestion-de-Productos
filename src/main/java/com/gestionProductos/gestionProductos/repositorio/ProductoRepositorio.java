
package com.gestionProductos.gestionProductos.repositorio;

import com.gestionProductos.gestionProductos.entidad.Producto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    
    public Optional<Producto> findById(Long id);
}
