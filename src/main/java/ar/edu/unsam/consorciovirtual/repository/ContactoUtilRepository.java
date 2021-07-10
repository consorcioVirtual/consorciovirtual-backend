package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.ContactoUtil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactoUtilRepository extends JpaRepository<ContactoUtil, Long> {

    List<ContactoUtil> findAllByBajaLogicaFalseAndServicioContaining(String palabraBuscada);

}
