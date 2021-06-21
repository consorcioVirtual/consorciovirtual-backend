package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.ExpensaGeneral;
import ar.edu.unsam.consorciovirtual.repository.ExpensaGeneralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ExpensaGeneralService {

    private final ExpensaGeneralRepository expensaGeneralRepository;

    @Transactional
    public void anularExpensasPorPeriodo(YearMonth periodo) {
        List<ExpensaGeneral> expensaAAnular = expensaGeneralRepository.findByPeriodoAndAnuladaFalse(periodo);
        if(!expensaAAnular.isEmpty()){
            expensaAAnular.get(0).anularExpensa();
        } else throw new IllegalArgumentException("No existe expensa activa de ese periodo");
    }
}
