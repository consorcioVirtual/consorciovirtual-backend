package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
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

    private Double calcularValorDeGastos(YearMonth periodo, String tipo){
        List<Double> gastos = gastoRepository.findImporteByPeriodoAndByTipo(periodo, tipo);
        return  gastos.stream()
                    .mapToDouble(a -> a)
                    .sum();
    }

    public void generarExpensasPorImporteDeGastos(YearMonth periodo){
        Double importeGastosComunes= (double)Math.round(calcularValorDeGastos(periodo, "Común")* 100d) / 100d;
        Double importeGastosExtraordinarias=(double)Math.round(calcularValorDeGastos(periodo, "Extraordinaria")* 100d) / 100d;

        generarExpensas(importeGastosComunes, importeGastosExtraordinarias, periodo);
    }

    @Transactional
    private void generarExpensas(Double importeComun, Double importeExtraordinaria, YearMonth periodo) {
        if(expensaGeneralRepository.findOneByPeriodoAndAnuladaFalse(periodo) != null){
            throw new IllegalArgumentException("Ya existe una expensa activa de ese periodo");
        }
        ExpensaGeneral expensaGeneral = new ExpensaGeneral();
        expensaGeneral.setPeriodo(periodo);
        expensaGeneral.setValorTotalExpensaExtraordinaria(importeExtraordinaria);
        expensaGeneral.setValorTotalExpensaComun(importeComun);
        expensaGeneralRepository.save(expensaGeneral);
        ExpensaGeneral expensaGeneralConId = expensaGeneralRepository.findOneByPeriodoAndAnuladaFalse(periodo);
        List<Departamento> departamentos = departamentoRepository.findByBajaLogicaFalse();

        List <Gasto> gastosDelPeriodo = gastoRepository.findGastosByPeriodo(periodo);
        int x;
        for (x=0; x<departamentos.size(); x++){
            ExpensaDeDepartamento unaExpensa = new ExpensaDeDepartamento();
            unaExpensa.setExpensaGeneral(expensaGeneralConId);
            unaExpensa.setDepartamento(departamentos.get(x));
            unaExpensa.cargarImportesYPeriodo();
            unaExpensa.cargarUnidadDepto();
            expensaDeDepartamentoRepository.save(unaExpensa);
            CreadorDePDF.createResumenDeExpensa(unaExpensa, gastosDelPeriodo);
        }
    }
}
