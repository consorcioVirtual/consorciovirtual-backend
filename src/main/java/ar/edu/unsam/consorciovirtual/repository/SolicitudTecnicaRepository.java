package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolicitudTecnicaRepository extends JpaRepository<SolicitudTecnica, Long> {

    @Query(value = "SELECT *  FROM solicitud_tecnica solicitud " +
            "INNER JOIN estado e ON solicitud.id_estado = e.id " +
            "INNER JOIN usuario a ON solicitud.id_autor = a.id " +
            "WHERE solicitud.baja_logica = false " +
            "AND (e.nombre_estado = 'Pendiente' " +
            "OR a.tipo <> 'Inquilino')" +
            "AND (solicitud.id = :idSolicitud " +
            "OR solicitud.nombre_autor LIKE %:nombreAutor% " +
            "OR e.nombre_estado LIKE %:nombreEstado% " +
            "OR solicitud.titulo LIKE %:titulo%)" , nativeQuery=true)
    List<SolicitudTecnica> buscarCreadasPorUsuariosNoInquilinosOAprobadasPorPropietarios(@Param("idSolicitud") Long idSolicitud, @Param("nombreAutor") String nombreAutor, @Param("titulo") String titulo, @Param("nombreEstado") String nombreEstado);

    @Query(value = "SELECT * FROM solicitud_tecnica solicitud " +
            "INNER JOIN estado e ON solicitud.id_estado = e.id " +
            "WHERE solicitud.baja_logica = false " +
            "AND solicitud.id_autor = :idUsuario " +
            "AND (solicitud.id = :idSolicitud " +
            "OR solicitud.nombre_autor LIKE %:nombreAutor% " +
            "OR e.nombre_estado LIKE %:nombreEstado% " +
            "OR solicitud.titulo LIKE %:titulo%)" , nativeQuery=true)
    List<SolicitudTecnica> buscarPropias(@Param("idUsuario") Long idUsuario, @Param("idSolicitud") Long idSolicitud, @Param("nombreAutor") String nombreAutor, @Param("titulo") String titulo, @Param("nombreEstado") String nombreEstado);

    @Query(value = "SELECT * FROM solicitud_tecnica solicitud " +
            "INNER JOIN estado e ON solicitud.id_estado = e.id " +
            "INNER JOIN departamento d ON d.id_propietario=:idUsuario " +
            "WHERE solicitud.baja_logica = false " +
            "AND (solicitud.id_autor=d.id_inquilino " +
            "OR solicitud.id_autor=:idUsuario) " +
            "AND (solicitud.id = :idSolicitud " +
            "OR solicitud.nombre_autor LIKE %:nombreAutor% " +
            "OR e.nombre_estado LIKE %:nombreEstado% " +
            "OR solicitud.titulo LIKE %:titulo%)" , nativeQuery=true)
    List<SolicitudTecnica> buscarPropiasODeMisInquilinos(@Param("idUsuario") Long idUsuario, @Param("idSolicitud") Long idSolicitud, @Param("nombreAutor") String nombreAutor, @Param("titulo") String titulo, @Param("nombreEstado") String nombreEstado);

    List<SolicitudTecnica> findByTipo(String tipo);

}

