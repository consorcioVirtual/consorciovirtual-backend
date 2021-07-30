package ar.edu.unsam.consorciovirtual.service;

import javax.transaction.Transactional;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.domain.TipoUsuario;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.domainDTO.UsuarioConDeptoDTO;
import ar.edu.unsam.consorciovirtual.repository.DepartamentoRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import ar.edu.unsam.consorciovirtual.utils.ValidationMethods;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RegistroModificacionService registroModificacionService;
//    private final DepartamentoService departamentoService;
    private final DepartamentoRepository departamentoRepository;
//    private final GestorDeCorreo gestorDeCorreo;

//    public static Usuario usuarioLogueado;

    public List<Usuario> buscarTodos(String palabraBuscada) {
        List<Usuario> users = usuarioRepository.findBySearch(palabraBuscada);
        users.forEach(this::agregarUltimaModificacion);
        return users;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<Usuario> buscarPorTipo(TipoUsuario tipo){
        return usuarioRepository.findByTipo(tipo);
    }


//    public Usuario buscarPorNombre(String nombre){
//        return usuarioRepository.findByNombre(nombre).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//    }
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) throws DataConsistencyException {
        validarAlta(usuario);
        usuario.setPassword(usuario.getDni());
        validarUsuario(usuario);
//        gestorDeCorreo.enviarMensajeNuevoUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> registrarTodos(List <Usuario> listaUsuarios) { return usuarioRepository.saveAll(listaUsuarios); }

    public void modificar(Long idLogueado, Usuario usuarioActualizado) throws DataConsistencyException {
        if(usuarioRepository.existsByCorreoAndBajaLogicaFalseAndIdNot(usuarioActualizado.getCorreo(), usuarioActualizado.getId())){
            throw new DataConsistencyException("El mail indicado ya está registrado en Consorcio Virtual.");
        }
        
        validarUsuario(usuarioActualizado);
        Usuario usuarioAnterior = usuarioRepository.findById(usuarioActualizado.getId()).get();
        usuarioActualizado.setPassword(usuarioAnterior.getPassword());
        registroModificacionService.guardarPorTipoYId(TipoRegistro.USUARIO, usuarioActualizado.getId(), getNombreYApellidoById(idLogueado));
        usuarioRepository.save(usuarioActualizado);
    }

    public Usuario loguearUsuario(String correo, String password){
        Usuario user = usuarioRepository.findByCorreoAndPasswordAndBajaLogicaFalse(correo, password).orElseThrow(() -> new SecurityException("Usuario o contraseña incorrectos."));
        return user;
    }

    public void modificarContrasenia(String correo, String password, String newPassword) {
        Usuario user = usuarioRepository.findByCorreoAndPasswordAndBajaLogicaFalse(correo, password).orElseThrow(() -> new SecurityException("Contraseña incorrecta."));

        user.setPassword(newPassword);
        usuarioRepository.save(user);

    }


    public Usuario bajaLogica(Long idLogueado, Long idABorrar) throws DataConsistencyException {
        validarBaja(idLogueado, idABorrar);
        Usuario usuario = usuarioRepository.findById(idABorrar).get();
        usuario.setBajaLogica(true);
        registroModificacionService.eliminarTodosPorTipoYId(TipoRegistro.USUARIO, idABorrar);

        return usuarioRepository.save(usuario);
    }

    private void validarBaja(Long idLogueado, Long idABorrar) throws DataConsistencyException {
        if(idLogueado.equals(idABorrar)){
            throw new DataConsistencyException("No es posible eliminarte a ti mismo.");
        }
        if(usuarioEsAdminDeLaApp(idLogueado)){
            throw new SecurityException("No tiene permisos para eliminar un usuario.");
        }
        if(usuarioSeRelacionaConDeptos(idABorrar)){
            throw new DataConsistencyException("No es posible eliminar un usuario que es propietario de un departamento.");
        }
    }

    private void validarAlta(Usuario newUser) throws DataConsistencyException {
        if(usuarioRepository.existsByCorreoAndDniAndBajaLogicaFalse(newUser.getCorreo(), newUser.getDni())){
            throw new DataConsistencyException("El usuario que desea crear ya está registrado en Consorcio Virtual.");
        }
        if(usuarioRepository.existsByCorreoAndBajaLogicaFalse(newUser.getCorreo())){
            throw new DataConsistencyException("El mail indicado ya está registrado en Consorcio Virtual.");
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

    public Boolean usuarioEsPropietario(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        return usuario.getTipo() == TipoUsuario.Propietario;
    }

    public Boolean usuarioEsInquilino(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        return usuario.getTipo() == TipoUsuario.Inquilino;
    }

    private void agregarUltimaModificacion(@NotNull Usuario user){
        String fechaUltimaModificacion = registroModificacionService.getUltimaModificacion(TipoRegistro.USUARIO, user.getId());
        user.setUltimaModificacion(fechaUltimaModificacion);
    }

    //Chequea si hay deptos que tengan al usuario como propietario
    private Boolean usuarioSeRelacionaConDeptos(Long idUsuario){
        return departamentoRepository.cantidadDeptosDelUsuario(idUsuario) != 0;
    }

    public String getNombreYApellidoById(Long idUsuario){
        return usuarioRepository.findById(idUsuario).get().getNombreYApellido();
    }

    //INQUILINOS
    public List<Usuario> buscarInquilinos(String palabraBuscada) {
        List<Usuario> users = usuarioRepository.findBySearchInquilino(palabraBuscada);
        users.forEach(this::agregarUltimaModificacion);
        return users;
    }

    public List<Usuario> buscarInquilinosDeUsuario(String palabraBuscada, Long idPropietario){
        List<Usuario> users = usuarioRepository.findBySearchInquilinosDeUsuario(palabraBuscada, idPropietario);
        users.forEach(this::agregarUltimaModificacion);
        return users;
    }

    public UsuarioConDeptoDTO getInquilino(Long idInquilino) {
        Usuario inquilino = usuarioRepository.getById(idInquilino);
        Departamento depto = departamentoRepository.findByInquilinoAndBajaLogicaFalse(inquilino).get(0);
        UsuarioConDeptoDTO inquilinoDTO = UsuarioConDeptoDTO.fromUsuario(inquilino);
        inquilinoDTO.setPiso(depto.getPiso());
        inquilinoDTO.setNroDepartamento(depto.getNroDepartamento());
        inquilinoDTO.setTorre(depto.getTorre());
        return inquilinoDTO;
    }

    private void validarUsuario(Usuario usuario) throws DataConsistencyException {
        if (
            ValidationMethods.stringNullOVacio(usuario.getNombre()) ||
            ValidationMethods.stringNullOVacio(usuario.getApellido()) ||
            ValidationMethods.stringNullOVacio(usuario.getCorreo()) ||
            ValidationMethods.stringNullOFueraDeLimites(usuario.getDni(),7,8) ||
            ValidationMethods.datoNull(usuario.getTipo()) ||
            ValidationMethods.fechaPosteriorAHoy(usuario.getFechaNacimiento())
        ) throw new DataConsistencyException("Ha ocurrido un error con los datos ingresados. Verificalos e intentá de nuevo.");

    }

    public String buscarCorreoDePropietarioPorInquilino(Long idUsuario) {
        return usuarioRepository.buscarCorreoDePropietarioPorInquilino(idUsuario);
    }
}
