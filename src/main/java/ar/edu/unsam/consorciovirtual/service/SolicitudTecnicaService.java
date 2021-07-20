package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.EstadoRepository;
import ar.edu.unsam.consorciovirtual.repository.SolicitudTecnicaRepository;
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
        return solicitudTecnicaRepository.findById(id).orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
    }

    public SolicitudTecnica modificarSolicitud(Long idLogueado, SolicitudTecnica solicitud) {
        if (usuarioService.usuarioEsAdminDeLaApp(idLogueado) || usuarioService.usuarioEsAdminDelConsorcio(idLogueado) || usuarioService.usuarioEsPropietario(idLogueado)) {
            Usuario _autor = solicitudTecnicaRepository.findById(solicitud.getId()).get().getAutor();
            solicitud.setAutor(_autor);
            SolicitudTecnica updatedRequest = asignarEstado(solicitud);
            registroModificacionService.guardarPorTipoYId(TipoRegistro.SOLICITUD_TECNICA, solicitud.getId(), usuarioService.getNombreYApellidoById(idLogueado));
            return solicitudTecnicaRepository.save(updatedRequest);
        } else {
            throw new SecurityException("No tiene permisos");
        }
    }

    public SolicitudTecnica registrarSolicitud(SolicitudTecnica solicitud) {
        SolicitudTecnica newRequest = asignarAutorYEstado(solicitud);
        return solicitudTecnicaRepository.save(newRequest);
    }
    
    private SolicitudTecnica asignarAutorYEstado(SolicitudTecnica solicitud){
        Usuario _autor = usuarioService.buscarPorId(solicitud.getAutor().getId());
        solicitud.setNombreAutor(_autor.getNombreYApellido());

        return asignarEstado(solicitud);
    }

    private SolicitudTecnica asignarEstado(SolicitudTecnica solicitud){
        Estado _estado = estadoService.buscarPorId(solicitud.getEstado().getId());
        solicitud.getEstado().setNombreEstado(_estado.getNombreEstado());
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

}
