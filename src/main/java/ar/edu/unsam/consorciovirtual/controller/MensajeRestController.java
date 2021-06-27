package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Mensaje;
import ar.edu.unsam.consorciovirtual.service.MensajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class MensajeRestController {
    @Autowired
    private final MensajeService mensajeService;

    //Sin parámetro de busqueda, si lo hacemos tipo Wathsapp la busqueda podría ser en el front directamente.
    //DEFINIR: como limitamos la cantidad de mensajes que se cargan
    @GetMapping("/mensajes")
    public List<Mensaje> getMensajes(){
        return mensajeService.getMensajes();
    }

    //No se le pasa el autor completo desde el front, se le carga en el back por idAutor
    /*No se le pasa el mensajeCitado completo desde el front,
    se le carga en el back por idMensajeCitado, que es pasado como requestParam (no es obligatorio pasar algo)*/
    @PostMapping("/mensajes/create/{idAutor}")
    public void createMensaje(@PathVariable Long idAutor, @RequestBody Mensaje mensajeNuevo, @RequestParam(required=false) Long idMensajeCitado){
        mensajeService.createMensaje(idAutor, mensajeNuevo, idMensajeCitado);
    }

    //DEFINIR: Si se puede eliminar mensaje (de poder hacerlo, creo que tiene que haber un límite de tiempo)
}
