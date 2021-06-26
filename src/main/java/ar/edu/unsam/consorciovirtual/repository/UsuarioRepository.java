package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCorreo(String correo);
//    Optional<Usuario> findByNombre(String nombre);

    List<Usuario> findByNombreContainingAndBajaLogicaFalseOrApellidoContainingAndBajaLogicaFalseOrDniContainingAndBajaLogicaFalseOrCorreoContainingAndBajaLogicaFalse(String nombre, String apellido, String dni, String correo);

    Usuario findByCorreoAndPassword(String correo, String password);

    //Trae un usuario por id que es adm de app o adm de consorcio
    //Se maneja por el número del enum estar atentos a no cambair el orden del mismo
    @Query(value = "SELECT * FROM usuario as unUsuario " +
            "WHERE unUsuario.id = :idAutor AND (unUsuario.tipo = 0 OR unUsuario.tipo = 1)", nativeQuery = true)
    Optional<Usuario> buscarAdministradorPorId(@Param("idAutor") Long idAutor);
}

