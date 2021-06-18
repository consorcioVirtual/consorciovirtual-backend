package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Expensa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensaRepository extends JpaRepository<Expensa, Long> {
    List<Expensa> findByAnuladaFalse();
}
