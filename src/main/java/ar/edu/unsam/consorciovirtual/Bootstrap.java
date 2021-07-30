package ar.edu.unsam.consorciovirtual;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.EstadoRepository;
import ar.edu.unsam.consorciovirtual.repository.RegistroMensajeRepository;
import ar.edu.unsam.consorciovirtual.repository.RegistroModificacionRepository;
import ar.edu.unsam.consorciovirtual.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final AnuncioService anuncioService;
    private final MensajeService mensajeService;
    private final ReclamoService reclamoService;
    private final ContactoUtilService contactoUtilService;
    private final RegistroModificacionRepository registroModificacionRepository;
    private final RegistroMensajeRepository registroMensajeRepository;


    //Usuarios
    private final Usuario propietario1A = createUser("Santiago", "Ranieri", "santi.ranieri@gmail.com", "38830200", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Propietario, "110082132");
    private final Usuario propietario1B = createUser("Santiago", "Lopez Roth", "saanti1535@gmail.com", "40123423", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Propietario, "1167990094");
    private final Usuario propietario1C = createUser("Santiago", "Lorenzo", "santiilorenzo9499@gmail.com", "42543231", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Propietario, "1127900091");
    private final Usuario propietario2A = createUser("Pablo", "Vigliero", "pablovigliero@gmail.com", "36350120", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Propietario, "1133265545");
    private final Usuario propietario2B = createUser("Nahuel", "Ramos", "nahuelramos518@gmail.com", "36765908", LocalDate.of(1995, 8, 25),  "123", TipoUsuario.Propietario, "1167990090");
    private final Usuario propietario2C = createUser("Cecilia", "Lara", "santiilorenzo9499@gmail.com", "32332211", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Propietario, "1157992177");
    private final Usuario propietario3ABC = createUser("Roberto", "Rivas", "santiilorenzo9499@gmail.com", "32332212", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Propietario, "1167990090");
    private final Usuario administradorApp = createUser("Graciela", "Suarez", "nahueeh@live.com.ar", "32332211", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Administrador, "1167992292");
    private final Usuario administradorConsorcio = createUser("Maria", "Suarez", "nahueeh@live.com.ar", "32332211", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Administrador_consorcio, "1167992292");
    private final Usuario inquilino1A = createUser("Roberto", "Perez", "santi.ranieri@gmail.com", "32332212", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Inquilino, "1167990090");
    private final Usuario inquilino3A = createUser("Juan", "Perez", "nramos@estudiantes.unsam.edu.ar", "32332212", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Inquilino, "1167990090");
    private final Usuario inquilino3B = createUser("Paula", "Perez", "saanti1535@gmail.com", "32332212", LocalDate.of(1995, 8, 25), "123", TipoUsuario.Inquilino, "1167990090");


    //Departamentos (El que se cree tiene que tener un 11 como porcentaje de expensas)
    private final Departamento depto1A = createDepartamento("1", "1", "A", 10.5, 35, propietario1A, inquilino1A);
    private final Departamento depto1B = createDepartamento("1", "1", "B", 10.5, 15, propietario1B, null);
    private final Departamento depto1C = createDepartamento("1", "1", "C", 7.5, 20, propietario1C, null);
    private final Departamento depto2A = createDepartamento("1", "2", "A", 12.0, 45, propietario2A, null);
    private final Departamento depto2B = createDepartamento("1", "2", "B", 8.0, 45, propietario2B, null);
    private final Departamento depto2C = createDepartamento("1", "2", "C", 12.0, 45, propietario2C, null);
    private final Departamento depto3A = createDepartamento("1", "3", "A", 7.5, 45, propietario3ABC, inquilino3A);
    private final Departamento depto3B = createDepartamento("1", "3", "B", 9.5, 45, propietario3ABC, inquilino3B);
    private final Departamento depto3C = createDepartamento("1", "3", "C", 11.5, 45, propietario3ABC, null);

    //Estados
    private final Estado estadoPendienteAprobacion = createState("Pendiente de aprobación", "Solicitud tecnica", null);
    private final Estado estadoPendienteResolucion = createState("Pendiente de resolución", "Solicitud tecnica", estadoPendienteAprobacion);
    private final Estado estadoEnProceso = createState("En proceso", "Solicitud tecnica", estadoPendienteResolucion);
    private final Estado estadoResuelto = createState("Resuelto", "Solicitud tecnica", estadoEnProceso);
    private final Estado estadoRechazado = createState("Rechazado", "Solicitud tecnica", estadoEnProceso);

    //Notas de solicitudes/reclamos
    private final Nota notaSolicitud1 = createNota(administradorConsorcio.getNombre(), administradorConsorcio.getId(), "El técnico visitará el edificio el jueves", LocalDateTime.of(2021, 8, 15, 10, 30));
    private final Nota notaSolicitud2 = createNota(administradorConsorcio.getNombre(),administradorConsorcio.getId(), "El técnico solucionó el problema", LocalDateTime.of(2021, 7, 2, 15, 52));
    private final List<Nota> notasSolicitud = List.of(notaSolicitud1, notaSolicitud2);

    private final Nota notaReclamo1 = createNota(administradorConsorcio.getNombre(), administradorConsorcio.getId(), "Ya se hablo en la reunión de consorcio", LocalDateTime.of(2021, 8, 15, 10, 30));
    private final Nota notaReclamo2 = createNota(administradorConsorcio.getNombre(),administradorConsorcio.getId(), "Volví a hablarlo y va a empezar a cerrarla", LocalDateTime.of(2021, 7, 2, 15, 52));
    private final List<Nota> notasReclamos = List.of(notaReclamo1, notaReclamo2);


    //Solicitudes
    private final SolicitudTecnica solicitud1 = createSolicitudTecnica("Interna", "Me llueve el techo", "Cuando el vecino de arriba baldea el piso se me llueve el techo", LocalDate.of(2021, 06, 11), notasSolicitud, propietario1A, estadoPendienteResolucion);
    private final SolicitudTecnica solicitud2 = createSolicitudTecnica("Interna", "El piso filtra muy rápido", "Cuando baldeo el piso se me escurre re rápido el agua, ni idea a donde irá", LocalDate.of(2021, 07, 12),null, propietario3ABC, estadoEnProceso);
    private final SolicitudTecnica solicitud3 = createSolicitudTecnica("Interna", "El piso filtra muy rápidito", "Cuando baldeo el piso se me escurre re rápiditoooo el agua, ni idea a donde irá", LocalDate.of(2021, 06, 29), null, inquilino3B, estadoRechazado);
    private final SolicitudTecnica solicitud4 = createSolicitudTecnica("Interna", "Caño roto", "Se rompió un caño de agua en mi baño, se me inunda todo", LocalDate.of(2021, 07, 17),null, inquilino3A, estadoPendienteAprobacion);

    //Reclamos
    private final Reclamo reclamo1 = createReclamo("Mucho ruido en el edificio", "Despues de las 12 de la noche en el depto 24 ponen musica a todo volumen, perjudicando a los que tenemos que trabajar", LocalDate.of(2021,03,01), propietario2A, estadoEnProceso, null);
    private final Reclamo reclamo2 = createReclamo("El encargado deja la puerta abierta", "Varias veces el encargado sale del edificio y deja la puerta abierta, poniendo en riesgo la seguridad del edificio", LocalDate.of(2021,07,03), propietario3ABC, estadoPendienteResolucion, notasReclamos);
    private final Reclamo reclamo3 = createReclamo("Olor a gas en la entrada", "Cuando entro al edificio siento mucho olor a gas, puede haber una perdida", LocalDate.of(2021,11,13), propietario2C, estadoResuelto, null);

    //Gastos
    private final Gasto gasto1 = createGasto("Un gasto de limpieza", Rubro.LIMPIEZA, "Común",
            YearMonth.of(2021,5),500.25, LocalDate.of(2021,03,15), new ArrayList<>());
    private final Gasto gasto2 = createGasto("Sueldo Empleado", Rubro.SUELDOYCARGASSOCIALES, "Común",
            YearMonth.of(2021,5),50000.00, LocalDate.of(2021,03,01), new ArrayList<>());
    private final Gasto gasto3 = createGasto("Pintar el edificio", Rubro.MANTENIMIENTOPARTESCOMUNES, "Extraordinaria",
            YearMonth.of(2021,5),25300.40, LocalDate.of(2021,03,25), new ArrayList<>());
    private final Gasto gasto4 = createGasto("Gastos varios", Rubro.OTROS, "Común",
            YearMonth.of(2021,5),310.60, LocalDate.of(2021,03,31), new ArrayList<>());
    private final Gasto gasto5 = createGasto("Cuenta bancaria", Rubro.GASTOSBANCARIOS, "Común",
            YearMonth.of(2021,5),3200.00, LocalDate.of(2021,03,02), new ArrayList<>());

    //Gastos prueba para generar expensa (no cambiar periodo)
    private final Gasto gasto11 = createGasto("Sueldo Empleado", Rubro.SUELDOYCARGASSOCIALES, "Común",
            YearMonth.of(2021,3),50000.00, LocalDate.of(2021,03,01), new ArrayList<>());
    private final Gasto gasto12 = createGasto("Pintar el edificio", Rubro.MANTENIMIENTOPARTESCOMUNES, "Extraordinaria",
            YearMonth.of(2021,3),25300.40, LocalDate.of(2021,03,25), new ArrayList<>());
    private final Gasto gasto13 = createGasto("Fotocopias", Rubro.ADMINISTRACION, "Común",
            YearMonth.of(2021,3),2000.00, LocalDate.of(2021,03,01), new ArrayList<>());
    private final Gasto gasto14 = createGasto("Boleta de agua", Rubro.SERVICIOSPUBICOS, "Común",
            YearMonth.of(2021,3),840.40, LocalDate.of(2021,03,25), new ArrayList<>());
    private final Gasto gasto15 = createGasto("Productos de limpieza", Rubro.LIMPIEZA, "Común",
            YearMonth.of(2021,3),6000.00, LocalDate.of(2021,03,01), new ArrayList<>());
    private final Gasto gasto16 = createGasto("Arreglo del 2C", Rubro.REPARACIONESENUNIDADES, "Común",
            YearMonth.of(2021,3),37000.40, LocalDate.of(2021,03,25), new ArrayList<>());

    //Anuncios
    private final Anuncio anuncio1 = createAnuncio("Desinfección", "Se desnfectara el 01/08",
            LocalDate.of(2021,03,05), LocalDate.of(2021,8,05), administradorConsorcio);
    private final Anuncio anuncio2 = createAnuncio("Expensas", "En mayo habrá expensas extraordinarias",
            LocalDate.of(2021,04,25), LocalDate.of(2021,05,25), administradorConsorcio);
    private final Anuncio anuncio3 = createAnuncio("Ruidos Molestos", "Se reitera que no se pueden hacer ruidos molestos los días de semana luego de las  21:00hs",
            LocalDate.of(2021,01,01), LocalDate.of(2021,12,31), administradorApp);

    //Mensajes
    private final Mensaje mensaje1 = createMensaje("Hola, alguien vió unas llaves", propietario2A);
    private final Mensaje mensaje2 = createMensaje("Las perdí ayer", propietario2A);
    private final Mensaje mensaje3 = createMensaje("Creo que el el del tercero dijo que encontró unas", inquilino1A);


    //Contactos utiles
    private final ContactoUtil contactoUtil1 = createContactoUtil("Emergencia", "911", "Emergencia", "Emergencias Generales");
    private final ContactoUtil contactoUtil2 = createContactoUtil("Pedro Ramirez", "155555555", "Plomero", "Para emergencias 24hs");
    private final ContactoUtil contactoUtil3 = createContactoUtil("Electro", "4233 6587", "Electricista", "Si no atienden queda acá a unas cuadras el local");

    //Registro de Mensaje
    private final RegistroMensaje regPropietario1A = createRegistroMensaje(propietario1A,1L);
    private final RegistroMensaje regPropietario1B = createRegistroMensaje(propietario1B,2L);
    private final RegistroMensaje regPropietario1C = createRegistroMensaje(propietario1C,3L);
    private final RegistroMensaje regPropietario2A = createRegistroMensaje(propietario2A,4L);
    private final RegistroMensaje regPropietario2B = createRegistroMensaje(propietario2B,5L);
    private final RegistroMensaje regPropietario2C = createRegistroMensaje(propietario2C,6L);
    private final RegistroMensaje retRegPropietario3ABC = createRegistroMensaje(propietario3ABC,7L);
    private final RegistroMensaje regAdminApp = createRegistroMensaje(administradorApp,8L);
    private final RegistroMensaje getRegAdminConsorcio = createRegistroMensaje(administradorConsorcio,9L);
    private final RegistroMensaje regInquilino1A = createRegistroMensaje(inquilino1A,10L);
    private final RegistroMensaje regInquilino3A = createRegistroMensaje(inquilino3A,11L);
    private final RegistroMensaje regInquilino3B = createRegistroMensaje(inquilino3B,12L);

    //Métodos
    @Override
    public void afterPropertiesSet() throws Exception {
        registroModificacionRepository.deleteAll();
        
        createAllusers();
        createAllDepartamentos();
        createAllStates();
        createAllRequests();
        createAllGastos();
        createAllAnuncios();
        createAllMensajes();
        createAllReclamos();
        createAllContactosUtiles();
        createAllRegistros();
    }



    private Usuario createUser(String nombre, String apellido, String correo, String dni, LocalDate fechaNacimiento, String password, TipoUsuario tipo, String telefono) {
        Usuario newUser = new Usuario();
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        newUser.setCorreo(correo);
        newUser.setDni(dni);
        newUser.setFechaNacimiento(fechaNacimiento);
        newUser.setPassword(password);
        newUser.setTipo(tipo);
        newUser.setTelefono(telefono);
        return newUser;
    }

    private void createAllusers() {
        List<Usuario> usuarios = List.of(propietario1A, propietario1B, propietario1C, propietario2A, propietario2B, propietario2C, propietario3ABC,
                inquilino1A, inquilino3A, inquilino3B, administradorApp, administradorConsorcio);
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
        List<Departamento> departamentos = List.of(depto1A, depto1B, depto1C, depto2A, depto2B, depto2C, depto3A, depto3B, depto3C);
        departamentoService.registrarTodos(departamentos);
    }

    private Nota createNota(String autor, Long idAutor, String texto, LocalDateTime fechaHora) {
        Nota nota = new Nota();
        nota.setAutor(autor);
        nota.setIdAutor(idAutor);
        nota.setTexto(texto);
        nota.setFechaHora(fechaHora);

        return nota;
    }

    private SolicitudTecnica createSolicitudTecnica(String tipo, String titulo, String detalle, LocalDate fecha, List<Nota> notas, Usuario autor, Estado estado){
        var newRequest = new SolicitudTecnica();
        newRequest.setTipo(tipo);
        newRequest.setTitulo(titulo);
        newRequest.setDetalle(detalle);
        newRequest.setFecha(fecha);
        newRequest.setNotas(notas);
        newRequest.setAutor(autor);
        newRequest.setEstado(estado);
        newRequest.setNombreAutor(autor.getNombreYApellido());
        newRequest.getEstado().setNombreEstado(estado.getNombreEstado());

        return newRequest;
    }

    private Reclamo createReclamo(String asunto, String mensaje, LocalDate fecha, Usuario autor, Estado estado, List<Nota> notas) {
        Reclamo reclamo = new Reclamo();
        reclamo.setAutor(autor);
        reclamo.setAsunto(asunto);
        reclamo.setMensaje(mensaje);
        reclamo.setFecha(fecha);
        reclamo.setEstado(estado);
        reclamo.setNotas(notas);

        return reclamo;
    }

    private void createAllReclamos() {
        List<Reclamo> reclamos = List.of(reclamo1, reclamo2, reclamo3);
        reclamoService.registrarTodos(reclamos);
    }

    private void createAllRequests(){
        List<SolicitudTecnica> requests = List.of(solicitud1, solicitud2, solicitud3, solicitud4);
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
        List<Estado> states = List.of(estadoPendienteResolucion, estadoPendienteAprobacion, estadoEnProceso, estadoResuelto, estadoRechazado);
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

    private Anuncio createAnuncio(String titulo, String descripcion, LocalDate fechaCreacion, LocalDate fechaVencimiento, Usuario autor){
        Anuncio unAnuncio = new Anuncio();
        unAnuncio.setTitulo(titulo);
        unAnuncio.setDescripcion(descripcion);
        unAnuncio.setFechaCreacion(fechaCreacion);
        unAnuncio.setFechaVencimiento(fechaVencimiento);
        unAnuncio.setAutor(autor);
        return unAnuncio;
    }

    private void createAllAnuncios() {
        List<Anuncio> anuncios = List.of(anuncio1, anuncio2, anuncio3);
        anuncioService.registrarTodos(anuncios);
    }

    private Mensaje createMensaje(String _mensaje, Usuario _usuarioEmisor){
        Mensaje nuevoMensaje = new Mensaje();
        nuevoMensaje.setMensaje(_mensaje);
        nuevoMensaje.setUsuarioEmisor(_usuarioEmisor);
        return nuevoMensaje;
    }

    private void createAllMensajes() {
        List<Mensaje> mensajes = List.of(mensaje1, mensaje2, mensaje3);
        mensajeService.registrarTodos(mensajes);
    }

    private Factura createFactura(String titulo, String descripcion, String enlace, Usuario autor,
                                  LocalDate fechaFactura, String nrofactura, String puntoDeVenta,
                                  String cuitProveedor, String cuitReceptor, String _CAE, Double importe){
        Factura nuevaFactura = new Factura();
        nuevaFactura.setTitulo(titulo);
        nuevaFactura.setDescripcion(descripcion);
        nuevaFactura.setEnlaceDeDescarga(enlace);
        nuevaFactura.setAutor(autor);
        nuevaFactura.setFechaFactura(fechaFactura);
        nuevaFactura.setNumeroFactura(nrofactura);
        nuevaFactura.setPuntoDeVenta(puntoDeVenta);
        nuevaFactura.setCuitProveedor(cuitProveedor);
        nuevaFactura.setCuitReceptor(cuitReceptor);
        nuevaFactura.setCae(_CAE);
        nuevaFactura.setImporte(importe);
        return nuevaFactura;
    }

    private ContactoUtil createContactoUtil(String _nombre, String _telefono, String _servicio, String _anotacion){
        ContactoUtil nuevoContacto = new ContactoUtil();
        nuevoContacto.setNombre(_nombre);
        nuevoContacto.setTelefono(_telefono);
        nuevoContacto.setServicio(_servicio);
        nuevoContacto.setAnotacion(_anotacion);
        return nuevoContacto;
    }

    private void createAllContactosUtiles() {
        List<ContactoUtil> contactos = List.of(contactoUtil1, contactoUtil2, contactoUtil3);
        contactoUtilService.registrarTodos(contactos);
    }

    private RegistroMensaje createRegistroMensaje(Usuario usuario, Long usuarioId){
        RegistroMensaje registroMensaje = new RegistroMensaje();
        registroMensaje.setUsuarioId(usuarioId);
        registroMensaje.setUltimoMensaje(0L);
        return registroMensaje;
    }

    private void createAllRegistros(){
        List<RegistroMensaje> registros = List.of(regInquilino1A, regInquilino3A, regInquilino3B, regAdminApp, getRegAdminConsorcio, regPropietario1A,
                regPropietario1B, regPropietario1C, regPropietario2A, regPropietario2B, regPropietario2C, retRegPropietario3ABC);
        registroMensajeRepository.saveAll(registros);
    }
}
