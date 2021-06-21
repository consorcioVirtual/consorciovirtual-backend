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
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class ExpensaRestController {

    private final ExpensaDeDepartamentoService expensaDeDepartamentoService;
    private final ExpensaGeneralService expensaGeneralService;
    private final GeneradorDeExpensas generadorDeExpensas;

    @GetMapping("/expensas")
    public List<ExpensaDeDepartamentoDTOParaListado> buscarTodos() {
        return this.expensaDeDepartamentoService.buscarTodos();
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
    @PostMapping("/expensas/createPorImporteDeGastos/{periodo}")
    public void generarExpensasPorImporteDeGastos(@PathVariable YearMonth periodo){
        this.generadorDeExpensas.generarExpensasPorImporteDeGastos(periodo);
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
