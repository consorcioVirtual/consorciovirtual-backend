package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domain.ExpensaGeneral;
import ar.edu.unsam.consorciovirtual.repository.ExpensaDeDepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ExpensaDeDepartamentoService {

    private final ExpensaDeDepartamentoRepository expensaDeDepartamentoRepository;

    private List<ExpensaDeDepartamentoDTOParaListado> mapearADTOParaListado(List<ExpensaDeDepartamento> expensas){
        return expensas.stream().map(exp -> ExpensaDeDepartamentoDTOParaListado.fromExpensaDeDepartamento(exp)).collect(Collectors.toList());
    }

    public List<ExpensaDeDepartamentoDTOParaListado> buscarTodos(String palabraBuscada) {
        List<ExpensaDeDepartamento> lista = expensaDeDepartamentoRepository.findByUnidadContainingAndAnuladaFalseOrEstadoContainingAndAnuladaFalse(palabraBuscada, palabraBuscada);
        return mapearADTOParaListado(lista);
    }

    public List<ExpensaDeDepartamentoDTOParaListado> buscarTodosSinAnuladas() {
        List<ExpensaDeDepartamento> lista = expensaDeDepartamentoRepository.findByAnuladaFalse();
        return mapearADTOParaListado(lista);
    }

    public ExpensaDeDepartamento buscarPorId(Long id){
        return expensaDeDepartamentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Expensa no Encontrada"));
    }

    public List<ExpensaDeDepartamento> buscarPorPeriodo(YearMonth periodo){
        return expensaDeDepartamentoRepository.findByPeriodoAndAnuladaFalse(periodo);
    }

}
