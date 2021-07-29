package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaDeDepartamento;
import ar.edu.unsam.consorciovirtual.domain.ExpensaGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;

public interface ExpensaDeDepartamentoRepository extends JpaRepository<ExpensaDeDepartamento, Long> {
    List<ExpensaDeDepartamento> findByAnuladaFalse();


    @Query("SELECT e FROM ExpensaDeDepartamento e " +
            "WHERE e.anulada = false " +
            "AND (e.departamento.unidad1 LIKE %:busqueda% " +
            "OR e.departamento.unidad2 LIKE %:busqueda% " +
            "OR e.departamento.unidad3 LIKE %:busqueda% " +
            "OR e.estado LIKE %:busqueda% " +
            "OR e.montoTotal = :montoTotal)")
    List<ExpensaDeDepartamento> findBySearch(@Param("busqueda") String busqueda, @Param("montoTotal") Double montoTotal);

    List<ExpensaDeDepartamento> findByPeriodoAndAnuladaFalse(YearMonth periodo);


    //Es JPQL porque las native no soportan listas por parámetro
    @Query("SELECT ex FROM ExpensaDeDepartamento ex " +
            "WHERE ex.anulada = false " +
            "AND ex.departamento.id IN :idDeptos " +
            "AND (ex.departamento.unidad1 LIKE %:unidad% " +
            "OR ex.departamento.unidad2 LIKE %:unidad% " +
            "OR ex.departamento.unidad3 LIKE %:unidad% " +
                "OR ex.estado LIKE %:estado%)")
    List<ExpensaDeDepartamento> buscarPorIdDeptosYFiltro(@Param("idDeptos") List<Long> idDeptos, @Param("unidad") String unidad, @Param("estado") String estado);
}
