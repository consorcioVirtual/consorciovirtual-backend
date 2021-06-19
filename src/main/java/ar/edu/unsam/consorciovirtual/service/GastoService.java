package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Gasto;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class GastoService {

    private final GastoRepository gastoRepository;

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
        return gastoRepository.findAll();
    }

    public void registrarTodos(List<Gasto> gastos) {
        gastoRepository.saveAll(gastos);
    }

    public Gasto buscarPorId(Long id) {
        return gastoRepository.findById(id).orElseThrow(() -> new RuntimeException("Gasto no encontrado"));
    }

//    public List<Gasto> algo(String palabraBuscada) {
//        System.out.println(palabraBuscada);
//        return gastoRepository.findByFechaDeCreacionContaining(palabraBuscada);
//    }
}
