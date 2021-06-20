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
    private final ExpensaService expensaService;


    //Usuarios
    private final Usuario santir = createUser("Santiago", "Ranieri", "santi.ranieri@gmail.com", "38.830.200", LocalDate.of(1995, 8, 25), "santiranieri", "123");
    private final Usuario santilr = createUser("Santiago", "Lopez Roth", "santi_kpo97@yahoo.com", "40.123.423", LocalDate.of(1995, 8, 25), "santilr", "123");
    private final Usuario santil = createUser("Santiago", "Lorenzo", "santilorenzo@gmail.com", "42.543.231", LocalDate.of(1995, 8, 25), "santil", "123");
    private final Usuario pablo = createUser("Pablo", "Vigliero", "pablitovig@hotmail.com", "36.350.120", LocalDate.of(1995, 8, 25), "pablito", "123");
    private final Usuario nahue = createUser("Nahue", "Ramos", "nahueramos@gmail.com", "36.765.908", LocalDate.of(1995, 8, 25), "nahue", "123");

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

    //Expensas
    private final Expensa expensaImpaga1 = createExpensa("marzo 2021", 150000.00, 0.00,
            null, null, depto1);
    private final Expensa expensaImpaga2 = createExpensa("febrero 2021", 150000.00, 0.00,
            null, null, depto1);
    private final Expensa expensaImpaga3 = createExpensa("enero 2021", 150000.00, 0.00,
            null, null, depto1);
    private final Expensa expensaPaga1 = createExpensa("marzo 2021", 150000.00, 0.00,
            LocalDate.of(2021,03,01), depto2.getInquilino(), depto2);

    //Métodos
    @Override
    public void afterPropertiesSet() throws Exception {
        createAllusers();
        createAllDepartamentos();
        createAllStates();
        createAllRequests();
        createAllGastos();
        createAllExpensas();
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
        List<Gasto> gastos = List.of(gasto1, gasto2, gasto3, gasto4, gasto5);
        gastoService.registrarTodos(gastos);
    }

    private Expensa createExpensa(String _periodo, Double _valorTotalOrdinaria, Double _valorTotalExtraordinaria,
                                  LocalDate _fechaDePago, Usuario _pagador, Departamento _departamento){
        Expensa unaExpensa = new Expensa();
        unaExpensa.setPeriodo(_periodo);
        unaExpensa.setValorTotalOrdinaria(_valorTotalOrdinaria);
        unaExpensa.setValorTotalExtraordinaria(_valorTotalExtraordinaria);
        unaExpensa.setFechaDePago(_fechaDePago);
        unaExpensa.setPagador(_pagador);
        unaExpensa.setDepartamento(_departamento);
        unaExpensa.calcularPorcentajeDePago();
        return unaExpensa;
    }

    private void createAllExpensas() {
        List<Expensa> expensas = List.of(expensaImpaga1, expensaImpaga2, expensaImpaga3, expensaPaga1);
        expensaService.registrarTodos(expensas);
    }

}
