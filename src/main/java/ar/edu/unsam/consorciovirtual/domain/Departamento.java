package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@Entity
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String torre;
    private String piso;
    private String nroDepartamento;
    private Double porcentajeExpensa;
    private Integer metrosCuadrados;

    @JsonIgnore
    private Boolean bajaLogica = false;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idPropietario")
    private Usuario propietario;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idInquilino")
    private Usuario inquilino;

    @JsonProperty("nombrePropietario")
    public String getNombrePropietario(){
        return obtenerNombreYApellido(propietario);
    }

    @JsonProperty("nombreInquilino")
    public String getNombreInquilino(){
        return obtenerNombreYApellido(inquilino);
    }

    private String obtenerNombreYApellido(Usuario usuario){
        String nombre;
        if(usuario == null){
           nombre = "";
        }else{
            nombre = usuario.getNombre() +" "+usuario.getApellido();
        }
        return nombre;
    }



}
