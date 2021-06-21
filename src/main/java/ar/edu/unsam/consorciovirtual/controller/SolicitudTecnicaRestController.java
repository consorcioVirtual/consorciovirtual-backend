package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnicaDTOParaListado;
import ar.edu.unsam.consorciovirtual.service.DepartamentoService;
import ar.edu.unsam.consorciovirtual.service.SolicitudTecnicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class SolicitudTecnicaRestController {

    private final SolicitudTecnicaService solicitudTecnicaService;

    @GetMapping("/solicitudes")
    public List<SolicitudTecnicaDTOParaListado> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return this.solicitudTecnicaService.buscarTodos(palabraBuscada);
    }

}
