package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@Data
@Entity
public class Departamento {

    @JsonView(Views.DepartamentoPisoNro.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String torre;
    @JsonView(Views.DepartamentoPisoNro.class)
    private String piso;
    @JsonView(Views.DepartamentoPisoNro.class)
    private String nroDepartamento;
    private Double porcentajeExpensa;
    private Integer metrosCuadrados;
    private String nombrePropietario;
    private String nombreInquilino;

    @JsonIgnore
    private Boolean bajaLogica = false;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="idPropietario")
    private Usuario propietario;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="idInquilino")
    private Usuario inquilino;

    @JsonIgnore
    @OneToMany(mappedBy = "departamento")
    private List<ExpensaDeDepartamento> listaDeExpensas;

    /*METODOS*/
    public Boolean tieneExpensasImpagas(){
        return getListaDeExpensas().stream().anyMatch(exp -> !exp.getAnulada() && !exp.estaPaga());
    }

    @JsonIgnore
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

    public void quitarInquilino(){
        setInquilino(null);
        setNombreInquilino(null);
    }

    public void quitarPropietario(){
        setPropietario(null);
        setNombrePropietario(null);
    }

    public Boolean esPropietario(Usuario usuario){
        return getPropietario().getId().equals(usuario.getId());
    }
}
