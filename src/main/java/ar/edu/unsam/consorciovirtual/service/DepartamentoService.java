package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final UsuarioService usuarioService;
    private final RegistroModificacionService registroModificacionService;

    public List<DepartamentoDTOParaListado> buscarTodos(String palabraBuscada, Long idLogueado) {
        List<Departamento> departamentos;
        if(usuarioService.usuarioEsAdminDelConsorcio(idLogueado) || usuarioService.usuarioEsAdminDeLaApp(idLogueado)){
            departamentos = departamentoRepository.findByNroDepartamentoContainingAndBajaLogicaFalseOrNombrePropietarioContainingAndBajaLogicaFalseOrNombreInquilinoContainingAndBajaLogicaFalseOrPisoContainingAndBajaLogicaFalse(palabraBuscada, palabraBuscada, palabraBuscada, palabraBuscada);
        } else {
            departamentos = departamentoRepository.buscarPorUsuarioYFiltro(idLogueado, palabraBuscada, palabraBuscada, palabraBuscada, palabraBuscada);
        }

       List<DepartamentoDTOParaListado> dtos = departamentos.stream().map(DepartamentoDTOParaListado::fromDepartamento).collect(Collectors.toList());
        dtos.forEach(this::agregarUltimaModificacion);

        return dtos;
    }

    public List<Departamento> registrarTodos(List <Departamento> listaDepartamentos) {
        return departamentoRepository.saveAll(listaDepartamentos);
    }

    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
    }

    public List<Departamento> buscarPorPropietarioOInquilino(Long id) {
        return departamentoRepository.buscarPorPropietarioOInquilino(id);
    }

    //Lista de ID's de deptos
    public List<Long> buscarIdPorPropietario(Long id) {
        return departamentoRepository.buscarIdPorPropietario(id);
    }

    //Lista de ID's de deptos
    public List<Long> buscarIdPorInquilino(Long id) {
        return departamentoRepository.buscarIdPorInquilino(id);
    }


    public Departamento modificarDepartamento(Long idLogueado, DepartamentoConUsuarios departamento) {
        validarModificacion(idLogueado);
        Departamento updatedDepartment = asignarPropietarioEInquilino(departamento);
        registroModificacionService.guardarPorTipoYId(TipoRegistro.DEPARTAMENTO, departamento.getDepartamento().getId(), usuarioService.getNombreYApellidoById(idLogueado));
        return departamentoRepository.save(updatedDepartment);
    }

    public Departamento registrarDepartamento(DepartamentoConUsuarios departamentoConUsuarios) {
        Departamento newDepartment = asignarPropietarioEInquilino(departamentoConUsuarios);
        return departamentoRepository.save(newDepartment);
    }

    private Departamento asignarPropietarioEInquilino(DepartamentoConUsuarios departamentoConUsuarios){
        Departamento departamento = departamentoConUsuarios.getDepartamento();

        Usuario _propietario = usuarioService.buscarPorId(departamentoConUsuarios.getPropietarioId());
        departamento.setPropietario(_propietario);
        departamento.setNombrePropietario(_propietario.getNombreYApellido());

        if(departamentoConUsuarios.getInquilinoId() != null) {
            Usuario _inquilino = usuarioService.buscarPorId(departamentoConUsuarios.getInquilinoId());
            departamento.setInquilino(_inquilino);
            departamento.setNombreInquilino(_inquilino.getNombreYApellido());
        }else{
            departamento.setInquilino(null);
            departamento.setNombreInquilino(null);
        }
        return departamento;
    }

    public void bajaLogica(Long idLogueado, Long idABorrar){
        validarBaja(idLogueado);
        Departamento departamento = departamentoRepository.findById(idABorrar).get();
        departamento.setBajaLogica(true);
        registroModificacionService.eliminarTodosPorTipoYId(TipoRegistro.DEPARTAMENTO, idABorrar);

        departamentoRepository.save(departamento);
    }

    private void validarBaja(Long idLogueado) {
        if(!usuarioService.usuarioEsAdminDeLaApp(idLogueado)){
            throw new SecurityException("No tiene permisos para eliminar departamentos.");
        }
    }

    private void validarModificacion(Long idLogueado){
        if(!usuarioService.usuarioEsAdminDeLaApp(idLogueado)){
            throw new SecurityException("No tiene permisos para modificar departamentos.");
        }
    }
    public long count(){
        return departamentoRepository.count();
    }


    private void agregarUltimaModificacion(@NotNull DepartamentoDTOParaListado dto){
        String fechaUltimaModificacion = registroModificacionService.getUltimaModificacion(TipoRegistro.DEPARTAMENTO, dto.getId());
        dto.setUltimaModificacion(fechaUltimaModificacion);
    }
}
