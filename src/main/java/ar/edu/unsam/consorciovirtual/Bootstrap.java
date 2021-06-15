package ar.edu.unsam.consorciovirtual;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.Estado;
import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.EstadoRepository;
import ar.edu.unsam.consorciovirtual.service.DepartamentoService;
import ar.edu.unsam.consorciovirtual.service.SolicitudTecnicaService;
import ar.edu.unsam.consorciovirtual.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Bootstrap implements InitializingBean {

    private final UsuarioService usuarioService;
    private final DepartamentoService departamentoService;
    private final SolicitudTecnicaService solicitudTecnicaService;
    private final EstadoRepository estadoRepository;


    //Usuarios
    private final Usuario santir = createUser("Santiago", "Ranieri", "santi.ranieri@gmail.com", "38.830.200", LocalDate.of(1995, 8, 25), "santiranieri", "123");
    private final Usuario santilr = createUser("Santiago", "Lopez Roth", "santi_kpo97@yahoo.com", "40.123.423", LocalDate.of(1995, 8, 25), "santilr", "123");
    private final Usuario santil = createUser("Santiago", "Lorenzo", "santilorenzo@gmail.com", "42.543.231", LocalDate.of(1995, 8, 25), "santil", "123");
    private final Usuario pablo = createUser("Pablo", "Vigliero", "pablitovig@hotmail.com", "36.350.120", LocalDate.of(1995, 8, 25), "pablito", "123");
    private final Usuario nahue = createUser("Nahue", "Ramos", "nahueramos@gmail.com", "36.765.908", LocalDate.of(1995, 8, 25), "nahue", "123");

    //Departamentos
    private final Departamento depto1 = createDepartamento(null, "1", "5", 6.5, 35, santil, null);
    private final Departamento depto2 = createDepartamento("1", "5", "1", 3.5, 15, santilr, pablo);

    //Estados
    private final Estado estadoPendiente = createState("Pendiente", "Solicitud tecnica", null);
    private final Estado estadoAprobado = createState("Aprobado", "Solicitud tecnica", estadoPendiente);

    //Solicitudes
    private final SolicitudTecnica solicitud1 = createSolicitudTecnica("Interna", "Me llueve el techo", "Cuando el vecino de arriba baldea el piso se me llueve el techo", LocalDate.of(2021, 06, 11), null, santir, estadoPendiente);
    private final SolicitudTecnica solicitud2 = createSolicitudTecnica("Interna", "El piso filtra muy rápido", "Cuando baldeo el piso se me escurre re rápido el agua, ni idea a donde irá", LocalDate.of(2021, 06, 10), null, nahue, estadoAprobado);


    //Métodos
    @Override
    public void afterPropertiesSet() throws Exception {
        createAllusers();
        createAllDepartamentos();
        createAllStates();
        createAllRequests();
    }

    private Usuario createUser(String nombre, String apellido, String correo, String dni, LocalDate fechaNacimiento, String username, String password) {
        Usuario newUser = new Usuario();
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        newUser.setCorreo(correo);
        newUser.setDni(dni);
        newUser.setFechaNacimiento(fechaNacimiento);
        newUser.setUsername(username);
        newUser.setPassword(password);
        return newUser;
    }

    private void createAllusers() {
        List<Usuario> usuarios = List.of(santir, santilr, santil, pablo, nahue);
        usuarioService.registrarTodos(usuarios);
    }

    private Departamento createDepartamento(String torre, String piso, String nroDepartamento, Double porcentajeExpensa, Integer metrosCuadrados, Usuario propietario, Usuario inquilino) {
        var newDepartamento = new Departamento();
        newDepartamento.setTorre(torre);
        newDepartamento.setPiso(piso);
        newDepartamento.setNroDepartamento(nroDepartamento);
        newDepartamento.setPorcentajeExpensa(porcentajeExpensa);
        newDepartamento.setMetrosCuadrados(metrosCuadrados);
        newDepartamento.setPropietario(propietario);
        newDepartamento.setInquilino(inquilino);

        return newDepartamento;
    }

    private void createAllDepartamentos() {
        List<Departamento> departamentos = List.of(depto1, depto2);
        departamentoService.registrarTodos(departamentos);
    }

    private SolicitudTecnica createSolicitudTecnica(String tipo, String titulo, String detalle, LocalDate fecha, List<String> comentarios, Usuario autor, Estado estado){
        var newRequest = new SolicitudTecnica();
        newRequest.setTipo(tipo);
        newRequest.setTitulo(titulo);
        newRequest.setDetalle(detalle);
        newRequest.setFecha(fecha);
//        newRequest.setComentarios(comentarios);
        newRequest.setAutor(autor);
        newRequest.setEstado(estado);

        return newRequest;
    }

    private void createAllRequests(){
        List<SolicitudTecnica> requests = List.of(solicitud1, solicitud2);
        solicitudTecnicaService.registrarTodos(requests);
    }

    private Estado createState(String nombreEstado, String correspondeA, Estado estadoAnterior){
        var newState = new Estado();
        newState.setNombreEstado(nombreEstado);
        newState.setCorrespondeA(correspondeA);
        newState.setEstadoAnterior(estadoAnterior);

        return newState;
    }

    private void createAllStates(){
        List<Estado> states = List.of(estadoPendiente, estadoAprobado);
        estadoRepository.saveAll(states); // Directo al repo porque no un controller no se usaría para nada más
    }
}
