package ar.edu.unsam.consorciovirtual.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    private Boolean bajaLogica = false;

    @JsonIgnore
    private String password;

    private String username;
    private String nombre;
    private String apellido;
    private String correo;
    private String dni;
    private LocalDate fechaNacimiento;
}

