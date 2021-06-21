package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.DepartamentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnicaDTOParaListado;
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
//    private final EstadoRepository estadoRepository; //Para filtrar por estado

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

}
