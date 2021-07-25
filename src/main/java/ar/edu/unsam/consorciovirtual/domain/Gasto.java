package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.YearMonth;
import java.util.stream.Collectors;

@Data
@Entity
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Rubro rubro;
    private String tipo;
    private YearMonth periodo;
    private Double importe;
    private LocalDate fechaDeCreacion = LocalDate.now();
    private String url;
    private Boolean anulado = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idGasto")
    private List<Item> items = new ArrayList<Item>();

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idComprobante")
    private Documento comprobante;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    public String ultimaModificacion;

    /*METODOS*/

    /*Verificar que el importe coincida con los valores de los item, no podemos sacar el importe en base a los items dado
    que la relación la marcamos como parcial*/
    public Boolean importeCoincideConSumaDeIntems(){
        double sumaDeItems = items.stream().mapToDouble(item->item.importeTotalItem()).sum();
        return sumaDeItems==this.importe;
    }

    public Boolean tieneItems(){
        return !items.isEmpty();
    }


}
