package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Expensa;
import ar.edu.unsam.consorciovirtual.repository.ExpensaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ExpensaService {

    private final ExpensaRepository expensaRepository;

    public List<Expensa> buscarTodos() {
        return expensaRepository.findAll();
    }

    public List<Expensa> buscarTodosSinAnuladas() {
        return expensaRepository.findByAnuladaFalse();
    }

    public void registrarTodos(List<Expensa> expensas) {
        expensaRepository.saveAll(expensas);
    }

}
