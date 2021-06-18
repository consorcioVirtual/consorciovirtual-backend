package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double importeUnitario;
    private Double cantidad;
    //private tipo; De momento descartada
    private String descripcion;

    public Double importeTotalItem(){
        return this.cantidad*this.importeUnitario;
    }
}

