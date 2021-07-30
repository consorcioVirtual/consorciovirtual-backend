package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.utils.ExtractorDatoDeJSON;
import ar.edu.unsam.consorciovirtual.utils.GestorDeCorreo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class GestorDeCorreoRestController {
    private final GestorDeCorreo gestorDeCorreo;

    @PostMapping("/enviarCorreo/usuarioNuevo")
    public void enviarCorreoAnuevoUsuario(@RequestBody Usuario usuario) throws JsonProcessingException {
        gestorDeCorreo.enviarMensajeNuevoUsuario(usuario);
    }

    @PostMapping("/enviarCorreo/notaEnReclamo/{idReclamo}/{idUsuario}")
    public void enviarCorreoNuevaNotaReclamo(@PathVariable Long idReclamo, @PathVariable Long idUsuario){
        gestorDeCorreo.enviarMensajeNuevaNota(idReclamo, idUsuario, "Reclamo");
    }

    @PostMapping("/enviarCorreo/notaEnSolicitud/{idSolicitud}/{idUsuario}")
    public void enviarCorreoNuevaNotaSolicitud(@PathVariable Long idSolicitud, @PathVariable Long idUsuario){
        gestorDeCorreo.enviarMensajeNuevaNota(idSolicitud, idUsuario, "Solicitud");
    }

    @PostMapping("/enviarCorreo/nuevoAnuncio/{idUsuario}")
    public void enviarCorreoNuevoAnuncio(@PathVariable Long idUsuario, @RequestParam() String titulo){
        gestorDeCorreo.enviarMensajeNuevoAnuncio(titulo, idUsuario);
    }

}
