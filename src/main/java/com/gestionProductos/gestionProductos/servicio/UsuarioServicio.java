package com.gestionProductos.gestionProductos.servicio;

import com.gestionProductos.gestionProductos.entidad.Usuario;
import com.gestionProductos.gestionProductos.enumeracion.Rol;
import com.gestionProductos.gestionProductos.repositorio.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FotoService fotoService;
   
    @Transactional
    public void registrar(String nombre, String apellido, String email, String username, String password, String password2,  MultipartFile foto) throws Exception {
        validar(nombre, apellido, email, username, password, password2, foto);
        Usuario usuario = new Usuario();
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setNombre(nombre);
        usuario.setPassword( new BCryptPasswordEncoder().encode(password) );
        usuario.setRol(Rol.USER);
        usuario.setUsername(username);
        String rutaPortada= fotoService.almacenarArchivo(foto);
        usuario.setImagen(rutaPortada);
//        usuario.setImagen(fotoService.copiar(foto));
        usuarioRepositorio.save(usuario);
    }

    private void validar(String nombre, String apellido, String email, String username, String password, String password2, MultipartFile foto) throws Exception {
        if (nombre.isEmpty() || nombre == null) {
            throw new Exception("El nombre no puede estar estar vacío");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new Exception("El nombre no puede estar estar vacío");
        }
        if (username.isEmpty() || username == null) {
            throw new Exception("El nombre no puede estar estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new Exception("El email no puede estar vacio");
        }
        if (password.isEmpty()) {
            throw new Exception("La contraseña no puede estar vacía");
        }
        if (password2.isEmpty()) {
            throw new Exception("Debe repetir la contraseña");
        }
        if (password.length() < 5) {
            throw new Exception("La contraseña debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new Exception("Las contraseñas ingresadas deben ser iguales");
        }
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            throw new Exception("El email ya se encuentra registrado");
        }
       
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }
}
