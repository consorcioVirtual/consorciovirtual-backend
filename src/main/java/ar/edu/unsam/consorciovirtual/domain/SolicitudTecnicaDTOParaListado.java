package ar.edu.unsam.consorciovirtual.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
public class SolicitudTecnicaDTOParaListado {
    private Long id;
    private String titulo;
    private LocalDate fecha;
    private String nombreAutor;
    private String nombreEstado;

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