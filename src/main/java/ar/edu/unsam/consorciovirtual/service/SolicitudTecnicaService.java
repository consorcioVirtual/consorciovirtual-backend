package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import ar.edu.unsam.consorciovirtual.repository.EstadoRepository;
import ar.edu.unsam.consorciovirtual.repository.SolicitudTecnicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SolicitudTecnicaService {

    private final SolicitudTecnicaRepository solicitudTecnicaRepository;
//    private final EstadoRepository estadoRepository; //Para filtrar por estado

    public List<SolicitudTecnica> buscarTodos() {
        return solicitudTecnicaRepository.findAll();
    }

    public List<SolicitudTecnica> registrarTodos(List <SolicitudTecnica> listaSolicitudes) {
        return solicitudTecnicaRepository.saveAll(listaSolicitudes);
    }

}
