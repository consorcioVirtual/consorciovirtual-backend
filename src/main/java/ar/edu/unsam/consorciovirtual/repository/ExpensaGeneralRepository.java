package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.ExpensaGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;

public interface ExpensaGeneralRepository extends JpaRepository<ExpensaGeneral, Long> {

    List<ExpensaGeneral> findByPeriodo(YearMonth periodo);
}
