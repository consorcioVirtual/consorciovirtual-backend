package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnicaDTOParaListado;
import ar.edu.unsam.consorciovirtual.service.SolicitudTecnicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/solicitud/{id}")
    public SolicitudTecnica buscarPorId(@PathVariable Long id) {
        return this.solicitudTecnicaService.buscarPorId(id);
    }

    @PutMapping("/solicitud/modificar")
    public SolicitudTecnica modificarSolicitud(@RequestParam Long idLogueado, @RequestBody SolicitudTecnica solicitudTecnica) {
        return this.solicitudTecnicaService.modificarSolicitud(idLogueado, solicitudTecnica);
    }

    @PutMapping("/solicitud/crear")
    public SolicitudTecnica crearSolicitud(@RequestBody SolicitudTecnica solicitudTecnica){
        return solicitudTecnicaService.registrarSolicitud(solicitudTecnica);
    }

    @PutMapping("/solicitud/eliminar/{id}")
    public void bajaLogicaSolicitud(@PathVariable Long id) {
        solicitudTecnicaService.bajaLogicaSolicitud(id);
    }


}
