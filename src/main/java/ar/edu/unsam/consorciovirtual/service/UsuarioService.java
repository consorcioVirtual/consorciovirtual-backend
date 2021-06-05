package ar.edu.unsam.consorciovirtual.service;

import javax.transaction.Transactional;

import ar.edu.unsam.consorciovirtual.domain.Usuario;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario registrar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

}
