package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

    List<Usuario> findByNombreContainingAndBajaLogicaFalseOrApellidoContainingOrDniContainingOrCorreoContaining(String nombre, String apellido, String dni, String correo);

    Usuario findByUsernameAndPassword(String username, String password);
}

