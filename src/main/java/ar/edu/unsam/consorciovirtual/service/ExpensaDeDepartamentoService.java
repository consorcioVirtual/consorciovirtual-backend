package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.DocumentoRepository;
import ar.edu.unsam.consorciovirtual.repository.ExpensaDeDepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.unsam.consorciovirtual.domain.Constants.CARPETA_DE_RECIBOS;

@RequiredArgsConstructor
@Service
@Transactional
public class ExpensaDeDepartamentoService {

    private final ExpensaDeDepartamentoRepository expensaDeDepartamentoRepository;
    private final UsuarioService usuarioService;
    private final DepartamentoService departamentoService;
    private final DocumentoService documentoService;
    private final DocumentoRepository documentoRepository;
    private final GestorDeCorreo gestorDeCorreo;

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

    public void pagarExpensa(Long idExpensa, Long idUsuario) {
        ExpensaDeDepartamento expensa = buscarPorId(idExpensa);
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        if(!expensa.estaPaga()){
            expensa.pagarExpensa(usuario);
            //Crea el recibo de pago
            String nombreSimple = "ReciboDeExpensa"+expensa.getPeriodo().toString()+"-"+expensa.getUnidad();
            String nombreArchivo = CARPETA_DE_RECIBOS+nombreSimple+".pdf";
            CreadorDePDF.createReciboDeExpensa(expensa, nombreArchivo);
            //Guarda el recibo de pago
            Documento nuevoDocumento = documentoService.crearDocumentoEnBaseAPDFDelSistema(nombreSimple, nombreArchivo);
            documentoRepository.save(nuevoDocumento);
            //Se envía el correo con el recibo
            gestorDeCorreo.enviarReciboDeExpensas(expensa, nombreArchivo);
        }else throw new IllegalArgumentException("La expensa que desea pagar, ya se encuentra paga");
    }
}
