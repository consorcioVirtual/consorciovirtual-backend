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

    public List<DepartamentoDTOParaListado> buscarTodos(String palabraBuscada) {
        List<Departamento> departamentos = departamentoRepository.findByNroDepartamentoContainingAndBajaLogicaFalseOrNombrePropietarioContainingAndBajaLogicaFalseOrNombreInquilinoContainingAndBajaLogicaFalseOrPisoContainingAndBajaLogicaFalse(palabraBuscada, palabraBuscada, palabraBuscada, palabraBuscada);
        return departamentos.stream().map(x -> DepartamentoDTOParaListado.fromDepartamento(x)).collect(Collectors.toList());
    }

    public List<Departamento> registrarTodos(List <Departamento> listaDepartamentos) {
        return departamentoRepository.saveAll(listaDepartamentos);
    }

    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
    }

    public Departamento modificarDepartamento(Departamento departamentoActualizado) {
        Departamento departamentoAnterior = departamentoRepository.findById(departamentoActualizado.getId()).get();

        departamentoActualizado.setPropietario(departamentoAnterior.getPropietario());
        departamentoActualizado.setInquilino(departamentoAnterior.getInquilino());
        // registroModificacionService.guardarPorTipoYId(TipoRegistro.DEPARTAMENTO, departamentoActualizado.getId());

        return departamentoRepository.save(departamentoActualizado);
    }

    public Departamento registrarDepartamento(Departamento departamento) {
//      TODO: VER SI EL "getInquilino" Y "getPropietario" NO TRAEN LOS ID's.
//        EN ESE CASO, LLAMAR AL UsuarioService PARA TRAER EL USUARIO CORRESPONDIENTE
        Usuario _inquilino = departamento.getInquilino();
        Usuario _propietario = departamento.getPropietario();

        if( _inquilino != null){
            departamento.setNombreInquilino(_inquilino.getNombreYApellido());
        }

        departamento.setNombrePropietario(_propietario.getNombreYApellido());
        return departamentoRepository.save(departamento);
    }

    public void bajaLogica(Long id){
        Departamento departamento = departamentoRepository.findById(id).get();
        departamento.setBajaLogica(true);

        departamentoRepository.save(departamento);
    }

}
