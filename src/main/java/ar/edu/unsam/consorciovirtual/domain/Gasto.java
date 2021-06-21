package ar.edu.unsam.consorciovirtual.domain;

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
    private String tipo;//(Extraordinaria/Común, si se mantiene el string usar esta nomenclatura
                        // porque así busca el generador de expensas) agregar al DER
    private YearMonth periodo; // Definir formato de periodo y hacer validación
    private Double importe;
    private LocalDate fechaDeCreacion; // (diferencia entre DER y Vista)
    //falta relacionarlo con la/s factura/s

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idGasto")
    private List<Item> items = new ArrayList<Item>();

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
