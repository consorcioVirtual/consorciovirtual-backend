package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Nota;
import ar.edu.unsam.consorciovirtual.domain.Reclamo;
import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotaRepository  extends JpaRepository<Nota, Long> {

    @Query(value = "SELECT unReclamo.* , 0 AS clazz_ " +
            "FROM nota as unaNota JOIN reclamo as unReclamo ON unReclamo.id=unaNota.reclamo_id WHERE unaNota.id = :idNota" , nativeQuery=true)
    Reclamo obtenerReclamo(Long idNota);

    @Query(value = "SELECT unaSolicitud.* , 0 AS clazz_ FROM nota as unaNota JOIN solicitud_tecnica as unaSolicitud " +
            "ON unaSolicitud.id=unaNota.solicitud_id WHERE unaNota.id = :idNota" , nativeQuery=true)
    SolicitudTecnica obtenerSolicitud(Long idNota);

}
