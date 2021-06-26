package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaCreacion = LocalDate.now();
    private LocalDate fechaVencimiento;
    @JsonIgnore()
    private Boolean bajaLogica = false;

    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name="idAutor")
    private Usuario autor;

    /*METODOS*/
    @JsonProperty("nombreAutor")
    public String getNombreAutor(){
        return autor.getNombre() + " " + autor.getApellido();
    }

}
