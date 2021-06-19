package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.DepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
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

    //TODO: Como está, rompe muy feo. Ver eso + la  forma de buscar por nombre de inquilino/usuario.
    //TODO: Se intentó con los ids directamente pero rompe.
    public List<DepartamentoDTOParaListado> buscarTodos(String palabraBuscada) {
        List<Departamento> departamentos = departamentoRepository.findByNroDepartamentoContainingAndBajaLogicaFalseOrNombrePropietarioContainingAndBajaLogicaFalseOrNombreInquilinoContainingAndBajaLogicaFalse(palabraBuscada, palabraBuscada, palabraBuscada);
        return departamentos.stream().map(x -> DepartamentoDTOParaListado.fromDepartamento(x)).collect(Collectors.toList());
    }

    public List<Departamento> registrarTodos(List <Departamento> listaDepartamentos) {
        return departamentoRepository.saveAll(listaDepartamentos);
    }

    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
    }

    public Departamento modificar(Departamento departamentoActualizado) {
        Departamento departamentoAnterior = departamentoRepository.findById(departamentoActualizado.getId()).get();

        departamentoActualizado.setPropietario(departamentoAnterior.getPropietario());
        departamentoActualizado.setInquilino(departamentoAnterior.getInquilino());
        // registroModificacionService.guardarPorTipoYId(TipoRegistro.DEPARTAMENTO, departamentoActualizado.getId());

        return departamentoRepository.save(departamentoActualizado);
    }

    public Departamento registrarDepartamento(Departamento departamento) {
        Usuario _inquilino = departamento.getInquilino();
        Usuario _propietario = departamento.getPropietario();

        if( _inquilino != null){
            departamento.setNombreInquilino(_inquilino.getNombreYApellido());
        }

        departamento.setNombrePropietario(_propietario.getNombreYApellido());
        return departamentoRepository.save(departamento);
    }

//    private Integer busquedaToInteger(String palabraBuscada){
//        try{
//            return Integer.parseInt(palabraBuscada);
//        } catch (NumberFormatException ex){
//            return 0;
//        }
//    }

}
