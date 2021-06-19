package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Rubro rubro;
    private String tipo;//(extraordinaria/comun) agregar al DER
    private String periodo; // Definir formato de periodo y hacer validación
    private Double importe;
    private LocalDate fechaDeCreacion; // (diferencia entre DER y Vista)
    //falta relacionarlo con la/s factura/s

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idGasto")
    private List<Item> items;

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
