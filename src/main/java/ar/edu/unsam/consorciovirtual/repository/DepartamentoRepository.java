package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    List<Departamento> findByNroDepartamentoContainingAndBajaLogicaFalseOrNombrePropietarioContainingAndBajaLogicaFalseOrNombreInquilinoContainingAndBajaLogicaFalseOrPisoContainingAndBajaLogicaFalse(String nroDepartamento, String nombrePropietario, String nombreInquilino, String pisoDepto);

    List<Departamento> findByBajaLogicaFalse();

    @Query(value = "SELECT * FROM departamento " +
            "WHERE baja_logica = false " +
            "AND (id_propietario = :idUsuario OR id_inquilino = :idUsuario)" , nativeQuery=true)
    List <Departamento> buscarPorPropietarioOInquilino(Long idUsuario);

    @Query(value = "SELECT COUNT(*) FROM departamento " +
            "WHERE baja_logica = false " +
            "AND id_propietario = :idUsuario" , nativeQuery=true)
    Long cantidadDeptosDelUsuario(Long idUsuario);

    @Query(value = "SELECT id FROM departamento " +
            "WHERE baja_logica = false " +
            "AND id_propietario = :idUsuario" , nativeQuery=true)
    List <Long> buscarIdPorPropietario(Long idUsuario);

    @Query(value = "SELECT id FROM departamento " +
            "WHERE baja_logica = false " +
            "AND id_inquilino = :idUsuario" , nativeQuery=true)
    List <Long> buscarIdPorInquilino(Long idUsuario);

    @Query(value = "SELECT * FROM departamento " +
            "WHERE baja_logica = false " +
            "AND (id_propietario = :idUsuario OR id_inquilino = :idUsuario) " +
            "AND (nro_departamento LIKE %:nroDepartamento% " +
                "OR nombre_propietario LIKE %:nombrePropietario% " +
                "OR nombre_inquilino LIKE %:nombreInquilino% " +
                "OR piso LIKE %:pisoDepto%)" , nativeQuery=true)
    List <Departamento> buscarPorUsuarioYFiltro(@Param("idUsuario") Long idUsuario, @Param("nroDepartamento") String nroDepartamento, @Param("nombrePropietario") String nombrePropietario, @Param("nombreInquilino") String nombreInquilino, @Param("pisoDepto") String pisoDepto);

    List<Departamento> findByPropietarioAndInquilinoIsNullAndBajaLogicaFalse(Usuario propietario);

    List<Departamento> findByInquilinoAndBajaLogicaFalse( Usuario inquilino);

    @Query(value = "SELECT SUM(porcentaje_expensa) FROM departamento", nativeQuery=true)
    Double porcentajeDeExpensasCubierto();
}