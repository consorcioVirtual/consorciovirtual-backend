package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Mensaje;
import ar.edu.unsam.consorciovirtual.domain.MensajeRequest;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.MensajeRepository;
import ar.edu.unsam.consorciovirtual.repository.RegistroMensajeRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MensajeService {
    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroMensajeRepository registroMensajeRepository;

    public List<Mensaje> getMensajes(String palabraBuscada) {
//        return mensajeRepository.findAll(Sort.by( (Sort.Direction.DESC),"id") );
        return mensajeRepository.findMensajesFiltrados(palabraBuscada);
    }

    public void createMensaje(MensajeRequest mensaje) {
        Usuario usuarioEmisor = usuarioRepository.findById(mensaje.getIdEmisor())
                .orElseThrow(() -> new IllegalArgumentException ("Error con el usuario que quiere crear el mensaje"));
        Mensaje mensajeNuevo = new Mensaje();
        mensajeNuevo.setMensaje(mensaje.getMensaje());
        mensajeNuevo.setUsuarioEmisor(usuarioEmisor);
        mensajeRepository.save(mensajeNuevo);
    }

//    public Integer getCantidadMensajes(Long usuarioId) {
//
//    }

//    public void createMensaje(Long idAutor, Mensaje mensajeNuevo, Long idMensajeCitado) {
//        Usuario autor = usuarioRepository.findById(idAutor).orElseThrow(() -> new IllegalArgumentException ("Error con el usuario que quiere crear el mensaje"));
//        if (idMensajeCitado != null){
//            Mensaje mensajeCitado = mensajeRepository.findById(idMensajeCitado).orElseThrow(() -> new IllegalArgumentException ("Error al buscar el mensaje citado"));
//            mensajeNuevo.setMensajeCitado(mensajeCitado);
//        }
//        mensajeNuevo.setAutor(autor);
//        mensajeRepository.save(mensajeNuevo);
//    }

    public void registrarTodos(List<Mensaje> mensajes) {
        mensajeRepository.saveAll(mensajes);
    }
}
