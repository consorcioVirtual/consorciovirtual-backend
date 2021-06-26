package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

    @Query(value = "SELECT * FROM anuncio as unAnuncio " +
            "WHERE unAnuncio.fecha_vencimiento > :fecha AND unAnuncio.baja_logica = false", nativeQuery = true)
    List<Anuncio> buscarPorFechaDeVencimientoPosterior(@Param("fecha") LocalDate fecha);

    List<Anuncio> findByBajaLogicaFalse();
}
