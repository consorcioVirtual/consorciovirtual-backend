package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class RegistroModificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TipoRegistro tipoRegistro;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long idModificado;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy - hh:mm")
    private LocalDateTime fechaModificacion;
    private String usuarioModificador;
}

