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

    public List<Usuario> buscarTodos() { return usuarioRepository.findByBajaLogicaFalse(); }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario registrar(Usuario usuario) { return usuarioRepository.save(usuario); }

    public List<Usuario> registrarTodos(List <Usuario> listaUsuarios) { return usuarioRepository.saveAll(listaUsuarios); }

    public Usuario modificar(Usuario usuarioActualizado) {
        Usuario usuarioAnterior = usuarioRepository.findById(usuarioActualizado.getId()).get();

        usuarioActualizado.setUsername(usuarioAnterior.getUsername());
        usuarioActualizado.setPassword(usuarioAnterior.getPassword());
        registroModificacionService.guardarPorTipoYId(TipoRegistro.USUARIO, usuarioActualizado.getId());

        return usuarioRepository.save(usuarioActualizado);
    }

    public Usuario loguearUsuario(Usuario usuario){
        Usuario user = usuarioRepository.findByUsernameAndPassword(usuario.getUsername(), usuario.getPassword());
        usuarioLogueado = user;

        if(user != null) {
            return user;
        } else throw new SecurityException("Usuario o contraseña incorrectos");
    }
}
