package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static ar.edu.unsam.consorciovirtual.utils.Constants.ZONE_ID_ARGENTINA;

@Data
@Entity
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String asunto;
    private String mensaje;
    private LocalDate fecha = LocalDate.now(ZONE_ID_ARGENTINA);

    @JsonIgnore
    private Boolean bajaLogica = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    public String ultimaModificacion;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "reclamo_id")
    private List<Nota> notas;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="idAutor")
    private Usuario autor;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="idEstado")
    private Estado estado;

    @JsonProperty("estado")
    private String getNombreEstado() {
        return estado.getNombreEstado();
    }

    @JsonProperty("nombreAutor")
    private String getNombreYApellidoAutor() {
        return autor.getNombreYApellido();
    }

    @JsonProperty("idAutor")
    private Long getIdAutor() {
        return autor.getId();
    }

    public Boolean tieneNotas(){ return !notas.isEmpty(); }
}
