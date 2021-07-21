package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.service.GestorDeCorreo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GestorDeCorreoRestController {
    private final GestorDeCorreo gestorDeCorreo;

    @PostMapping("/enviarCorreo/usuarioNuevo")
    public void enviarCorreoAnuevoUsuario(@RequestBody Usuario usuario) throws JsonProcessingException {
        gestorDeCorreo.enviarMensajeNuevoUsuario(usuario);
    }

}
