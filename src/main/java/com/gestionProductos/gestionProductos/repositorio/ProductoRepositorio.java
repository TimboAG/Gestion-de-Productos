
package com.gestionProductos.gestionProductos.repositorio;

import com.gestionProductos.gestionProductos.entidad.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    
    public Optional<Producto> findById(Long id);
    
    @Query("SELECT p FROM Producto p WHERE" + " CONCAT(p.id, p.nombre, p.marca, p.hechoEn, p.precio) "+ "LIKE %:name%")
    public List<Producto> findAll(@Param("name") String palabraClave);
}
