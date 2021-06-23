package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.ExpensaGeneral;
import ar.edu.unsam.consorciovirtual.domain.Gasto;
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
        ExpensaGeneral expensaAAnular = expensaGeneralRepository.findOneByPeriodoAndAnuladaFalse(periodo);
        if(expensaAAnular != null){
            expensaAAnular.anularExpensa();
        } else throw new IllegalArgumentException("No existe expensa activa de ese periodo");
    }

    public ExpensaGeneral buscarPorPeriodo(YearMonth periodo){
        return expensaGeneralRepository.findOneByPeriodoAndAnuladaFalse(periodo);
    }
}
