package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.domainDTO.ExpensaDeDepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.service.*;
import ar.edu.unsam.consorciovirtual.utils.GeneradorDeExpensas;
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
    public List<ExpensaDeDepartamentoDTOParaListado> buscarTodos(@RequestParam Long idLogueado, @RequestParam(defaultValue="") String palabraBuscada) {
        return this.expensaDeDepartamentoService.buscarTodos(idLogueado, palabraBuscada);
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
    @GetMapping("/expensas/createPorImportePredefinido")
    public void generarExpensasPorImportePredefinido(@RequestParam String periodo, @RequestParam Double importeComunes, @RequestParam Double importeExtraordinarias){
        this.generadorDeExpensas.generarExpensasPorImportePredefinido(importeComunes, importeExtraordinarias, stringToYearMonth(periodo));
    }

    @Transactional
    @GetMapping("/expensas/anular")
    public void anularExpensasDeUnPeriodo(@RequestParam String periodo){
        YearMonth periodoABuscar = stringToYearMonth(periodo);
        this.expensaGeneralService.anularExpensasPorPeriodo(periodoABuscar);
    }

    @GetMapping("/expensageneral/periodo")
    public ExpensaGeneral buscarPorPeriodoGeneral(@RequestParam String periodo) {
        YearMonth periodoABuscar = stringToYearMonth(periodo);
        return expensaGeneralService.buscarPorPeriodo(periodoABuscar);
    }
    
    @GetMapping("/expensadepto/periodo")
    public List<ExpensaDeDepartamento> buscarPorPeriodoDepartamento(@RequestParam String periodo) {
        YearMonth periodoABuscar = stringToYearMonth(periodo);
        return expensaDeDepartamentoService.buscarPorPeriodo(periodoABuscar);
    }


    public YearMonth stringToYearMonth(String periodo){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        var periodoFormat = periodo.replaceAll("^\"+|\"+$", "");
        return YearMonth.parse(periodoFormat, formatter);
    }

    /*El idUsuario es el id del pagador, puede no corresponder con el due??o o inquilino del departamento
    dado que si lo paga el administrador de manera manual quedar?? registrado su id. De esta manera se
    puede identificar cuales se pagaron por MP y cu??les manualmente(efectivo).*/
    @PutMapping("/expensas/pagar/{idExpensa}/{idUsuario}")
    public void pagarExpensa(@PathVariable Long idExpensa, @PathVariable Long idUsuario){
        expensaDeDepartamentoService.pagarExpensa(idExpensa, idUsuario);
    }

}
