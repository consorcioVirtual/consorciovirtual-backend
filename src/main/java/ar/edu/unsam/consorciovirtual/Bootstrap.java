package ar.edu.unsam.consorciovirtual;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class Bootstrap implements InitializingBean {

    private final UsuarioService usuarioService;

    @Override
    public void afterPropertiesSet() throws Exception {
        usuarioService.registrar(santi());
    }

    private Usuario santi() {
        Usuario santi = new Usuario();
        santi.setNombre("Santi");
        santi.setApellido("LR");
        santi.setCorreo("santiLR@gmail.com");
        santi.setDni("40333222");
        santi.setFechaNacimiento(LocalDate.of(1990,05,05));
        santi.setUsername("santi");
        return santi;
    }
}
