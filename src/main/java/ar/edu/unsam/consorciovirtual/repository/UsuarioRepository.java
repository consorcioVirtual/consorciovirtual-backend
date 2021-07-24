package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.TipoUsuario;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCorreo(String correo);

    @Query(value = "SELECT * FROM usuario as unUsuario " +
            "WHERE (unUsuario.nombre LIKE %:word% OR unUsuario.apellido LIKE %:word% OR unUsuario.dni LIKE %:word% OR unUsuario.correo LIKE %:word% OR unUsuario.tipo LIKE %:word%) AND (unUsuario.baja_logica = 0)", nativeQuery = true)
    List<Usuario> findBySearch(String word);

    List<Usuario> findByTipo(TipoUsuario tipo);

    Usuario findByCorreoAndPasswordAndBajaLogicaFalse(String correo, String password);

    //Trae un usuario por id que es adm de app o adm de consorcio
    //Se maneja por el número del enum estar atentos a no cambair el orden del mismo

    @Query(value = "SELECT * FROM usuario as unUsuario " +
            "WHERE unUsuario.id = :idAutor AND (unUsuario.tipo = 'Administrador' OR unUsuario.tipo = 'Administrador_Consorcio')", nativeQuery = true)
    Optional<Usuario> buscarAdministradorPorId(@Param("idAutor") Long idAutor);
    @Query(value = "SELECT * FROM usuario as unUsuario " +
            "WHERE unUsuario.tipo = 'Administrador_Consorcio' AND unUsuario.baja_logica = 0", nativeQuery = true)
    Optional<Usuario> buscarAdministradorDeConsorcioActivo();

    @Query(value = "SELECT COUNT(*) from usuario as unUsuario WHERE unUsuario.id = :idUsuario AND " +
            "(unUsuario.tipo = 'Administrador' OR unUsuario.tipo = 'Administrador_Consorcio')", nativeQuery = true)
    Integer esAdministrador(@Param("idUsuario") Long idUsuario);

    //QUERYS INQUILINO
    @Query(value = "SELECT * FROM usuario as u " +
            "WHERE (u.nombre LIKE %:word% OR u.apellido LIKE %:word% OR u.dni LIKE %:word% OR u.correo LIKE %:word% ) AND u.tipo = 'Inquilino' AND (u.baja_logica = 0)", nativeQuery = true)
    List<Usuario> findBySearchInquilino(String word);

    @Query(value = "select u.id, u.nombre, u.apellido, u.fecha_nacimiento, u.dni, u.baja_logica, u.correo, u.password, u.tipo from usuario u " +
            "inner join departamento d ON u.id = d.id_inquilino AND d.id_propietario = :idPropietario AND " +
            "(u.nombre LIKE %:word% OR u.apellido LIKE %:word% OR u.dni LIKE %:word% OR u.correo LIKE %:word%) AND u.baja_logica = 0", nativeQuery = true)
    List<Usuario> findBySearchInquilinosDeUsuario(String word, @Param("idPropietario") Long idPropietario);
}

