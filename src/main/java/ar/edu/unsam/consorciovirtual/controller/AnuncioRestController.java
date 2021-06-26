package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import ar.edu.unsam.consorciovirtual.domain.AnuncioDTOParaListado;
import ar.edu.unsam.consorciovirtual.service.AnuncioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
public class AnuncioRestController {
    @Autowired
    private final AnuncioService anuncioService;

    @GetMapping("/anuncios")
    public List<AnuncioDTOParaListado> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return this.anuncioService.buscarTodos(palabraBuscada);
    }

    @GetMapping("/anuncios/vigentes")
    public List<AnuncioDTOParaListado> buscarTodosLosVigentes(@RequestParam(defaultValue="") String palabraBuscada) {
        return this.anuncioService.buscarTodosLosVigentes(palabraBuscada);
    }

    @GetMapping("/anuncios/{idAnuncio}")
    public Anuncio getAnuncioById(@PathVariable Long idAnuncio){ return anuncioService.getAnuncioById(idAnuncio);}

    @PutMapping("/anuncios/eliminar/{id}")
    public void bajaLogicaAnuncio(@PathVariable Long id) {
        anuncioService.bajaLogica(id);
    }

    //No se le pasa el autor desde el front, se le carga en el back por idAutor
    @PostMapping("/anuncios/crear/{idAutor}")
    public void createAnuncio(@PathVariable Long idAutor, @RequestBody Anuncio nuevoAnuncio) {
        anuncioService.crearAnuncio(idAutor, nuevoAnuncio);
    }

    /* No se le pasa el autor desde el front dado que no es necesario, solo utiliza el id del usuario
    que intenta la modificación para corroborar que es el mismo que lo creo, si no es el miemo tira
    una excepción */
    @PutMapping("/anuncios/modificar/{idUsuario}")
    public void modificarAnuncio(@PathVariable Long idUsuario, @RequestBody Anuncio anuncioActualizado) {
        anuncioService.modificarAnuncio(idUsuario, anuncioActualizado);
    }

}
