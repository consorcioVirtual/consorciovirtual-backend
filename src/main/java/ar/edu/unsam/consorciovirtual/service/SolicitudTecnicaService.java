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

    public SolicitudTecnica modificarSolicitud(SolicitudTecnica solicitudActualizada) {
        SolicitudTecnica solicitudAnterior = solicitudTecnicaRepository.findById(solicitudActualizada.getId()).get();

        solicitudActualizada.setAutor(solicitudAnterior.getAutor());
        //TODO: Como encaramos el tema del estado?
//        solicitudActualizada.setEstado();

        return solicitudTecnicaRepository.save(solicitudActualizada);
    }

    public SolicitudTecnica registrarSolicitud(SolicitudTecnica solicitud) {
//      TODO: VER SI EL "getAutor" NO TRAE EL ID.
//        EN ESE CASO, LLAMAR AL UsuarioService PARA TRAER EL USUARIO CORRESPONDIENTE
        Usuario _autor = solicitud.getAutor();

        solicitud.setNombreAutor(_autor.getNombreYApellido());
        return solicitudTecnicaRepository.save(solicitud);
    }

    public void bajaLogicaSolicitud(Long id){
        SolicitudTecnica solicitud = solicitudTecnicaRepository.findById(id).get();
        solicitud.setBajaLogica(true);

        solicitudTecnicaRepository.save(solicitud);
    }

}
