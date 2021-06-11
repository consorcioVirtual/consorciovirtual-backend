package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public List<Usuario> buscarTodos() {
        return this.usuarioService.buscarTodos();
    }

    @GetMapping("/usuario/{username}")
    public Usuario buscarPorUsername(@PathVariable String username) {
        return this.usuarioService.buscarPorUsername(username);
    }

}
