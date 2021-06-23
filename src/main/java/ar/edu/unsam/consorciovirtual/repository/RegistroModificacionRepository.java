package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.RegistroModificacion;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RegistroModificacionRepository extends MongoRepository<RegistroModificacion, Long> {

    List<RegistroModificacion> findByTipoRegistroAndIdModificadoOrderByFechaHoraModificacionAsc(TipoRegistro tipoRegistro, Long idModificado);
}
