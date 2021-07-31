package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class RegistroMensaje {


//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Id
    private Long usuarioId;
    private Long ultimoMensaje;
}
