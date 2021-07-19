package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentoDTOParaListado {
    private Long id;

    private String titulo;
    private String autor;
    private LocalDate fechaCreacion;
    private String modificado;
    private String archivo;

    public static DocumentoDTOParaListado fromDocumento(Documento documento){
        DocumentoDTOParaListado documentoDTO = new DocumentoDTOParaListado();
        documentoDTO.id = documento.getId();
        documentoDTO.titulo = documento.getTitulo();
        documentoDTO.autor = documento.getNombreAutor();
        documentoDTO.fechaCreacion = documento.getFechaCreacion();
        documentoDTO.modificado = "Falta";
        String[] divisionPorBarra = documento.getEnlaceDeDescarga().split("/");
        documentoDTO.archivo = divisionPorBarra[divisionPorBarra.length-1];
        return documentoDTO;
    }
}
