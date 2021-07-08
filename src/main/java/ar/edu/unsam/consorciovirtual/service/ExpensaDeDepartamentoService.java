package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domain.ExpensaGeneral;
import ar.edu.unsam.consorciovirtual.domain.Reclamo;
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
    private final UsuarioService usuarioService;
    private final DepartamentoService departamentoService;

    private List<ExpensaDeDepartamentoDTOParaListado> mapearADTOParaListado(List<ExpensaDeDepartamento> expensas){
        return expensas.stream().map(exp -> ExpensaDeDepartamentoDTOParaListado.fromExpensaDeDepartamento(exp)).collect(Collectors.toList());
    }

    public List<ExpensaDeDepartamentoDTOParaListado> buscarTodos(Long idLogueado, String palabraBuscada) {
        List<ExpensaDeDepartamento> expensas;
        List<Long> idDeptos;

        if (usuarioService.usuarioEsAdminDelConsorcio(idLogueado) || usuarioService.usuarioEsAdminDeLaApp(idLogueado)) {
            expensas = expensaDeDepartamentoRepository.findByUnidadContainingAndAnuladaFalseOrEstadoContainingAndAnuladaFalse(palabraBuscada, palabraBuscada);
            return mapearADTOParaListado(expensas);
        } else if (usuarioService.usuarioEsPropietario(idLogueado)) {
            idDeptos = departamentoService.buscarIdPorPropietario(idLogueado);
            expensas = expensaDeDepartamentoRepository.buscarPorIdDeptosYFiltro(idDeptos, palabraBuscada, palabraBuscada);
            return mapearADTOParaListado(expensas);
        } else {
            idDeptos = departamentoService.buscarIdPorInquilino(idLogueado);
            expensas = expensaDeDepartamentoRepository.buscarPorIdDeptosYFiltro(idDeptos, palabraBuscada, palabraBuscada);
            return mapearADTOParaListado(expensas);
        }
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
