package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Mensaje;
import ar.edu.unsam.consorciovirtual.domain.RegistroMensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroMensajeRepository extends JpaRepository<RegistroMensaje, Long> {
}
