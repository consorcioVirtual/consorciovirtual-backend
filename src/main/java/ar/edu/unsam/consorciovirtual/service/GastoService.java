package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Gasto;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class GastoService {

    private final GastoRepository gastoRepository;
    private final RegistroModificacionService registroModificacionService;
    private final UsuarioService usuarioService;

    private Boolean importeDeGastoValido(Gasto nuevoGasto){
        return (!nuevoGasto.tieneItems() || (nuevoGasto.tieneItems() && nuevoGasto.importeCoincideConSumaDeIntems()))
                && nuevoGasto.getImporte() != 0 ;
    }

    public void ingresarNuevoGasto(Gasto nuevoGasto){
        if(this.importeDeGastoValido(nuevoGasto)){
            gastoRepository.save(nuevoGasto);
        } else throw new IllegalArgumentException("Error en los importes ingresados");
    }

    public List<Gasto> buscarTodos(String palabraBuscada) {
        Double importe = busquedaToDouble(palabraBuscada);
        return gastoRepository.findByTituloContainingOrImporte(palabraBuscada, importe);
    }

    private Double busquedaToDouble(String palabraBuscada) {
        try {
            return Double.valueOf(palabraBuscada);
        } catch (NumberFormatException ex){
            return null;
        }
    }

    public void registrarTodos(List<Gasto> gastos) {
        gastoRepository.saveAll(gastos);
    }

    public Gasto buscarPorId(Long id) {
        return gastoRepository.findById(id).orElseThrow(() -> new RuntimeException("Gasto no encontrado"));
    }

    public Gasto modificar(Long idLogueado, Gasto gastoActualizado) {
        registroModificacionService.guardarPorTipoYId(TipoRegistro.GASTO, gastoActualizado.getId(), usuarioService.getNombreYApellidoById(idLogueado));
        return gastoRepository.save(gastoActualizado);
    }

    public void bajaLogica(Long id){
        Gasto gasto = gastoRepository.findById(id).get();
        //gasto.setBajaLogica(true);
        registroModificacionService.eliminarTodosPorTipoYId(TipoRegistro.GASTO, id);

        gastoRepository.save(gasto);
    }

    public List<Gasto> buscarPorPeriodo(YearMonth periodo){
        return gastoRepository.findGastosByPeriodo(periodo);
    }

//    public List<Gasto> algo(String palabraBuscada) {
//        System.out.println(palabraBuscada);
//        return gastoRepository.findByFechaDeCreacionContaining(palabraBuscada);
//    }
}
