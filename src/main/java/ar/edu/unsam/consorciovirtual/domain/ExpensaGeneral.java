package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ExpensaGeneral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private YearMonth periodo;
    private Double valorTotalExpensaComun;
    private Double valorTotalExpensaExtraordinaria;
    private Boolean anulada = false;

    @JsonIgnore
    @OneToMany(mappedBy = "expensaGeneral")
    private List<ExpensaDeDepartamento> listaDeExpensas = new ArrayList<>();

    /*METODOS*/
    public void anularExpensa(){
            anulada = true;
            listaDeExpensas.forEach(expensaDepto -> expensaDepto.anularExpensa());
    }

}
