package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GastoRepository  extends JpaRepository<Gasto, Long> {
}
