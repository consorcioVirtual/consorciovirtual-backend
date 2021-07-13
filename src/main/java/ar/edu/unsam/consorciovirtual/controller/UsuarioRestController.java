package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.TipoUsuario;
import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.service.UsuarioService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UsuarioRestController {

    @Autowired
    private final UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public List<Usuario> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return this.usuarioService.buscarTodos(palabraBuscada);
    }

    @GetMapping("/usuario/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return this.usuarioService.buscarPorId(id);
    }

    @GetMapping("/buscar/usuario")
    public List<Usuario> buscarPorId(@RequestParam(required = true) TipoUsuario tipo) {
        return this.usuarioService.buscarPorTipo(tipo);
    }

    @PutMapping("/usuario/modificar")
    public void modificarUsuario(@RequestParam Long idLogueado, @RequestBody Usuario usuario) {
        usuarioService.modificar(idLogueado, usuario);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
       return usuarioService.loguearUsuario(usuario);
   }


    @PutMapping("/usuario/crear")
    public Usuario crearUsuario(@RequestBody String body) throws JsonProcessingException {
        Usuario newUser = new ObjectMapper().readValue(body, Usuario.class);
        return usuarioService.registrarUsuario(newUser);
    }

    @DeleteMapping("/usuario/eliminar/{idABorrar}")
    public void bajaLogicaUsuario(@RequestParam Long idLogueado, @PathVariable Long idABorrar) throws DataConsistencyException {
       usuarioService.bajaLogica(idLogueado, idABorrar);
    }

}