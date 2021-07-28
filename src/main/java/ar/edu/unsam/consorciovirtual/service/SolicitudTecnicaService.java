package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.domainDTO.SolicitudTecnicaDTOParaListado;
import ar.edu.unsam.consorciovirtual.repository.SolicitudTecnicaRepository;
import ar.edu.unsam.consorciovirtual.utils.ValidationMethods;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class SolicitudTecnicaService {

    private final SolicitudTecnicaRepository solicitudTecnicaRepository;
    private final UsuarioService usuarioService;
    private final EstadoService estadoService;
    private final RegistroModificacionService registroModificacionService;

    public List<SolicitudTecnicaDTOParaListado> buscarTodos(Long idLogueado, String palabraBuscada) {
        Long idSolicitud = busquedaToLong(palabraBuscada);
        List<SolicitudTecnica> solicitudes;
        if (usuarioService.usuarioEsAdminDelConsorcio(idLogueado) || usuarioService.usuarioEsAdminDeLaApp(idLogueado)) {
            solicitudes = solicitudTecnicaRepository.buscarCreadasPorUsuariosNoInquilinosOAprobadasPorPropietarios(idSolicitud, palabraBuscada, palabraBuscada, palabraBuscada);
        } else if (usuarioService.usuarioEsPropietario(idLogueado)) {
            solicitudes = solicitudTecnicaRepository.buscarPropiasODeMisInquilinos(idLogueado, idSolicitud, palabraBuscada, palabraBuscada, palabraBuscada);
        } else {
            solicitudes = solicitudTecnicaRepository.buscarPropias(idLogueado, idSolicitud, palabraBuscada, palabraBuscada, palabraBuscada);
        }

        List<SolicitudTecnicaDTOParaListado> solicitudDtos =  solicitudes.stream().map(SolicitudTecnicaDTOParaListado::fromSolicitudTecnica).collect(Collectors.toList());
        solicitudDtos.forEach(this::agregarUltimaModificacion);
        return solicitudDtos;
    }

    public List<SolicitudTecnica> buscarPorTipo(String tipo) {
        return solicitudTecnicaRepository.findByTipo(tipo);
    }

    private Long busquedaToLong(String palabraBuscada) {
        try {
            return Long.valueOf(palabraBuscada);
        } catch (NumberFormatException ex){
            return null;
        }
    }

    public List<SolicitudTecnica> registrarTodos(List <SolicitudTecnica> listaSolicitudes) {
        return solicitudTecnicaRepository.saveAll(listaSolicitudes);
    }

    public SolicitudTecnica buscarPorId(Long id) {
        return solicitudTecnicaRepository.findById(id).orElseThrow(() -> new RuntimeException("Departamento no encontrado."));
    }

    public SolicitudTecnica modificarSolicitud(Long idLogueado, SolicitudTecnica solicitud) throws DataConsistencyException {
        if (!puedeModificar(idLogueado, solicitud)) throw new SecurityException("No tiene permisos para modificar esta solicitud técnica.");
        if(solicitudResuelta(solicitud.getId())) throw new DataConsistencyException("No puede modificar una solicitud técnica finalizada o rechazada.");

        Usuario _autor = solicitudTecnicaRepository.findById(solicitud.getId()).get().getAutor();
        solicitud.setAutor(_autor);
        SolicitudTecnica updatedRequest = asignarEstado(solicitud);

        validarSolicitud(updatedRequest);
        registroModificacionService.guardarPorTipoYId(TipoRegistro.SOLICITUD_TECNICA, solicitud.getId(), usuarioService.getNombreYApellidoById(idLogueado));

        return solicitudTecnicaRepository.save(updatedRequest);
    }

    private boolean puedeModificar(Long idLogueado, SolicitudTecnica solicitud) {
        return  usuarioService.usuarioEsAdminDeLaApp(idLogueado) ||
                usuarioService.usuarioEsAdminDelConsorcio(idLogueado) ||
                usuarioService.usuarioEsPropietario(idLogueado) ||
                idLogueado == solicitud.getAutor().getId();
    }

    public SolicitudTecnica registrarSolicitud(SolicitudTecnica solicitud) throws DataConsistencyException {
        SolicitudTecnica newRequest = asignarAutorYEstado(solicitud);
        validarSolicitud(newRequest);
        return solicitudTecnicaRepository.save(newRequest);
    }

    private boolean solicitudResuelta(Long idSolicitud){
        Estado estado = buscarPorId(idSolicitud).getEstado();
        return estado.getNombreEstado().equals("Resuelto") || estado.getNombreEstado().equals("Rechazado");
    }

    private SolicitudTecnica asignarAutorYEstado(SolicitudTecnica solicitud){
        Usuario _autor = usuarioService.buscarPorId(solicitud.getAutor().getId());
        solicitud.setNombreAutor(_autor.getNombreYApellido());

        return asignarEstado(solicitud);
    }

    private SolicitudTecnica asignarEstado(SolicitudTecnica solicitud){
        Estado _estado = estadoService.buscarPorNombre(solicitud.getEstado().getNombreEstado());
        solicitud.setEstado(_estado);
        return solicitud;
    }

    public void bajaLogicaSolicitud(Long id){
        SolicitudTecnica solicitud = solicitudTecnicaRepository.findById(id).get();
        solicitud.setBajaLogica(true);
        registroModificacionService.eliminarTodosPorTipoYId(TipoRegistro.SOLICITUD_TECNICA, id);

        solicitudTecnicaRepository.save(solicitud);
    }

    private void agregarUltimaModificacion(@NotNull SolicitudTecnicaDTOParaListado dto){
        String fechaUltimaModificacion = registroModificacionService.getUltimaModificacion(TipoRegistro.SOLICITUD_TECNICA, dto.getId());
        dto.setUltimaModificacion(fechaUltimaModificacion);
    }

    private void validarSolicitud(SolicitudTecnica solicitud) throws DataConsistencyException {
        if (
            ValidationMethods.stringNullOVacio(solicitud.getTipo()) ||
            ValidationMethods.stringNullOVacio(solicitud.getTitulo()) ||
            ValidationMethods.stringNullOVacio(solicitud.getDetalle()) ||
            ValidationMethods.datoNull(solicitud.getAutor()) ||
            ValidationMethods.datoNull(solicitud.getEstado())
        ) throw new DataConsistencyException("Ha ocurrido un error con los datos ingresados. Verificalos e intentá de nuevo.");

        if(solicitud.tieneNotas()) validarNotas(solicitud.getNotas());
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
