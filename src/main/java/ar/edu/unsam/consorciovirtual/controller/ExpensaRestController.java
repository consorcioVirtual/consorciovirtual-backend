package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.service.ExpensaDeDepartamentoService;
import ar.edu.unsam.consorciovirtual.service.ExpensaGeneralService;
import ar.edu.unsam.consorciovirtual.service.ExtractorDatoDeJSON;
import ar.edu.unsam.consorciovirtual.service.GeneradorDeExpensas;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class ExpensaRestController {

    private final ExpensaDeDepartamentoService expensaDeDepartamentoService;
    private final ExpensaGeneralService expensaGeneralService;
    private final GeneradorDeExpensas generadorDeExpensas;

    @GetMapping("/expensas")
    public List<ExpensaDeDepartamentoDTOParaListado> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return this.expensaDeDepartamentoService.buscarTodos(palabraBuscada);
    }

    @GetMapping("/expensas/activas")
    public List<ExpensaDeDepartamentoDTOParaListado> buscarTodosSinAnuladas() {
        return this.expensaDeDepartamentoService.buscarTodosSinAnuladas();
    }

    @GetMapping("/expensas/{id}")
    public ExpensaDeDepartamento buscarPorId(@PathVariable Long id) {
        return this.expensaDeDepartamentoService.buscarPorId(id);
    }

    @Transactional
    @GetMapping("/expensas/createPorImporteDeGastos")
    public void generarExpensasPorImporteDeGastos(@RequestParam String periodo){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        var periodoFormat = periodo.replaceAll("^\"+|\"+$", "");
        YearMonth periodoABuscar = YearMonth.parse(periodoFormat, formatter);
        this.generadorDeExpensas.generarExpensasPorImporteDeGastos(periodoABuscar);
    }

    @Transactional
    @PostMapping("/expensas/createPorImportePredefinido/{periodo}")
    public void generarExpensasPorImportePredefinido(@PathVariable YearMonth periodo, @RequestBody String importes){
        Double importeComunes = ExtractorDatoDeJSON.extraerDoubleDeJson(importes, "importeComunes");
        Double importeExtraordinarias = ExtractorDatoDeJSON.extraerDoubleDeJson(importes, "importeExtraordinarias");
        this.generadorDeExpensas.generarExpensasPorImportePredefinido(importeComunes, importeExtraordinarias, periodo);
    }

    @Transactional
    @PutMapping("/expensas/anular/{periodo}")
    public void anularExpensasDeUnPeriodo(@PathVariable YearMonth periodo){
        this.expensaGeneralService.anularExpensasPorPeriodo(periodo);
    }



}
