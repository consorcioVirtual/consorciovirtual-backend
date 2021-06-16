package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.RegistroModificacion;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroModificacionRepository extends JpaRepository<RegistroModificacion, Long> {

    List<RegistroModificacion> findByTipoRegistroAndIdModificadoOrderByFechaModificacionAsc(TipoRegistro tipoRegistro, Long idModificado);
}
