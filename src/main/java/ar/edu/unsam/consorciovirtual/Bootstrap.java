package ar.edu.unsam.consorciovirtual;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.service.DepartamentoService;
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

    //Usuarios
    private Usuario santir = createUser("Santiago", "Ranieri", "santi.ranieri@gmail.com", "38.830.200", LocalDate.of(1995, 8, 25), "santiranieri");
    private Usuario santilr = createUser("Santiago", "Lopez Roth", "santi_kpo97@yahoo.com", "40.123.423", LocalDate.of(1995, 8, 25), "santilr");
    private Usuario santil = createUser("Santiago", "Lorenzo", "santilorenzo@gmail.com", "42.543.231", LocalDate.of(1995, 8, 25), "santil");
    private Usuario pablo = createUser("Pablo", "Vigliero", "pablitovig@hotmail.com", "36.350.120", LocalDate.of(1995, 8, 25), "pablito");
    private Usuario nahue = createUser("Nahue", "Ramos", "nahueramos@gmail.com", "36.765.908", LocalDate.of(1995, 8, 25), "nahue");

    //Departamentos
    private Departamento depto1= createDepartamento(null, "1","5", 6.5, 35, santil, null);
    private Departamento depto2= createDepartamento("1", "5","1", 3.5, 15, santilr, pablo);

    //Métodos
    @Override
    public void afterPropertiesSet() throws Exception {
        createAllusers();
        createAllDepartamentos();
    }

    private Usuario createUser(String nombre, String apellido, String correo, String dni, LocalDate fechaNacimiento, String username) {
        Usuario newUser = new Usuario();
        newUser.setNombre(nombre);
        newUser.setApellido(apellido);
        newUser.setCorreo(correo);
        newUser.setDni(dni);
        newUser.setFechaNacimiento(fechaNacimiento);
        newUser.setUsername(username);
        return newUser;
    }

    private void createAllusers(){
        List<Usuario> usuarios = List.of(santir, santilr, santil, pablo, nahue);
        usuarioService.registrarTodos(usuarios);
    }

    private Departamento createDepartamento(String torre, String piso, String nroDepartamento, Double porcentajeExpensa, Integer metrosCuadrados, Usuario propietario, Usuario inquilino){
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

    private void createAllDepartamentos(){
        List<Departamento> departamentos = List.of(depto1, depto2);
        departamentoService.registrarTodos(departamentos);
    }

}
