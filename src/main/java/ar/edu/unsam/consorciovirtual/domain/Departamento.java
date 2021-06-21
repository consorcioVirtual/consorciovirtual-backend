package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


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
    private String nombrePropietario;
    private String nombreInquilino;

    @JsonIgnore
    private Boolean bajaLogica = false;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idPropietario")
    private Usuario propietario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idInquilino")
    private Usuario inquilino;

    @JsonIgnore
    @OneToMany(mappedBy = "departamento")
    private List<ExpensaDeDepartamento> listaDeExpensas;

    /*METODOS*/
    public Boolean tieneExpensasImpagas(){
        return getListaDeExpensas().stream().anyMatch(exp -> !exp.getAnulada() && !exp.estaPaga());
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, namespace = "estadoDeCuenta")
    public String getEstadoDeCuenta(){
        String estadoDeCuenta;
        if (tieneExpensasImpagas()){
            estadoDeCuenta="Pendiente";
        }else{
            estadoDeCuenta="Pagado";
        }
        return estadoDeCuenta;
    }

    public String getUnidad(){
        return piso + nroDepartamento;
    }

}
