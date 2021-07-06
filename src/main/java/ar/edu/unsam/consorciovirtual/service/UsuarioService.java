package ar.edu.unsam.consorciovirtual.service;

import javax.transaction.Transactional;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.domain.TipoUsuario;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.DepartamentoRepository;
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

    private final DepartamentoRepository departamentoRepository;

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

    public void modificar(Usuario usuarioActualizado) {
        Usuario usuarioAnterior = usuarioRepository.findById(usuarioActualizado.getId()).get();
        //usuarioActualizado.setCorreo(usuarioAnterior.getCorreo());
        usuarioActualizado.setPassword(usuarioAnterior.getPassword());
        registroModificacionService.guardarPorTipoYId(TipoRegistro.USUARIO, usuarioActualizado.getId());
        usuarioRepository.save(usuarioActualizado);
    }

    public Usuario loguearUsuario(Usuario usuario){
        Usuario user = usuarioRepository.findByCorreoAndPasswordAndBajaLogicaFalse(usuario.getCorreo(), usuario.getPassword());
        usuarioLogueado = user;

        if(user != null) {
            return user;
        } else throw new SecurityException("Usuario o contraseña incorrectos");
    }

    public void bajaLogica(Long idLogueado, Long idABorrar) throws DataConsistencyException {
        validarBaja(idLogueado, idABorrar);
        Usuario usuario = usuarioRepository.findById(idABorrar).get();
        usuario.setBajaLogica(true);

        usuarioRepository.save(usuario);
    }

    private void validarBaja(Long idLogueado, Long idABorrar) throws DataConsistencyException {
        if(idLogueado.equals(idABorrar)){
            throw new DataConsistencyException("No es posible eliminarte a ti mismo.");
        }
        if(!usuarioEsAdminDeLaApp(idLogueado)){
            throw new SecurityException("No tiene permisos para eliminar un usuario.");
        }
        if(usuarioSeRelacionaConDeptos(idABorrar)){
            throw new DataConsistencyException("El usuario a eliminar está asociado a un departamento (es inquilino o propietario).");
        }
    }

    public Boolean usuarioEsAdminDeLaApp(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        return usuario.getTipo() == TipoUsuario.Administrador;
    }

    public Boolean usuarioEsAdminDelConsorcio(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        return usuario.getTipo() == TipoUsuario.Administrador_consorcio;
    }

    //Chequea si hay deptos que tengan al usuario como propietario/inquilino
    private Boolean usuarioSeRelacionaConDeptos(Long idUsuario){
        return !departamentoRepository.buscarPorUsuario(idUsuario).isEmpty();
    }
}
