package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenido; //Ver si se le pone límite al largo del mensaje
    @JsonIgnore
    private LocalDateTime fechaYHora = LocalDateTime.now();

    @JsonIgnore
    @OneToOne()
    @JoinColumn(name="idMensajeCitado")
    private Mensaje mensajeCitado;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idAutor")
    private Usuario autor;

    /*METODOS*/
    @JsonProperty("mensajeCitado")
    public String getContenidoDeMensajeCitado(){
        String contenidoMensajeCitado;
        if(mensajeCitado == null){
            contenidoMensajeCitado = "";
        }else{
            contenidoMensajeCitado = mensajeCitado.getContenido();
        }
        return contenidoMensajeCitado;
    }

    @JsonProperty("nombreAutor")
    public String getNombreAutor(){
        return autor.getNombre() + " " + autor.getApellido();
    }

    private String extraerSubStringDeFecha(Integer inicio, Integer fin){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return formato.format(fechaYHora).substring(inicio, fin);
    }

    @JsonProperty("fecha")
    public String getFecha(){
        return extraerSubStringDeFecha(0, 10);
    }

    @JsonProperty("hora")
    public String getHora(){
        return extraerSubStringDeFecha(11, 16);
    }

}
