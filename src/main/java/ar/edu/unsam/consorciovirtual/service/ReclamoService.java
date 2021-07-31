package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.ReclamoRepository;
import ar.edu.unsam.consorciovirtual.utils.ValidationMethods;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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

    public List<Reclamo> buscarTodos(Long idLogueado, String palabraBuscada) {
        Long idReclamo = busquedaToLong(palabraBuscada);
        List<Reclamo> reclamos;

        if (usuarioService.usuarioEsAdminDelConsorcio(idLogueado) || usuarioService.usuarioEsAdminDeLaApp(idLogueado)) {
            reclamos = reclamoRepository.findByIdAndBajaLogicaFalseOrAutorNombreContainingAndBajaLogicaFalseOrAutorApellidoContainingAndBajaLogicaFalseAndBajaLogicaFalseOrAsuntoContainingAndBajaLogicaFalseOrEstadoNombreEstadoContainingAndBajaLogicaFalse(idReclamo, palabraBuscada, palabraBuscada, palabraBuscada, palabraBuscada);
        } else if (usuarioService.usuarioEsPropietario(idLogueado)) {
            reclamos = reclamoRepository.buscarPropiosODeMisInquilinos(idLogueado, idReclamo, palabraBuscada, palabraBuscada, palabraBuscada);
        } else {
            reclamos = reclamoRepository.buscarPropios(idLogueado, idReclamo, palabraBuscada, palabraBuscada, palabraBuscada);
        }

        reclamos.forEach(this::agregarUltimaModificacion);
        return reclamos;
    }

    public Reclamo buscarPorId(Long id) {
        return reclamoRepository.findById(id).orElseThrow(() -> new RuntimeException("Reclamo no encontrado"));
    }

    public Reclamo modificarReclamo(Long idLogueado, Reclamo reclamo) throws DataConsistencyException {
        if (!puedeModificarReclamo(idLogueado, reclamo)) throw new SecurityException("No tiene permisos para modificar un reclamo.");
        if(reclamoResuelto(reclamo.getId())) throw new DataConsistencyException("No puede modificar un reclamo finalizado o rechazado.");

        Usuario _autor = reclamoRepository.findById(reclamo.getId()).get().getAutor();
        reclamo.setAutor(_autor);
        Reclamo updatedRequest = asignarEstado(reclamo);

        validarReclamo(updatedRequest);
        registroModificacionService.guardarPorTipoYId(TipoRegistro.RECLAMO, reclamo.getId(), usuarioService.getNombreYApellidoById(idLogueado));

        return reclamoRepository.save(updatedRequest);
    }

    private boolean puedeModificarReclamo(Long idLogueado, Reclamo reclamo) {
         return usuarioService.usuarioEsAdminDeLaApp(idLogueado) ||
                usuarioService.usuarioEsAdminDelConsorcio(idLogueado) ||
                idLogueado == reclamo.getAutor().getId();
    }

    private boolean reclamoResuelto(Long idReclamo){
        Estado estado = buscarPorId(idReclamo).getEstado();
        return estado.getNombreEstado().equals("Resuelto") || estado.getNombreEstado().equals("Rechazado");
    }


    public Reclamo registrarReclamo(Reclamo reclamo) throws DataConsistencyException {
        Reclamo newRequest = asignarAutorYEstado(reclamo);
        validarReclamo(newRequest);
        return reclamoRepository.save(newRequest);
    }

    public List<Reclamo> registrarTodos(List<Reclamo> listaReclamos) {
        return reclamoRepository.saveAll(listaReclamos);
    }

    public void bajaLogicaReclamo(Long id, Long idLogueado){
        Reclamo reclamo = reclamoRepository.findById(id).get();

        if (usuarioService.usuarioEsAdminDeLaApp(idLogueado) || usuarioService.usuarioEsAdminDelConsorcio(idLogueado) || idLogueado == reclamo.getAutor().getId()) {
            reclamo.setBajaLogica(true);
            registroModificacionService.eliminarTodosPorTipoYId(TipoRegistro.RECLAMO, id);

            reclamoRepository.save(reclamo);
        } else {
            throw new SecurityException("No tiene permisos para eliminar este reclamo.");
        }

    }

    private Reclamo asignarAutorYEstado(Reclamo reclamo){
        Usuario _autor = usuarioService.buscarPorId(reclamo.getAutor().getId());
        reclamo.setAutor(_autor);

        return asignarEstado(reclamo);
    }

    private Reclamo asignarEstado(Reclamo reclamo){
        Estado _estado = estadoService.buscarPorNombre(reclamo.getEstado().getNombreEstado());
        reclamo.setEstado(_estado);
        return reclamo;
    }

    private Long busquedaToLong(String palabraBuscada) {
        try {
            return Long.valueOf(palabraBuscada);
        } catch (NumberFormatException ex){
            return null;
        }
    }

    private void agregarUltimaModificacion(@NotNull Reclamo dto){
        String fechaUltimaModificacion = registroModificacionService.getUltimaModificacion(TipoRegistro.RECLAMO, dto.getId());
        dto.setUltimaModificacion(fechaUltimaModificacion);
    }

    private void validarReclamo(Reclamo reclamo) throws DataConsistencyException {
        if (
            ValidationMethods.stringNullOVacio(reclamo.getAsunto()) ||
            ValidationMethods.datoNull(reclamo.getMensaje()) ||
            ValidationMethods.datoNull(reclamo.getAutor()) ||
            ValidationMethods.datoNull(reclamo.getEstado())
        ) throw new DataConsistencyException("Ha ocurrido un error con los datos ingresados. Verificalos e intentá de nuevo.");

        if(reclamo.tieneNotas()) validarNotas(reclamo.getNotas());
    }

    private void validarNotas(List<Nota> notas) throws DataConsistencyException {
        for (Nota nota : notas) {
            if(
               ValidationMethods.datoNull(nota.getAutor()) ||
               ValidationMethods.stringNullOVacio(nota.getTexto())
            ) throw new DataConsistencyException("Ha ocurrido un error con las notas asociadas. Verificalas e intentá de nuevo.");
        }
    }

}
