package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Gasto;
import ar.edu.unsam.consorciovirtual.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class GastoRestController {

    @Autowired
    private final GastoService gastoService;

    @PostMapping("/gastos/ingresar")
    public void ingresarNuevoGasto(@RequestBody Gasto nuevoGasto){
        gastoService.ingresarNuevoGasto(nuevoGasto);
    }

    @GetMapping("/gastos")
    public List<Gasto> buscarTodos() {
        return this.gastoService.buscarTodos();
    }

}
