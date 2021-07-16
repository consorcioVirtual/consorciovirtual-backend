package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.SolicitudTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolicitudTecnicaRepository extends JpaRepository<SolicitudTecnica, Long> {
    List<SolicitudTecnica> findByBajaLogicaFalse();

    List<SolicitudTecnica> findByIdAndBajaLogicaFalseOrNombreAutorContainingAndBajaLogicaFalseOrTituloContainingAndBajaLogicaFalseOrEstadoNombreEstadoContainingAndBajaLogicaFalse(Long id, String nombreAutor, String titulo, String nombreEstado);


    @Query(value = "SELECT * FROM solicitud_tecnica " +
            "WHERE baja_logica = false " +
            "AND id_autor = :idUsuario " +
            "AND (id = :idSolicitud " +
                "OR nombre_autor LIKE %:nombreAutor% " +
//                "OR nombre_estado LIKE %:nombreEstado% " +
                "OR titulo LIKE %:titulo%)" , nativeQuery=true)
//    List<SolicitudTecnica> buscarPorUsuarioYFiltro(@Param("idUsuario") Long idUsuario, @Param("idSolicitud") Long idSolicitud, @Param("nombreAutor") String nombreAutor, @Param("titulo") String titulo, @Param("nombreEstado") String nombreEstado);
    List<SolicitudTecnica> buscarPorUsuarioYFiltro(@Param("idUsuario") Long idUsuario, @Param("idSolicitud") Long idSolicitud, @Param("nombreAutor") String nombreAutor, @Param("titulo") String titulo);

    List<SolicitudTecnica> findByTipo(String tipo);

}

