package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query(value="SELECT * FROM mensaje AS m WHERE m.mensaje LIKE %:palabraBuscada% ORDER BY m.id DESC", nativeQuery = true)
    List<Mensaje> findMensajesFiltrados(String palabraBuscada);
}
