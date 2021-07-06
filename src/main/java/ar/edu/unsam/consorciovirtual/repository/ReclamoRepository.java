package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {

    List<Reclamo> findByIdAndBajaLogicaFalseOrAutorNombreContainingAndBajaLogicaFalseOrAutorApellidoContainingAndBajaLogicaFalseAndBajaLogicaFalseOrAsuntoContainingAndBajaLogicaFalseOrEstadoNombreEstadoContainingAndBajaLogicaFalse(Long id, String nombre, String apellido, String asunto, String nombreEstado);
}
