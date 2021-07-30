package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;

import static ar.edu.unsam.consorciovirtual.utils.Constants.ZONE_ID_ARGENTINA;

@Data
@Entity
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaCreacion = LocalDate.now(ZONE_ID_ARGENTINA);
    private LocalDate fechaVencimiento;
    @JsonIgnore
    private Boolean bajaLogica = false;

    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name="idAutor")
    private Usuario autor;

    /*METODOS*/
    @JsonProperty("nombreAutor")
    public String getNombreAutor(){
        return autor.getNombreYApellido();
    }

}
