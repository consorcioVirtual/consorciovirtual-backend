package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Mensaje;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.MensajeRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MensajeService {
    @Autowired
    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Mensaje> getMensajes() {
        return mensajeRepository.findAll();
    }

    public void createMensaje(Long idAutor, Mensaje mensajeNuevo, Long idMensajeCitado) {
        Usuario autor = usuarioRepository.findById(idAutor).orElseThrow(() -> new IllegalArgumentException ("Error con el usuario que quiere crear el mensaje"));
        if (idMensajeCitado != null){
            Mensaje mensajeCitado = mensajeRepository.findById(idMensajeCitado).orElseThrow(() -> new IllegalArgumentException ("Error al buscar el mensaje citado"));
            mensajeNuevo.setMensajeCitado(mensajeCitado);
        }
        mensajeNuevo.setAutor(autor);
        mensajeRepository.save(mensajeNuevo);
    }

    public void registrarTodos(List<Mensaje> mensajes) {
        mensajeRepository.saveAll(mensajes);
    }
}