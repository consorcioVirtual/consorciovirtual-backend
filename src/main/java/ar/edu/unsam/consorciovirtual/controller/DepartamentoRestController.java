package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.service.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class DepartamentoRestController {

    private final DepartamentoService departamentoService;

    @GetMapping("/departamentos")
    public List<Departamento> buscarTodos() {
        return this.departamentoService.buscarTodos();
    }

}
