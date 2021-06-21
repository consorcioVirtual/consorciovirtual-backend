package ar.edu.unsam.consorciovirtual;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.EstadoRepository;
import ar.edu.unsam.consorciovirtual.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Bootstrap implements InitializingBean {

    private final UsuarioService usuarioService;
    private final DepartamentoService departamentoService;
    private final SolicitudTecnicaService solicitudTecnicaService;
    private final EstadoRepository estadoRepository;
    private final GastoService gastoService;
    private final GeneradorDeExpensas generadorDeExpensas;


    //Usuarios
    private final Usuario santir = createUser("Santiago", "Ranieri", "test@test.com", "38830200", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Administrador);
    private final Usuario santilr = createUser("Santiago", "Lopez Roth", "santi_kpo97@yahoo.com", "40123423", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Administrador);
    private final Usuario santil = createUser("Santiago", "Lorenzo", "santilorenzo@gmail.com", "42543231", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Administrador);
    private final Usuario pablo = createUser("Pablo", "Vigliero", "pablitovig@hotmail.com", "36350120", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Administrador);
    private final Usuario nahue = createUser("Nahue", "Ramos", "nahueramos@gmail.com", "36765908", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Administrador);
    private final Usuario juan = createUser("Juan", "Perez", "juanperez@gmail.com", "32332211", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Inquilino);
    private final Usuario maria = createUser("Maria", "Perez", "test", "32332211", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Administrador_consorcio);

    //Departamentos
    private final Departamento depto1 = createDepartamento(null, "1", "D", 6.5, 35, santil, null);
    private final Departamento depto2 = createDepartamento("1", "5", "E", 3.5, 15, santilr, pablo);
    private final Departamento depto3 = createDepartamento("1", "6", "A", 4.5, 20, santilr, nahue);
    private final Departamento depto4 = createDepartamento("1", "2", "C", 8.0, 45, santir, null);

    //Estados
    private final Estado estadoPendiente = createState("Pendiente", "Solicitud tecnica", null);
    private final Estado estadoAprobado = createState("Aprobado", "Solicitud tecnica", estadoPendiente);

    //Solicitudes
    private final SolicitudTecnica solicitud1 = createSolicitudTecnica("Interna", "Me llueve el techo", "Cuando el vecino de arriba baldea el piso se me llueve el techo", LocalDate.of(2021, 06, 11), null, santir, estadoPendiente);
    private final SolicitudTecnica solicitud2 = createSolicitudTecnica("Interna", "El piso filtra muy rápido", "Cuando baldeo el piso se me escurre re rápido el agua, ni idea a donde irá", LocalDate.of(2021, 06, 10), null, nahue, estadoAprobado);

    //Gastos
    private final Gasto gasto1 = createGasto("Un gasto de limpieza", Rubro.LIMPIEZA, "Común",
            YearMonth.of(2021,1),500.25, LocalDate.of(2021,03,15), new ArrayList<>());
    private final Gasto gasto2 = createGasto("Sueldo Empleado", Rubro.SUELDOYCARGASSOCIALES, "Común",
            YearMonth.of(2020,3),50000.00, LocalDate.of(2021,03,01), new ArrayList<>());
    private final Gasto gasto3 = createGasto("Pintar el edificio", Rubro.MANTENIMIENTOPARTESCOMUNES, "Extraordinaria",
            YearMonth.of(2024,8),25300.40, LocalDate.of(2021,03,25), new ArrayList<>());
    private final Gasto gasto4 = createGasto("Gastos varios", Rubro.OTROS, "Común",
            YearMonth.of(2029,5),310.60, LocalDate.of(2021,03,31), new ArrayList<>());
    private final Gasto gasto5 = createGasto("Cuenta bancaria", Rubro.GASTOSBANCARIOS, "Común",
            YearMonth.of(2019,2),3200.00, LocalDate.of(2021,03,02), new ArrayList<>());

    //Gastos prueba para generar expensa (no cambiar periodo)
    private final Gasto gasto11 = createGasto("Sueldo Empleado", Rubro.SUELDOYCARGASSOCIALES, "Común",
            YearMonth.of(2021,3),50000.00, LocalDate.of(2021,03,01), new ArrayList<>());
    private final Gasto gasto12 = createGasto("Pintar el edificio", Rubro.MANTENIMIENTOPARTESCOMUNES, "Extraordinaria",
            YearMonth.of(2021,3),25300.40, LocalDate.of(2021,03,25), new ArrayList<>());
    private final Gasto gasto13 = createGasto("Sueldo Empleado", Rubro.SUELDOYCARGASSOCIALES, "Común",
            YearMonth.of(2021,3),50000.00, LocalDate.of(2021,03,01), new ArrayList<>());
    private final Gasto gasto14 = createGasto("Pintar el edificio", Rubro.MANTENIMIENTOPARTESCOMUNES, "Extraordinaria",
            YearMonth.of(2021,3),25300.40, LocalDate.of(2021,03,25), new ArrayList<>());
    private final Gasto gasto15 = createGasto("Sueldo Empleado", Rubro.SUELDOYCARGASSOCIALES, "Común",
            YearMonth.of(2021,3),50000.00, LocalDate.of(2021,03,01), new ArrayList<>());
    private final Gasto gasto16 = createGasto("Pintar el edificio", Rubro.MANTENIMIENTOPARTESCOMUNES, "Extraordinaria",
            YearMonth.of(2021,3),25300.40, LocalDate.of(2021,03,25), new ArrayList<>());


    //Métodos
    @Override
    public void afterPropertiesSet() throws Exception {
        createAllusers();
        createAllDepartamentos();
        createAllStates();
        createAllRequests();
        createAllGastos();
        generadorDeExpensas.generarExpensasPorImportePredefinido(200000.00, 15000.00, YearMonth.of(2021,04));
        generadorDeExpensas.generarExpensasPorImporteDeGastos(YearMonth.of(2021,03));
    }

    private Usuario createUser(String nombre, String apellido, String correo, String dni, LocalDate fechaNacimiento, String password, TipoUsuario tipo) {
        Usuario newUser = new Usuario();
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        newUser.setCorreo(correo);
        newUser.setDni(dni);
        newUser.setFechaNacimiento(fechaNacimiento);
        newUser.setPassword(password);
        newUser.setTipo(tipo);
        return newUser;
    }

    private void createAllusers() {
        List<Usuario> usuarios = List.of(santir, santilr, santil, pablo, nahue, juan, maria);
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
        newDepartamento.setNombrePropietario(propietario.getNombreYApellido());
        if(inquilino != null){
            newDepartamento.setNombreInquilino(inquilino.getNombreYApellido());
        }

        return newDepartamento;
    }

    private void createAllDepartamentos() {
        List<Departamento> departamentos = List.of(depto1, depto2, depto3, depto4);
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
        newRequest.setNombreAutor(autor.getNombreYApellido());
        newRequest.setNombreEstado(estado.getNombreEstado());

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

    private Gasto createGasto(String _titulo, Rubro _rubro, String _tipo,
                              YearMonth _periodo, Double _importe, LocalDate _fechaDeCreacion, List<Item> _items){
        Gasto unGasto = new Gasto();
        unGasto.setTitulo(_titulo);
        unGasto.setRubro(_rubro);
        unGasto.setTipo(_tipo);
        unGasto.setPeriodo(_periodo);
        unGasto.setImporte(_importe);
        unGasto.setFechaDeCreacion(_fechaDeCreacion);
        unGasto.setItems(_items);
        return unGasto;
    }

    private void createAllGastos() {
        List<Gasto> gastos = List.of(gasto1, gasto2, gasto3, gasto4, gasto5, gasto11, gasto12, gasto13, gasto14, gasto15, gasto16);
        gastoService.registrarTodos(gastos);
    }

}
