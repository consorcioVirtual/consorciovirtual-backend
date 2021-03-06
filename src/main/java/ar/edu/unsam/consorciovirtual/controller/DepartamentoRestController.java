package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.domainDTO.DepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domainDTO.DepartamentoDTOReducido;
import ar.edu.unsam.consorciovirtual.service.DepartamentoService;
import com.fasterxml.jackson.annotation.JsonView;
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
    public List<DepartamentoDTOParaListado> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada, @RequestParam Long idLogueado) {
        return this.departamentoService.buscarTodos(palabraBuscada, idLogueado);
    }

    @GetMapping("/departamento/{id}")
    public Departamento buscarPorUsername(@PathVariable Long id) {
        return this.departamentoService.buscarPorId(id);
    }

    @JsonView(Views.DepartamentoPisoNro.class)
    @GetMapping("/departamentos/user/{id}")
    public List<Departamento> buscarDepartamentoPorPropietarioOInquilino(@PathVariable Long id) {
        return this.departamentoService.buscarPorPropietarioOInquilino(id);
    }

    @PutMapping("/departamento/modificar")
    public Departamento modificarDepartamento(@RequestParam Long idLogueado, @RequestBody DepartamentoConUsuarios departamentoConUsuarios) throws DataConsistencyException {
        return this.departamentoService.modificarDepartamento(idLogueado, departamentoConUsuarios);
    }

    @PutMapping("/departamento/crear")
    public Departamento crearDepartamento(@RequestBody String body) throws JsonProcessingException, DataConsistencyException {
        DepartamentoConUsuarios newDepartment = new ObjectMapper().readValue(body, DepartamentoConUsuarios.class);
        return departamentoService.registrarDepartamento(newDepartment);
    }

    @DeleteMapping("/departamento/eliminar/{idABorrar}")
    public void bajaLogicaDepartamento(@RequestParam Long idLogueado, @PathVariable Long idABorrar) {
        departamentoService.bajaLogica(idLogueado, idABorrar);
    }


    @GetMapping("/cantidaddepartamentos")
    public long count() {
        return this.departamentoService.count();
    }

    @GetMapping("/departamentos/user/sin-inquilino/{idPropietario}")
    public List<DepartamentoDTOReducido> getDepartamentosDeUsuarioSinInquiino(@PathVariable Long idPropietario){
        return departamentoService.getDepartamentosDeUsuarioSinInquilino(idPropietario);
    }
}
