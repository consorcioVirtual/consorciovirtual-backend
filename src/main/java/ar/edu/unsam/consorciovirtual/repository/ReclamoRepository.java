package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {

    List<Reclamo> findByIdAndBajaLogicaFalseOrAutorNombreContainingAndBajaLogicaFalseOrAutorApellidoContainingAndBajaLogicaFalseAndBajaLogicaFalseOrAsuntoContainingAndBajaLogicaFalseOrEstadoNombreEstadoContainingAndBajaLogicaFalse(Long id, String nombreAutor, String apellidoAutor, String asunto, String nombreEstado);

    @Query(value = "SELECT * FROM reclamo " +
            "WHERE baja_logica = false " +
            "AND id_autor = :idUsuario " +
            "AND (id = :idReclamo " +
//                "OR nombre_propietario LIKE %:nombreAutor% " +
//                "OR nombre_inquilino LIKE %:nombreInquilino% " +
//                "OR nombre_estado LIKE %:nombreEstado% " +
                "OR asunto LIKE %:asunto%)" , nativeQuery=true)
//    List <Reclamo> buscarPorUsuarioYFiltro(@Param("idUsuario") Long idUsuario, @Param("idReclamo") Long idReclamo, @Param("nombreAutor") String nombreAutor, @Param("apellidoAutor") String apellidoAutor, @Param("asunto") String asunto, @Param("nombreEstado") String nombreEstado);
    List <Reclamo> buscarPorUsuarioYFiltro(@Param("idUsuario") Long idUsuario, @Param("idReclamo") Long idReclamo, @Param("asunto") String asunto);

}
