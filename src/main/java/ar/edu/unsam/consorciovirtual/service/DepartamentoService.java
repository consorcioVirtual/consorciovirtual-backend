package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.domainDTO.DepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domainDTO.DepartamentoDTOReducido;
import ar.edu.unsam.consorciovirtual.repository.DepartamentoRepository;
import ar.edu.unsam.consorciovirtual.utils.ValidationMethods;
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


    public Departamento modificarDepartamento(Long idLogueado, DepartamentoConUsuarios departamento) throws DataConsistencyException {
        Departamento updatedDepartment = asignarPropietarioEInquilino(departamento);
        validarModificacion(idLogueado, updatedDepartment);
        registroModificacionService.guardarPorTipoYId(TipoRegistro.DEPARTAMENTO, departamento.getDepartamento().getId(), usuarioService.getNombreYApellidoById(idLogueado));
        return departamentoRepository.save(updatedDepartment);
    }

    public Departamento registrarDepartamento(DepartamentoConUsuarios departamentoConUsuarios) throws DataConsistencyException {
        Departamento newDepartment = asignarPropietarioEInquilino(departamentoConUsuarios);
        validarDepartamento(newDepartment);
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

    private void validarModificacion(Long idLogueado, Departamento departamento) throws DataConsistencyException {
        if(!usuarioService.usuarioEsAdminDeLaApp(idLogueado)){
            throw new SecurityException("No tiene permisos para modificar departamentos.");
        }
        validarDepartamento(departamento);
    }

    public long count(){
        return departamentoRepository.count();
    }


    private void agregarUltimaModificacion(@NotNull DepartamentoDTOParaListado dto){
        String fechaUltimaModificacion = registroModificacionService.getUltimaModificacion(TipoRegistro.DEPARTAMENTO, dto.getId());
        dto.setUltimaModificacion(fechaUltimaModificacion);
    }

    public List<DepartamentoDTOReducido> getDepartamentosDeUsuarioSinInquilino(Long idPropietario){
        Usuario propietario = usuarioService.buscarPorId(idPropietario);
        List<Departamento> departamentos = departamentoRepository.findByPropietarioAndInquilinoIsNullAndBajaLogicaFalse(propietario);
        return departamentos.stream().map(DepartamentoDTOReducido::fromDepartamento).collect(Collectors.toList());
    }

    public void setInquilino(Long idDepartamento,Usuario inquilino){
        Departamento departamento = departamentoRepository.getById(idDepartamento);
        departamento.setInquilino(inquilino);
        departamento.setNombreInquilino(inquilino.getNombreYApellido());
        departamentoRepository.save(departamento);
    }

    public void quitarUsuarioEnDepartamentos(Usuario usuario){
        List<Departamento> departamentos = departamentoRepository.findByInquilinoAndBajaLogicaFalse(usuario);
        if( !departamentos.isEmpty() ) {
            departamentos.forEach( Departamento::quitarInquilino );
            departamentoRepository.saveAll(departamentos);
        }
    }

    private void validarDepartamento(Departamento departamento) throws DataConsistencyException {
        if(
           (!ValidationMethods.datoNull(departamento.getTorre()) && ValidationMethods.superaLimiteCaracteres(departamento.getTorre(), 3)) ||
           ValidationMethods.stringNullOSuperaLimite(departamento.getPiso(), 3) ||
           ValidationMethods.stringNullOSuperaLimite(departamento.getNroDepartamento(), 3) ||
           ValidationMethods.numberNullOSuperiorA(departamento.getPorcentajeExpensa().intValue(), 99) ||
           ValidationMethods.numberNullOSuperiorA(departamento.getMetrosCuadrados(), 9999) ||
           ValidationMethods.datoNull(departamento.getPropietario())
//           ValidationMethods.datoNull(departamento.getInquilino())
        ) throw new DataConsistencyException("Ha ocurrido un error con los datos ingresados. Verificalos e intentá de nuevo.");
    }
}
