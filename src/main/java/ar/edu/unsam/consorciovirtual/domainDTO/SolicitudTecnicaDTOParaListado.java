package ar.edu.unsam.consorciovirtual.domainDTO;

import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SolicitudTecnicaDTOParaListado {
    private Long id;
    private String titulo;
    private LocalDate fecha;
    private String nombreAutor;
    private String nombreEstado;
    private String ultimaModificacion;

    public static SolicitudTecnicaDTOParaListado fromSolicitudTecnica(SolicitudTecnica solicitud){
        SolicitudTecnicaDTOParaListado solicitudDTO = new SolicitudTecnicaDTOParaListado();
        solicitudDTO.id = solicitud.getId();
        solicitudDTO.titulo = solicitud.getTitulo();
        solicitudDTO.fecha = solicitud.getFecha();
        solicitudDTO.nombreAutor = solicitud.getNombreAutor();
        solicitudDTO.nombreEstado = solicitud.getEstado().getNombreEstado();
        return solicitudDTO;
    }

}