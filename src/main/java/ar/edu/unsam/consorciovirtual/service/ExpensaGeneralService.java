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

    private Boolean contieneExpensasPagas(ExpensaGeneral expensaGeneral){
        return expensaGeneral.getListaDeExpensas().stream().anyMatch(exp -> exp.estaPaga());
    }

    @Transactional
    public void anularExpensasPorPeriodo(YearMonth periodo) {
        List <ExpensaGeneral> posiblesExpensas = expensaGeneralRepository.findByPeriodoAndAnuladaFalse(periodo);
        if(!posiblesExpensas.isEmpty()){
            ExpensaGeneral expensaAAnular = posiblesExpensas.get(0);
            if(!contieneExpensasPagas(expensaAAnular)){
                expensaAAnular.anularExpensa();
            }else throw new IllegalArgumentException("No se puede anular esta expensa, dado que hay al menos " +
                    "una expensa correspondiente a este período, ya fue abonada");
        } else throw new IllegalArgumentException("No existe expensa activa de ese periodo");
    }

    public ExpensaGeneral buscarPorPeriodo(YearMonth periodo){
        return expensaGeneralRepository.findOneByPeriodoAndAnuladaFalse(periodo);
    }
}
