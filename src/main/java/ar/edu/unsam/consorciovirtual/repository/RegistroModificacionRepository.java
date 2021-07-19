package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.RegistroModificacion;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface RegistroModificacionRepository extends MongoRepository<RegistroModificacion, String> {

    List<RegistroModificacion> findByTipoRegistroAndIdModificadoOrderByFechaHoraModificacionAsc(TipoRegistro tipoRegistro, Long idModificado);

    RegistroModificacion findFirstByTipoRegistroAndIdModificadoOrderByFechaHoraModificacionDesc(TipoRegistro tipoRegistro, Long idModificado);
}
