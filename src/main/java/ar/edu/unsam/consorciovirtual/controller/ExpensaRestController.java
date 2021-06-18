package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Expensa;
import ar.edu.unsam.consorciovirtual.service.ExpensaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class ExpensaRestController {

    private final ExpensaService expensaService;

    @GetMapping("/expensas")
    public List<Expensa> buscarTodos() {
        return this.expensaService.buscarTodos();
    }

    @GetMapping("/expensas/activas")
    public List<Expensa> buscarTodosSinAnuladas() {
        return this.expensaService.buscarTodosSinAnuladas();
    }

}
