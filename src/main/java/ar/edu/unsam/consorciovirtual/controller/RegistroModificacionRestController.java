package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.RegistroModificacion;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.service.RegistroModificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegistroModificacionRestController {

    private final RegistroModificacionService registroModificacionService;

    @GetMapping("/registroModificacion")
    public List<RegistroModificacion> buscarPorTipoYId(@RequestParam TipoRegistro tipoRegistro, @RequestParam Long idModificado) {
        return registroModificacionService.buscarPorTipoYId(tipoRegistro, idModificado);
    }
}
