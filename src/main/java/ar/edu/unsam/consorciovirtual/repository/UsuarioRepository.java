package ar.edu.unsam.consorciovirtual.repository;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import ar.edu.unsam.consorciovirtual.domain.TipoUsuario;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCorreo(String correo);

    @Query(value = "SELECT u FROM Usuario u " +
            "WHERE u.bajaLogica = false " +
            "AND (u.nombreCompleto LIKE %:busqueda% " +
            "OR u.dni LIKE %:busqueda% " +
            "OR u.correo LIKE %:busqueda% " +
            "OR u.tipo LIKE %:busqueda%) ")
    List<Usuario> findBySearch(@Param("busqueda") String busqueda);

    List<Usuario> findByTipo(TipoUsuario tipo);

    Optional<Usuario> findByCorreoAndPasswordAndBajaLogicaFalse(String correo, String password);

    Boolean existsByCorreoAndDniAndBajaLogicaFalse(String correo, String dni);

    Boolean existsByCorreoAndBajaLogicaFalse(String correo);

    Boolean existsByCorreoAndBajaLogicaFalseAndIdNot(String correo, Long id);
    Optional<Usuario> findByCorreoAndBajaLogicaFalseAndIdNot(String correo, Long id);
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

    @Query(value = "SELECT unPropietario.correo , 0 AS clazz_ FROM usuario as unPropietario JOIN " +
            "departamento as unDepartamento WHERE :idInquilino = unDepartamento.id_inquilino AND " +
            "unDepartamento.id_propietario = unPropietario.id", nativeQuery = true)
    String buscarCorreoDePropietarioPorInquilino(@Param("idInquilino") Long idInquilino);
}

