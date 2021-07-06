package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.ReclamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final UsuarioService usuarioService;
    private final EstadoService estadoService;
    private final RegistroModificacionService registroModificacionService;

    public List<Reclamo> buscarTodos(String palabraBuscada) {
        Long idReclamo = busquedaToLong(palabraBuscada);
        List<Reclamo> reclamos = reclamoRepository.findByIdAndBajaLogicaFalseOrAutorNombreContainingAndBajaLogicaFalseOrAutorApellidoContainingAndBajaLogicaFalseAndBajaLogicaFalseOrAsuntoContainingAndBajaLogicaFalseOrEstadoNombreEstadoContainingAndBajaLogicaFalse(idReclamo, palabraBuscada, palabraBuscada, palabraBuscada, palabraBuscada);
        return reclamos;
    }

    public Reclamo buscarPorId(Long id) {
        return reclamoRepository.findById(id).orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));
    }

    public Reclamo modificarReclamo(Reclamo reclamo) {
        Usuario _autor = reclamoRepository.findById(reclamo.getId()).get().getAutor();
        reclamo.setAutor(_autor);
        Reclamo updatedRequest = asignarEstado(reclamo);
        registroModificacionService.guardarPorTipoYId(TipoRegistro.RECLAMO, reclamo.getId());
        return reclamoRepository.save(updatedRequest);
    }

    public Reclamo registrarReclamo(Reclamo reclamo) {
        Reclamo newRequest = asignarAutorYEstado(reclamo);
        return reclamoRepository.save(newRequest);
    }

    public List<Reclamo> registrarTodos(List<Reclamo> listaReclamos) {
        return reclamoRepository.saveAll(listaReclamos);
    }

    public void bajaLogicaReclamo(Long id){
        Reclamo reclamo = reclamoRepository.findById(id).get();
        reclamo.setBajaLogica(true);

        reclamoRepository.save(reclamo);
    }

    private Reclamo asignarAutorYEstado(Reclamo reclamo){
        Usuario _autor = usuarioService.buscarPorId(reclamo.getAutor().getId());
        reclamo.setAutor(_autor);

        return asignarEstado(reclamo);
    }

    private Reclamo asignarEstado(Reclamo reclamo){
        Estado _estado = estadoService.buscarPorId(reclamo.getEstado().getId());
        reclamo.getEstado().setNombreEstado(_estado.getNombreEstado());
        return reclamo;
    }

    private Long busquedaToLong(String palabraBuscada) {
        try {
            return Long.valueOf(palabraBuscada);
        } catch (NumberFormatException ex){
            return null;
        }
    }

}
