package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {

    @Query(value = "SELECT * FROM anuncio as unAnuncio " +
            "WHERE unAnuncio.fecha_vencimiento > :fecha AND unAnuncio.baja_logica = false AND " +
            "(unAnuncio.titulo LIKE %:palabraBuscada% OR unAnuncio.descripcion LIKE %:palabraBuscada%)", nativeQuery = true)
    List<Anuncio> buscarPorFechaDeVencimientoPosterior(@Param("fecha") LocalDate fecha, @Param("palabraBuscada") String palabraBuscada);

    @Query(value = "SELECT * FROM anuncio as unAnuncio " +
            "WHERE unAnuncio.baja_logica = false AND " +
            "(unAnuncio.titulo LIKE %:palabraBuscada% OR unAnuncio.descripcion LIKE %:palabraBuscada%)", nativeQuery = true)
    List<Anuncio> findByBajaLogicaFalse(@Param("palabraBuscada") String palabraBuscada);


    @Query(value = "SELECT a FROM Anuncio a " +
            "WHERE a.bajaLogica = false " +
            "AND (a.autor.nombreCompleto LIKE %:busqueda% " +
            "OR a.titulo LIKE %:busqueda%)")
    List<Anuncio> findBySearch(@Param("busqueda") String busqueda);
}
