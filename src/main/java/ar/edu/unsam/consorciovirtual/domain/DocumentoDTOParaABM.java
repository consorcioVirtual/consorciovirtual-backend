package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocumentoDTOParaABM {
    private Long id;

    private String titulo;
    private String autor;
    private String type;
    private String enlaceDeDescarga;
    private LocalDate fechaCreacion;
    private String descripcion;


    public static DocumentoDTOParaABM fromDocumento(Documento documento){
        DocumentoDTOParaABM documentoDTO = new DocumentoDTOParaABM();
        documentoDTO.id = documento.getId();
        documentoDTO.titulo = documento.getTitulo();
        documentoDTO.autor = documento.getNombreAutor();
        documentoDTO.fechaCreacion = documento.getFechaCreacion();
        documentoDTO.descripcion = documento.getDescripcion();
        documentoDTO.enlaceDeDescarga = documento.getEnlaceDeDescarga();
        if(documento instanceof Factura){
            documentoDTO.type = "factura";
        }else{
            documentoDTO.type = "documento";
        }
        return documentoDTO;
    }
}
