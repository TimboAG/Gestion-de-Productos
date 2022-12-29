package com.gestionProductos.gestionProductos.servicio;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoService {

    @Value("${storage.location}")
    private String storageLocation;
//
//   public String copiar(MultipartFile archivo) throws Exception {
//        String nombreFoto = archivo.getOriginalFilename();
//        if (archivo.isEmpty()) {
//            throw new Exception("No se puede almacenar un archivo vacio");
//        }
//        try {
//           Path rutaFoto = Paths.get(storageLocation, nombreFoto).toAbsolutePath();
//           Files.copy(archivo.getInputStream(),rutaFoto, StandardCopyOption.REPLACE_EXISTING);
//           return nombreFoto;
//        } catch (IOException e) {
//            throw new Exception("Error al almacenar el archivo" + nombreFoto);
//        }
//    }
//    

    @PostConstruct
    public void iniciarAlmacenDeArchivo() throws Exception {
        try {
            Files.createDirectories(Paths.get(storageLocation));
        } catch (IOException e) {
            throw new Exception("Error al iniciar ubicacion en el almacen de archivos");
        }
    }

    public String almacenarArchivo(MultipartFile archivo) throws Exception {
        String nombreArchivo = archivo.getOriginalFilename();
        if (archivo.isEmpty()) {
            throw new Exception("No se puede almacenar un archivo vacio");
        }
        try {
            InputStream inputStream = archivo.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new Exception("Error al almacenar el archivo" + nombreArchivo);
        }
        return nombreArchivo;
    }

    public Path cargarArchivo(String nombreArchivo) {
        return Paths.get(storageLocation).resolve(nombreArchivo);
    }

    public Resource cargarComoRecurso(String nombreArchivo) throws Exception {
        try {
            Path archivo = cargarArchivo(nombreArchivo);
            Resource recurso = new UrlResource(archivo.toUri());
            if (recurso.exists() || recurso.isReadable()) {
                return recurso;
            } else {
                throw new Exception("No se pudo encontrar el archivo" + nombreArchivo);
            }
        } catch (MalformedURLException e) {
            throw new Exception("No se pudo encontrar el archivo" + nombreArchivo);

        }
    }

    public void eliminarArchivo(String nombreArchivo) {
        Path archivo = cargarArchivo(nombreArchivo);
        try {
            FileSystemUtils.deleteRecursively(archivo);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
