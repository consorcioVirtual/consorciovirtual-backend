package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type") @JsonSubTypes({
        @JsonSubTypes.Type(value = Documento.class, name = "documento"),
        @JsonSubTypes.Type(value = Factura.class, name = "factura")
})
@JsonTypeName("documento")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private String enlaceDeDescarga;
    private LocalDate fechaCreacion = LocalDate.now();
    private LocalDate fechaModificacion = LocalDate.now();
    @JsonIgnore
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

    public Boolean esValido(){
        return noEsNullNiVacio(titulo) && noEsNullNiVacio(descripcion) && noEsNullNiVacio(enlaceDeDescarga);
    }

    private Boolean noEsNullNiVacio(String unString){
        return unString != null && unString != "";
    }
}
