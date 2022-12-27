package com.gestionProductos.gestionProductos.controlador;

import com.gestionProductos.gestionProductos.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "registro_usuario";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(name = "nombre") String nombre, @RequestParam(name = "apellido") String apellido,
            @RequestParam(name = "email") String email, @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password, @RequestParam(name = "password2") String password2, ModelMap modelo) throws Exception {
        try {
            usuarioServicio.registrar(nombre, apellido, email, username, password, password2);
            modelo.put("exito", "Usuario registrado correctamente");
            return "index";
        } catch (Exception exception) {
            modelo.put("error", exception.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            modelo.put("username", username);
            modelo.put("password", password);
            modelo.put("password2", password2);
            return "registro_usuario";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "usuario o contraseña invalido");
        }
        return "login";
    }
}
