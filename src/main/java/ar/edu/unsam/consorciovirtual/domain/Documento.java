package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private String enlaceDeDescarga;
    private LocalDate fechaCreacion = LocalDate.now();
    private LocalDate fechaModificacion = LocalDate.now();
    private Boolean bajaLogica = false;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idAutor")
    private Usuario autor;

    /*Métodos*/
    @JsonProperty("nombreAutor")
    public String getNombreAutor(){
        return autor.getNombre() + " " + autor.getApellido();
    }
}
