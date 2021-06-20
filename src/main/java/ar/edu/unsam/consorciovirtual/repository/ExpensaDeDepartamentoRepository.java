package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensaDeDepartamentoRepository extends JpaRepository<ExpensaDeDepartamento, Long> {
    List<ExpensaDeDepartamento> findByAnuladaFalse();
}
