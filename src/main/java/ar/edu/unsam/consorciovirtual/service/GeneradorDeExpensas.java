package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaGeneral;
import ar.edu.unsam.consorciovirtual.repository.DepartamentoRepository;
import ar.edu.unsam.consorciovirtual.repository.ExpensaDeDepartamentoRepository;
import ar.edu.unsam.consorciovirtual.repository.ExpensaGeneralRepository;
import ar.edu.unsam.consorciovirtual.repository.GastoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class GeneradorDeExpensas {

    private final GastoRepository gastoRepository;
    private final ExpensaGeneralRepository expensaGeneralRepository;
    private final ExpensaDeDepartamentoRepository expensaDeDepartamentoRepository;
    private final DepartamentoRepository departamentoRepository;

    public void generarExpensasPorImportePredefinido(Double importeComun, Double importeExtraordinaria, YearMonth periodo){
        generarExpensas(importeComun, importeExtraordinaria, periodo);
    }

    public Double calcularValorDeGastos(YearMonth periodo, String tipo){
        List<Double> gastos = gastoRepository.findImporteByPeriodoAndByTipo(periodo, tipo);
        return  gastos.stream()
                .mapToDouble(a -> a)
                .sum();
    }

    public void generarExpensasPorImporteDeGastos(YearMonth periodo){
        Double importeGastosComunes= calcularValorDeGastos(periodo, "Común");
        Double importeGastosExtraordinarias=calcularValorDeGastos(periodo, "Extraordinaria");

        generarExpensas(importeGastosComunes, importeGastosExtraordinarias, periodo);
    }

    private void generarExpensas(Double importeComun, Double importeExtraordinaria, YearMonth periodo) {
        ExpensaGeneral expensaGeneral = new ExpensaGeneral();
        expensaGeneral.setPeriodo(periodo);
        expensaGeneral.setValorTotalExpensaExtraordinaria(importeExtraordinaria);
        expensaGeneral.setValorTotalExpensaComun(importeComun);
        expensaGeneralRepository.save(expensaGeneral);
        ExpensaGeneral expensaGeneralConId = expensaGeneralRepository.findByPeriodo(periodo).get(0);
        List<Departamento> departamentos = departamentoRepository.findByBajaLogicaFalse();
        int x;

        for (x=0; x<departamentos.size(); x++){
            ExpensaDeDepartamento unaExpensa = new ExpensaDeDepartamento();
            unaExpensa.setExpensaGeneral(expensaGeneralConId);
            unaExpensa.setDepartamento(departamentos.get(x));
            unaExpensa.cargarImportesYPeriodo();
            expensaDeDepartamentoRepository.save(unaExpensa);
        }
    }
}
