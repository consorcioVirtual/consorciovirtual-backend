package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;


public interface GastoRepository  extends JpaRepository<Gasto, Long> {

    List<Gasto> findByFechaDeCreacionContaining(String unaFecha);

    @Query(value = "SELECT ungasto.importe, ungasto.tipo FROM gasto as ungasto " +
            "WHERE ungasto.periodo = :periodo AND ungasto.tipo = :tipo", nativeQuery = true)
    List<Double> findImporteByPeriodoAndByTipo(@Param("periodo") YearMonth periodo, @Param("tipo") String tipo);

    //TODO: Falta filtrado por períodos. Se va a hacer un desplegable en el front para que venga el tipo de dato que necesitamos
    List<Gasto> findByTituloContainingOrImporte(String titulo, Double importe);

    @Query(value = "SELECT * FROM gasto as ungasto " + "WHERE ungasto.periodo = :periodo", nativeQuery = true)
    List<Gasto> findGastosByPeriodo(@Param("periodo") YearMonth periodo);

}
