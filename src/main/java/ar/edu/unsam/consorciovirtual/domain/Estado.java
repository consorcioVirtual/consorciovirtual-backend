package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreEstado;
    private String correspondeA; // Como acá manejamos todos los estados (de solicitudes y reclamos) necesitamos hacer la distinción

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idEstadoAnterior")
    private Estado estadoAnterior;
}
