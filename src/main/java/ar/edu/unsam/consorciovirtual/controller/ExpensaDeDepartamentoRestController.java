package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.repository.GastoRepository;
import ar.edu.unsam.consorciovirtual.service.ExpensaDeDepartamentoService;
import ar.edu.unsam.consorciovirtual.service.GeneradorDeExpensas;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class ExpensaDeDepartamentoRestController {

    private final ExpensaDeDepartamentoService expensaDeDepartamentoService;
    private final GeneradorDeExpensas generadorDeExpensas;

    @GetMapping("/expensas")
    public List<ExpensaDeDepartamento> buscarTodos() {
        return this.expensaDeDepartamentoService.buscarTodos();
    }

    @GetMapping("/expensas/activas")
    public List<ExpensaDeDepartamento> buscarTodosSinAnuladas() {
        return this.expensaDeDepartamentoService.buscarTodosSinAnuladas();
    }


}
