package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static ar.edu.unsam.consorciovirtual.utils.Constants.ZONE_ID_ARGENTINA;

@Data
@Entity
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idAutor;
    private String autor;
    private String texto;
    private LocalDateTime fechaHora = LocalDateTime.now(ZONE_ID_ARGENTINA);

}
