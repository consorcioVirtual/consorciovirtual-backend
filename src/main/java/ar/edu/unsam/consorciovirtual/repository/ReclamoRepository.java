package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.domain.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {

    List<Reclamo> findByIdAndBajaLogicaFalseOrAutorNombreContainingAndBajaLogicaFalseOrAutorApellidoContainingAndBajaLogicaFalseAndBajaLogicaFalseOrAsuntoContainingAndBajaLogicaFalseOrEstadoNombreEstadoContainingAndBajaLogicaFalse(Long id, String nombreAutor, String apellidoAutor, String asunto, String nombreEstado);

    @Query("SELECT r FROM Reclamo r " +
            "WHERE r.bajaLogica = false " +
            "AND r.autor.id = :idUsuario " +
            "AND (r.id = :idReclamo " +
                "OR r.autor.nombre LIKE %:nombreAutor% " +
                "OR r.autor.apellido LIKE %:apellidoAutor% " +
                "OR r.estado.nombreEstado LIKE %:nombreEstado% " +
                "OR asunto LIKE %:asunto%)")
    List <Reclamo> buscarPorUsuarioYFiltro(@Param("idUsuario") Long idUsuario, @Param("idReclamo") Long idReclamo, @Param("nombreAutor") String nombreAutor, @Param("apellidoAutor") String apellidoAutor, @Param("asunto") String asunto, @Param("nombreEstado") String nombreEstado);

}
