package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentoDTOParaListado {
    private Long id;

    private String titulo;
    private String nombreAutor;
    private String enlaceDeDescarga;
    private LocalDate fechaCreacion;

    public static DocumentoDTOParaListado fromDocumento(Documento documento){
        DocumentoDTOParaListado documentoDTO = new DocumentoDTOParaListado();
        documentoDTO.id = documento.getId();
        documentoDTO.titulo = documento.getTitulo();
        documentoDTO.nombreAutor = documento.getNombreAutor();
        documentoDTO.enlaceDeDescarga = documento.getEnlaceDeDescarga();
        documentoDTO.fechaCreacion = documento.getFechaCreacion();
        return documentoDTO;
    }
}
