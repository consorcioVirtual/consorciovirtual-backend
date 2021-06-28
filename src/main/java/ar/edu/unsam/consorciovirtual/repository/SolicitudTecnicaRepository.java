package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudTecnicaRepository extends JpaRepository<SolicitudTecnica, Long> {
    List<SolicitudTecnica> findByBajaLogicaFalse();

    List<SolicitudTecnica> findByIdAndBajaLogicaFalseOrNombreAutorContainingAndBajaLogicaFalseOrTituloContainingAndBajaLogicaFalseOrEstadoNombreEstadoContainingAndBajaLogicaFalse(Long id, String nombreAutor, String titulo, String nombreEstado);
}

