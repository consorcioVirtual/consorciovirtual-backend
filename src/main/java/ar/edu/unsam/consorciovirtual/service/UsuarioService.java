package ar.edu.unsam.consorciovirtual.service;

import javax.transaction.Transactional;

import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RegistroModificacionService registroModificacionService;

    public static Usuario usuarioLogueado;

    public List<Usuario> buscarTodos(String palabraBuscada) {
        return usuarioRepository.findByNombreContainingAndBajaLogicaFalseOrApellidoContainingAndBajaLogicaFalseOrDniContainingAndBajaLogicaFalseOrCorreoContainingAndBajaLogicaFalse(palabraBuscada, palabraBuscada, palabraBuscada, palabraBuscada);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

//    public Usuario buscarPorNombre(String nombre){
//        return usuarioRepository.findByNombre(nombre).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//    }

    public Usuario registrarUsuario(Usuario usuario) { return usuarioRepository.save(usuario); }

    public List<Usuario> registrarTodos(List <Usuario> listaUsuarios) { return usuarioRepository.saveAll(listaUsuarios); }

    public Usuario modificar(Usuario usuarioActualizado) {
        Usuario usuarioAnterior = usuarioRepository.findById(usuarioActualizado.getId()).get();

//        usuarioActualizado.setCorreo(usuarioAnterior.getCorreo());
        usuarioActualizado.setPassword(usuarioAnterior.getPassword());
       // registroModificacionService.guardarPorTipoYId(TipoRegistro.USUARIO, usuarioActualizado.getId());

        return usuarioRepository.save(usuarioActualizado);
    }

    public Usuario loguearUsuario(Usuario usuario){
        Usuario user = usuarioRepository.findByCorreoAndPassword(usuario.getCorreo(), usuario.getPassword());
        usuarioLogueado = user;

        if(user != null) {
            return user;
        } else throw new SecurityException("Usuario o contraseña incorrectos");
    }

    public void bajaLogica(Long id){
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setBajaLogica(true);

        usuarioRepository.save(usuario);
    }

}
