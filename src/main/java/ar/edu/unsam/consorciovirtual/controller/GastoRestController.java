package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Gasto;
import ar.edu.unsam.consorciovirtual.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DateFormatter;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class GastoRestController {

    @Autowired
    private final GastoService gastoService;

    @PutMapping("/gastos/crear")
    public void ingresarNuevoGasto(@RequestBody Gasto nuevoGasto) {
        gastoService.ingresarNuevoGasto(nuevoGasto);
    }

    @GetMapping("/gastos")
    public List<Gasto> buscarTodos(@RequestParam(defaultValue = "") String palabraBuscada) {
        return this.gastoService.buscarTodos(palabraBuscada);
    }

    @GetMapping("/gasto/{id}")
    public Gasto buscarPorId(@PathVariable Long id) {
        return this.gastoService.buscarPorId(id);
    }

    @PutMapping("/gasto/modificar")
    public Gasto modificarGasto(@RequestParam Long idLogueado, @RequestBody Gasto gasto) {
        return this.gastoService.modificar(idLogueado, gasto);
    }

    @PutMapping("/gasto/eliminar/{id}")
    public void bajaLogicaGasto(@PathVariable Long id) {
        gastoService.bajaLogica(id);
    }

    @GetMapping("/gastos/periodo")
    public List<Gasto> buscarPorPeriodo(@RequestParam String periodo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        var periodoFormat = periodo.replaceAll("^\"+|\"+$", "");
        YearMonth periodoABuscar = YearMonth.parse(periodoFormat, formatter);
        return gastoService.buscarPorPeriodo(periodoABuscar);
    }
//    @GetMapping("/endpointFalopa")
//    public List<Gasto> buscarPorAlgo(@RequestParam(defaultValue="") String palabraBuscada){
//        return this.gastoService.algo(palabraBuscada);
//    }
}