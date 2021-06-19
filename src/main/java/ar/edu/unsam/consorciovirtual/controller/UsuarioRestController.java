package ar.edu.unsam.consorciovirtual.controller;

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
@CrossOrigin()
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

    @PutMapping("/usuario/modificar")
    public Usuario modificarUsuario(@RequestBody Usuario usuario) {
        return this.usuarioService.modificar(usuario);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
       return usuarioService.loguearUsuario(usuario);
   }


    @PutMapping("/usuario/create")
    public Usuario crearUsuario(@RequestBody String body) throws JsonProcessingException {
        Usuario newUser = new ObjectMapper().readValue(body, Usuario.class);
        return usuarioService.registrarUsuario(newUser);
    }

    @PutMapping("/usuario/delete/{id}")
    public void bajaLogicaUsuario(@PathVariable Long id) {
       usuarioService.bajaLogica(id);
    }

}