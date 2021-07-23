package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.Estado;
import ar.edu.unsam.consorciovirtual.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class EstadoService {
    private final EstadoRepository estadoRepository;

    public Estado buscarPorId(Long id) {
        return estadoRepository.findById(id).orElseThrow(() -> new RuntimeException("Estado no encontrado"));
    }

    public Estado buscarPorNombre(String nombre) {
        return estadoRepository.findByNombreEstado(nombre).orElseThrow(() -> new RuntimeException("Estado no encontrado"));
    }
}
