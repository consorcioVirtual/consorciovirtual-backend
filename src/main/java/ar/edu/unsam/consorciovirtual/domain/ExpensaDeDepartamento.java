package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Entity
public class ExpensaDeDepartamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double valorDepartamentoComun;
    private Double valorDepartamentoExtraordinaria;
    private YearMonth periodo;
    private Boolean anulada=false;
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idDepartamento")
    private Departamento departamento;
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idExpensaGeneral")
    private ExpensaGeneral expensaGeneral;

    //Para definir: Es necesario representar la relación calcula, que pusimos en el DER, con el admin de la app?

    /*ATRIBUTOS PARA EL PAGO*/
    private LocalDate fechaDePago;
    /*Ver como registrarlo
    si se paga por fuera de la app (si es que se puede pagar por fuera)*/
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idUsuarioPagador")
    private Usuario pagador;


    /*METODOS*/
    public Double montoTotalExpensaComun(){
        return expensaGeneral.getValorTotalExpensaComun();
    }
    public Double montoTotalExpensaExtraordinaria(){
        return expensaGeneral.getValorTotalExpensaExtraordinaria();
    }
    public Double calcularPorcentajeDePago(Double valor){
        return valor*departamento.getPorcentajeExpensa()/100;
    }
    public void cargarImportesYPeriodo(){
        periodo = expensaGeneral.getPeriodo();
        valorDepartamentoComun= (double)Math.round(calcularPorcentajeDePago(montoTotalExpensaComun())* 100d) / 100d;
        valorDepartamentoExtraordinaria= (double)Math.round(calcularPorcentajeDePago(montoTotalExpensaExtraordinaria())* 100d) / 100d;
    }

    public void anularExpensa(){
        anulada = true;
    }

    public Boolean estaPaga(){
        return fechaDePago != null;
    }

    @JsonProperty("montoAPagar")
    public Double getMontoAPagar(){
        return (valorDepartamentoExtraordinaria+valorDepartamentoComun);
    }

    @JsonProperty("unidad")
    public String getUnidad(){
        return departamento.getPiso() + "º " + departamento.getNroDepartamento();
    }
}