package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.RegistroModificacion;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.repository.RegistroModificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class RegistroModificacionService {
    private final RegistroModificacionRepository registroModificacionRepository;

    public List<RegistroModificacion> buscarTodos() {
        return registroModificacionRepository.findAll();
    }

    public List<RegistroModificacion> buscarPorTipoYId(TipoRegistro tipoRegistro, Long idModificado) {
        return registroModificacionRepository.findByTipoRegistroAndIdModificado(tipoRegistro, idModificado);
    }

    public RegistroModificacion guardarPorTipoYId(TipoRegistro tipoRegistro, Long idModificado) {
        RegistroModificacion registroModificacion = new RegistroModificacion();
        registroModificacion.setIdModificado(idModificado);
        registroModificacion.setTipoRegistro(tipoRegistro);
        registroModificacion.setUsuarioModificador(UsuarioService.usuarioLogueado.getNombre() + " " + UsuarioService.usuarioLogueado.getApellido());
        registroModificacion.setFechaModificacion(LocalDateTime.now());
        return registroModificacionRepository.save(registroModificacion);
    }
}
