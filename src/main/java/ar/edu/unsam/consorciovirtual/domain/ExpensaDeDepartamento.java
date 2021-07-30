package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Formula;

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
    @JsonIgnore
    private Boolean anulada=false;
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idDepartamento")
    private Departamento departamento;
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="idExpensaGeneral")
    private ExpensaGeneral expensaGeneral;
    private String estado = "Pendiente"; // Ni bien se genera una expensa, esta está como pendiente
    private String unidad;

    @ColumnTransformer(read = "valor_departamento_comun + valor_departamento_extraordinaria")
    private String montoTotal;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idResumen")
    private Documento resumenDeExpensa;
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

    public void cargarUnidadDepto(){
        unidad = departamento.getUnidad();
    }

    public void anularExpensa(){
        if(!estaPaga()){
            String titulo = resumenDeExpensa.getTitulo() + " (Reemplazado)";
            String detalle = resumenDeExpensa.getDescripcion() + " (Este comprobante fue reemplazado, " +
                    "dado que la expensa a la que hace referencia debió ser anulada)";
            resumenDeExpensa.setTitulo(titulo);
            resumenDeExpensa.setDescripcion(detalle);
            anulada = true;
        }
    }

    public Boolean estaPaga(){
        return fechaDePago != null;
    }

    public void pagarExpensa(Usuario usuario) {
        fechaDePago = LocalDate.now();
        pagador = usuario;
        estado = "Pagada";
    }

//    public String getEstado(){
//        if(estaPaga()){
//            estado = "Pagada";
//        }else{
//            estado = "Pendiente";
//        }
//        return estado;
//    }

    @JsonProperty("montoAPagar")
    public Double getMontoAPagar(){
        return (valorDepartamentoExtraordinaria+valorDepartamentoComun);
    }

    @JsonProperty("propietario")
    public String getPropietario(){
        return (departamento.getNombrePropietario());
    }

}
