package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.EstadoRepository;
import ar.edu.unsam.consorciovirtual.repository.SolicitudTecnicaRepository;
import lombok.RequiredArgsConstructor;
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

    public List<SolicitudTecnicaDTOParaListado> buscarTodos(String palabraBuscada) {
        Long idSolicitud = busquedaToLong(palabraBuscada);
        List<SolicitudTecnica> solicitudes = solicitudTecnicaRepository.findByIdAndBajaLogicaFalseOrNombreAutorContainingAndBajaLogicaFalseOrTituloContainingAndBajaLogicaFalseOrNombreEstadoContainingAndBajaLogicaFalse(idSolicitud, palabraBuscada, palabraBuscada, palabraBuscada);
        return solicitudes.stream().map(x -> SolicitudTecnicaDTOParaListado.fromSolicitudTecnica(x)).collect(Collectors.toList());
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

    public SolicitudTecnica modificarSolicitud(SolicitudTecnica solicitud) {
        Usuario _autor = solicitudTecnicaRepository.findById(solicitud.getId()).get().getAutor();
        solicitud.setAutor(_autor);
        SolicitudTecnica updatedRequest = asignarEstado(solicitud);
        return solicitudTecnicaRepository.save(updatedRequest);
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
        solicitud.setNombreEstado(_estado.getNombreEstado());
        return solicitud;
    }

    public void bajaLogicaSolicitud(Long id){
        SolicitudTecnica solicitud = solicitudTecnicaRepository.findById(id).get();
        solicitud.setBajaLogica(true);

        solicitudTecnicaRepository.save(solicitud);
    }

}
