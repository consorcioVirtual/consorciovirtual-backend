package ar.edu.unsam.consorciovirtual.domainDTO;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.domain.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
public class UsuarioConDeptoDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String dni;
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fechaNacimiento;
    private String torre;
    private String piso;
    private String nroDepartamento;

    public static UsuarioConDeptoDTO fromUsuario(Usuario usuario){
        UsuarioConDeptoDTO nuevoUsuario = new UsuarioConDeptoDTO();
        nuevoUsuario.setId(usuario.getId());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApellido(usuario.getApellido());
        nuevoUsuario.setCorreo(usuario.getCorreo());
        nuevoUsuario.setDni(usuario.getDni());
        nuevoUsuario.setFechaNacimiento(usuario.getFechaNacimiento());
        return nuevoUsuario;
    }
}
