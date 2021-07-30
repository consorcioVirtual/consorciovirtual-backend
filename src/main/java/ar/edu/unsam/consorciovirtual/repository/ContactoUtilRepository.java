package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.ContactoUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactoUtilRepository extends JpaRepository<ContactoUtil, Long> {

    List<ContactoUtil> findAllByBajaLogicaFalseAndServicioContaining(String palabraBuscada);

    @Query(value = "SELECT * FROM contacto_util as contacto WHERE contacto.servicio LIKE %:palabraBuscada% " +
            "AND contacto.baja_logica = false;", nativeQuery = true)
    List<ContactoUtil> buscarTodos(@Param("palabraBuscada") String palabraBuscada);
}
