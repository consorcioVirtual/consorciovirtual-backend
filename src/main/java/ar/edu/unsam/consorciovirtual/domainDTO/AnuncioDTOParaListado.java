package ar.edu.unsam.consorciovirtual.domainDTO;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AnuncioDTOParaListado {
    private Long id;
    private String titulo;
    private String nombreAutor;
    private LocalDate fechaCreacion;
    private LocalDate fechaVencimiento;
    private String modificado;
    private String ultimaModificacion;

    public static AnuncioDTOParaListado fromAnuncio(Anuncio anuncio){
      AnuncioDTOParaListado anuncioDTO = new AnuncioDTOParaListado();
      anuncioDTO.id = anuncio.getId();
      anuncioDTO.titulo = anuncio.getTitulo();
      anuncioDTO.nombreAutor = anuncio.getNombreAutor();
      anuncioDTO.fechaCreacion = anuncio.getFechaCreacion();
      anuncioDTO.fechaVencimiento = anuncio.getFechaVencimiento();
      anuncioDTO.modificado = "Falta";
      return anuncioDTO;
    }

}
