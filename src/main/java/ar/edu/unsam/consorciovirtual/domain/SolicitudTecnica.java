package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static ar.edu.unsam.consorciovirtual.utils.Constants.ZONE_ID_ARGENTINA;

@Data
@Entity
public class SolicitudTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private String titulo;
    private String detalle;
    private LocalDate fecha = LocalDate.now(ZONE_ID_ARGENTINA);
    private String nombreAutor;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "solicitud_id")
    private List<Nota> notas;

    @JsonIgnore
    private Boolean bajaLogica = false;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="idAutor")
    private Usuario autor;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="idEstado")
    private Estado estado;

    public boolean tieneNotas() { return !notas.isEmpty(); }


//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name="idDocumento")
//    private List<Documento> documentosRelacionados;
}
