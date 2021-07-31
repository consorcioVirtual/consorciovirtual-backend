package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.Documento;
import ar.edu.unsam.consorciovirtual.domain.Gasto;
import ar.edu.unsam.consorciovirtual.domain.Item;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.repository.DocumentoRepository;
import ar.edu.unsam.consorciovirtual.repository.GastoRepository;
import ar.edu.unsam.consorciovirtual.repository.ExpensaGeneralRepository;
import ar.edu.unsam.consorciovirtual.utils.ValidationMethods;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class GastoService {

    private final GastoRepository gastoRepository;
    private final DocumentoRepository documentoRepository;
    private final ExpensaGeneralRepository expensaRepository;
    private final RegistroModificacionService registroModificacionService;
    private final UsuarioService usuarioService;


    public void ingresarNuevoGasto(Gasto nuevoGasto) throws DataConsistencyException {
        validarGasto(nuevoGasto);
        gastoRepository.save(nuevoGasto);
    }

    public List<Gasto> buscarTodos(String palabraBuscada) {
        Double importe = busquedaToDouble(palabraBuscada);
        List<Gasto> gastos = gastoRepository.findByAnuladoFalseAndTituloContainingOrAnuladoFalseAndImporte(palabraBuscada, importe);
        gastos.forEach(this::agregarUltimaModificacion);
        return gastos;
    }

    private Double busquedaToDouble(String palabraBuscada) {
        try {
            return Double.valueOf(palabraBuscada);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public void registrarTodos(List<Gasto> gastos) {
        gastoRepository.saveAll(gastos);
    }

    public Gasto buscarPorId(Long id) {
        return gastoRepository.findById(id).orElseThrow(() -> new RuntimeException("Gasto no encontrado"));
    }

    @Transactional
    public Gasto modificar(Long idLogueado, Gasto gastoActualizado) throws DataConsistencyException {
        Gasto gastoViejo = gastoRepository.findById(gastoActualizado.getId()).orElseThrow(() -> new RuntimeException("No se encontró el gasto que desea modificar"));
        if(noExisteExpensaEnElPeriodo(gastoActualizado.getPeriodo()) && !gastoViejo.getAnulado()){
            validarGasto(gastoActualizado);
            gastoViejo.getComprobante().setEnlaceDeDescarga(gastoActualizado.getUrl());
            gastoViejo.getComprobante().setTitulo(gastoActualizado.getTitulo());
            registroModificacionService.guardarPorTipoYId(TipoRegistro.DOCUMENTO, gastoViejo.getComprobante().getId(), usuarioService.getNombreYApellidoById(idLogueado));
            documentoRepository.save(gastoViejo.getComprobante());
            Documento comprobante = documentoRepository.findById(gastoViejo.getComprobante().getId()).orElseThrow(() -> new RuntimeException("No se encontró el documento relacionado al gasto que desea modificar"));
            gastoActualizado.setComprobante(comprobante);
            registroModificacionService.guardarPorTipoYId(TipoRegistro.GASTO, gastoActualizado.getId(), usuarioService.getNombreYApellidoById(idLogueado));
            return gastoRepository.save(gastoActualizado);
        } else throw new IllegalArgumentException("No puede modificar un gasto que ya fue agregado a una expensa o se encuentra anulado");
    }

    @Transactional
    public void bajaLogica(Long id) {
        Gasto gasto = gastoRepository.findById(id).get();
        if (noExisteExpensaEnElPeriodo(gasto.getPeriodo())) {
            gasto.setAnulado(true);
            registroModificacionService.eliminarTodosPorTipoYId(TipoRegistro.GASTO, id);
            gastoRepository.save(gasto);
            Documento comprobante = gasto.getComprobante();
            comprobante.setTitulo(comprobante.getTitulo() + " (gasto anulado)");
            comprobante.setDescripcion(comprobante.getDescripcion() + " (El gasto relacionado a este documento fue anulado).");
            documentoRepository.save(comprobante);
        } else throw new IllegalArgumentException("No puede anular un gasto que ya fue agregado a una expensa");
    }

    private Boolean noExisteExpensaEnElPeriodo(YearMonth periodo) {
        return expensaRepository.findByPeriodoAndAnuladaFalse(periodo).isEmpty();
    }

    public List<Gasto> buscarPorPeriodo(YearMonth periodo) {
        return gastoRepository.findGastosByPeriodo(periodo);
    }

    private void agregarUltimaModificacion(@NotNull Gasto gasto) {
        String fechaUltimaModificacion = registroModificacionService.getUltimaModificacion(TipoRegistro.GASTO, gasto.getId());
        gasto.setUltimaModificacion(fechaUltimaModificacion);
    }

    public void eliminiarGasto(String urlGasto) {
        Gasto gasto = gastoRepository.findGastoByUrl(urlGasto);
        gastoRepository.delete(gasto);
    }

    private void validarGasto(Gasto gasto) throws DataConsistencyException {
        if (
            ValidationMethods.stringNullOVacio(gasto.getTitulo()) ||
            ValidationMethods.datoNull(gasto.getRubro()) ||
            ValidationMethods.stringNullOVacio(gasto.getTipo()) ||
            ValidationMethods.datoNull(gasto.getPeriodo()) ||
            ValidationMethods.stringNullOVacio(gasto.getUrl())
        ) throw new DataConsistencyException("Ha ocurrido un error con los datos ingresados. Verificalos e intentá de nuevo.");

        if(gasto.tieneItems()) validarItems(gasto.getItems());
        if (!this.importeDeGastoValido(gasto)) throw new IllegalArgumentException("Error en el importe del gasto. El mismo debe ser positivo e igual a la suma de sus items (si tuviese).");
    }

    private Boolean importeDeGastoValido(Gasto nuevoGasto) {
        return (!nuevoGasto.tieneItems() || (nuevoGasto.tieneItems() && nuevoGasto.importeCoincideConSumaDeIntems()))
                && nuevoGasto.getImporte() != 0;
    }

    private void validarItems(List<Item> items) throws DataConsistencyException {
        for (Item item : items) {
            if(
                ValidationMethods.doubleNullOMenorIgualACero(item.getImporteUnitario()) ||
                ValidationMethods.doubleNullOMenorIgualACero(item.getCantidad()) ||
                ValidationMethods.stringNullOVacio(item.getDescripcion())
            ) throw new DataConsistencyException("Ha ocurrido un error con los items ingresados. Verificalos e intentá de nuevo.");
        }
    }


}
