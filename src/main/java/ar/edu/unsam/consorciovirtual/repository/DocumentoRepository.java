package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Query(value = "SELECT unDocumento.* , 0 AS clazz_ FROM documento as unDocumento JOIN usuario as unUsuario ON unUsuario.id=unDocumento.id_autor" +
            " WHERE unDocumento.baja_logica = false AND " +
            "(unDocumento.titulo LIKE %:palabraBuscada% OR unDocumento.enlace_de_descarga LIKE %:palabraBuscada%" +
            " OR unUsuario.nombre LIKE %:palabraBuscada% OR unUsuario.apellido LIKE %:palabraBuscada%)", nativeQuery = true)
    List<Documento> findByBajaLogicaFalse(String palabraBuscada);

    Optional<Documento> findByIdAndBajaLogicaFalse(Long id);

    Optional<Documento> findByTituloContaining(String nombreSimple);
}
