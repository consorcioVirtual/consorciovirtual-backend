package ar.edu.unsam.consorciovirtual.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Data
@Document(collection = "registroModificacion")
public class RegistroModificacion {
    @Id
    @JsonIgnore
    private ObjectId id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private TipoRegistro tipoRegistro;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long idModificado;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy - hh:mm a")
    private LocalDateTime fechaHoraModificacion;
    private String usuarioModificador;
}

