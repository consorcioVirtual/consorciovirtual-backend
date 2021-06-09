package ar.edu.unsam.consorciovirtual;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Bootstrap implements InitializingBean {

    private final UsuarioService usuarioService;

    @Override
    public void afterPropertiesSet() throws Exception {
        createAllusers();
    }

    private Usuario createUser(String nombre, String apellido, String correo, String dni, LocalDate fechaNacimiento, String username) {
        Usuario newUser = new Usuario();
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        newUser.setCorreo(correo);
        newUser.setDni(dni);
        newUser.setFechaNacimiento(fechaNacimiento);
        newUser.setUsername(username);
        return newUser;
    }

    private void createAllusers(){
        var santir = createUser("Santiago", "Ranieri", "santi.ranieri@gmail.com", "38.830.200", LocalDate.of(1995, 8, 25), "santiranieri");
        var santilr = createUser("Santiago", "Lopez Roth", "santi_kpo97@yahoo.com", "40.123.423", LocalDate.of(1995, 8, 25), "santilr");
        var santil = createUser("Santiago", "Lorenzo", "santilorenzo@gmail.com", "42.543.231", LocalDate.of(1995, 8, 25), "santil");
        var pablo = createUser("Pablo", "Vigliero", "pablitovig@hotmail.com", "36.350.120", LocalDate.of(1995, 8, 25), "pablito");
        var nahue = createUser("Nahue", "Ramos", "nahueramos@gmail.com", "36.765.908", LocalDate.of(1995, 8, 25), "nahue");

        List<Usuario> usuarios = List.of(santir, santilr, santil, pablo, nahue);
        usuarioService.registrarTodos(usuarios);
    }

}
