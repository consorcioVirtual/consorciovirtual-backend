package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
    List<Estado> findByBajaLogicaFalse();
}
