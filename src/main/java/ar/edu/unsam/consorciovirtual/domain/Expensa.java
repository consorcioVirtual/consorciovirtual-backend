package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Expensa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String periodo; // Definir como tratar el periodo.
    private Double valorTotalOrdinaria;
    private Double valorTotalExtraordinaria;
    private Boolean anulada = false;

    /*Definir si los importes a pagar van en la misma expensa
    En el DER los planteamos juntos pero en la vista están separados*/
    private Double valorDepartamentoOrdinaria;
    private Double valorDepartamentoExtraordinaria;

    private LocalDate fechaDePago;

    /*Ver como registrarlo
    si se paga por fuera de la app (si es que se puede pagar por fuera)*/
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idUsuarioPagador")
    private Usuario pagador;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idDepartamento")
    private Departamento departamento;

    //Es necesario representar la relación calcula, que pusimos en el DER, con el admin de la app?

    /*METODOS*/
    public void calcularPorcentajeDePago(){
        valorDepartamentoOrdinaria= valorTotalOrdinaria*departamento.getPorcentajeExpensa()/100;
        valorDepartamentoExtraordinaria= valorTotalExtraordinaria*departamento.getPorcentajeExpensa()/100;
    }

    public void anularExpensa(){
        anulada = true;
    }

    public Boolean estaPaga(){
        return fechaDePago != null;
    }

    @JsonProperty("montoAPagar")
    public Double getMontoAPagar(){
        return (valorDepartamentoExtraordinaria+valorDepartamentoOrdinaria);
    }

    @JsonProperty("unidad")
    public String getUnidad(){
        return departamento.getPiso() + "º " + departamento.getNroDepartamento();
    }

}