package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import ar.edu.unsam.consorciovirtual.domainDTO.SolicitudTecnicaDTOParaListado;
import ar.edu.unsam.consorciovirtual.service.SolicitudTecnicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class SolicitudTecnicaRestController {

    private final SolicitudTecnicaService solicitudTecnicaService;

    @GetMapping("/solicitudes")
    public Set<SolicitudTecnicaDTOParaListado> buscarTodos(@RequestParam Long idLogueado, @RequestParam(defaultValue="") String palabraBuscada) {
        return this.solicitudTecnicaService.buscarTodos(idLogueado, palabraBuscada).stream().collect(Collectors.toSet());
    }

    @GetMapping("/solicitud/{id}")
    public SolicitudTecnica buscarPorId(@PathVariable Long id) {
        return this.solicitudTecnicaService.buscarPorId(id);
    }

    @PutMapping("/solicitud/modificar")
    public SolicitudTecnica modificarSolicitud(@RequestParam Long idLogueado, @RequestBody SolicitudTecnica solicitudTecnica) throws DataConsistencyException {
        return this.solicitudTecnicaService.modificarSolicitud(idLogueado, solicitudTecnica);
    }

    @PutMapping("/solicitud/crear")
    public SolicitudTecnica crearSolicitud(@RequestBody SolicitudTecnica solicitudTecnica) throws DataConsistencyException {
        return solicitudTecnicaService.registrarSolicitud(solicitudTecnica);
    }

    @PutMapping("/solicitud/eliminar/{id}")
    public void bajaLogicaSolicitud(@PathVariable Long id) {
        solicitudTecnicaService.bajaLogicaSolicitud(id);
    }


}
