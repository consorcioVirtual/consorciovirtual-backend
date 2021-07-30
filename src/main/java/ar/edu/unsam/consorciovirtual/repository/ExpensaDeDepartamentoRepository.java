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

    //TODO: Falta filtrado por períodos. Se va a hacer un desplegable en el front para que venga el tipo de dato que necesitamos
    List<ExpensaDeDepartamento> findByUnidadContainingAndAnuladaFalseOrEstadoContainingAndAnuladaFalse(String unidad, String estado);

    List<ExpensaDeDepartamento> findByPeriodoAndAnuladaFalse(YearMonth periodo);


    //Es JPQL porque las native no soportan listas por parámetro
    @Query("SELECT ex FROM ExpensaDeDepartamento ex " +
            "WHERE ex.anulada = false " +
            "AND ex.departamento.id IN :idDeptos " +
            "AND (ex.unidad LIKE %:unidad% " +
                "OR ex.estado LIKE %:estado%)")
    List<ExpensaDeDepartamento> buscarPorIdDeptosYFiltro(@Param("idDeptos") List<Long> idDeptos, @Param("unidad") String unidad, @Param("estado") String estado);
}
