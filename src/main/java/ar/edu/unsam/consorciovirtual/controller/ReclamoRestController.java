package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Reclamo;
import ar.edu.unsam.consorciovirtual.service.ReclamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ReclamoRestController {

    private final ReclamoService reclamoService;

    @GetMapping("/reclamos")
    public List<Reclamo> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return reclamoService.buscarTodos(palabraBuscada);
    }

    @GetMapping("/reclamo/{id}")
    public Reclamo buscarPorId(@PathVariable Long id) {
        return reclamoService.buscarPorId(id);
    }

    @PostMapping("/reclamo/crear")
    public Reclamo crearReclamo(@RequestBody Reclamo reclamo){
        return reclamoService.registrarReclamo(reclamo);
    }

    @PutMapping("/reclamo/modificar")
    public Reclamo modificarReclamo(@RequestBody Reclamo reclamo) {
        return reclamoService.modificarReclamo(reclamo);
    }

    @DeleteMapping("/reclamo/eliminar/{id}")
    public void bajaLogicaReclamo(@PathVariable Long id) {
        reclamoService.bajaLogicaReclamo(id);
    }

}
