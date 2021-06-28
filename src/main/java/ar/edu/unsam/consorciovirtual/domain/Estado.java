package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreEstado;
    @JsonIgnore
    private String correspondeA; // Como acá manejamos todos los estados (de solicitudes y reclamos) necesitamos hacer la distinción
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idEstadoAnterior")
    private Estado estadoAnterior;
    @JsonIgnore
    private Boolean bajaLogica = false;
}
