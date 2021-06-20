package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.repository.ExpensaDeDepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ExpensaDeDepartamentoService {

    private final ExpensaDeDepartamentoRepository expensaDeDepartamentoRepository;

    public List<ExpensaDeDepartamento> buscarTodos() {
        return expensaDeDepartamentoRepository.findAll();
    }

    public List<ExpensaDeDepartamento> buscarTodosSinAnuladas() {
        return expensaDeDepartamentoRepository.findByAnuladaFalse();
    }

    public void registrarTodos(List<ExpensaDeDepartamento> expensas) {
        expensaDeDepartamentoRepository.saveAll(expensas);
    }

}
