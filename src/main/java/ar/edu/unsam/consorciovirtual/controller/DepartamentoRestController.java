package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.DepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.service.DepartamentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class DepartamentoRestController {

    @Autowired
    private final DepartamentoService departamentoService;

    @GetMapping("/departamentos")
    public List<DepartamentoDTOParaListado> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return this.departamentoService.buscarTodos(palabraBuscada);
    }

    @GetMapping("/departamento/{id}")
    public Departamento buscarPorUsername(@PathVariable Long id) {
        return this.departamentoService.buscarPorId(id);
    }

    @PutMapping("/departamento/modificar")
    public Departamento modificarDepartamento(@RequestBody Departamento departamento) {
        return this.departamentoService.modificarDepartamento(departamento);
    }

    @PutMapping("/departamento/crear")
    public Departamento crearDepartamento(@RequestBody String body) throws JsonProcessingException {
        Departamento newDepartment = new ObjectMapper().readValue(body, Departamento.class);
        return departamentoService.registrarDepartamento(newDepartment);
    }

    @PutMapping("/departamento/eliminar/{id}")
    public void bajaLogicaDepartamento(@PathVariable Long id) {
        departamentoService.bajaLogica(id);
    }


    @GetMapping("/cantidaddepartamentos")
    public long count() {
        return this.departamentoService.count();
    }
}
