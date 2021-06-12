package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class SolicitudTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private String titulo;
    private String detalle;
    private LocalDate fecha;
//    private List<String> comentarios;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idAutor")
    private Usuario autor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idEstado")
    private Estado estado;

//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name="idDocumento")
//    private List<Documento> documentosRelacionados;
}
