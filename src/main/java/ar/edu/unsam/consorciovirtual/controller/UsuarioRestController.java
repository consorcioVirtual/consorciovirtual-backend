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

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class UsuarioRestController {

    @Autowired
    private final UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public List<Usuario> buscarTodos() {
        return this.usuarioService.buscarTodos();
    }

    @GetMapping("/usuario/{username}")
    public Usuario buscarPorUsername(@PathVariable String username) {
        return this.usuarioService.buscarPorUsername(username);
    }


    //TODO: Ver como sacar el string del json
//    @PostMapping("/login/{username}")
//    public Usuario login(@RequestBody String _password, @PathVariable String username) throws JsonProcessingException {
//        String password = new ObjectMapper().readValue(_password, String.class);
//        System.out.println(_password);
//        return usuarioService.loguearUsuario(username, _password);
//    }

    //TODO: No está probado, pero va por ahí 
//    @PutMapping("/usuario/create")
//    public void crearUsuario(@RequestBody String body) throws JsonProcessingException {
//        Usuario newUser = new ObjectMapper().readValue(body, Usuario.class);
//        usuarioService.registrar(newUser);
//    }



}