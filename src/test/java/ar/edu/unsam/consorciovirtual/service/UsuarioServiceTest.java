package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Buscar por username devuelve usuario")
    public void buscarPorUsername() {
       /* Usuario usuarioABuscar = new Usuario();
        usuarioABuscar.setUsername("santi");

        usuarioService.registrar(usuarioABuscar);

        Usuario usuario = usuarioService.buscarPorId(1);

        assertNotNull(usuario);*/
    }
}

