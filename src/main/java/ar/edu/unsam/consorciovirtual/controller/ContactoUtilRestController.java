package ar.edu.unsam.consorciovirtual.controller;

import ar.edu.unsam.consorciovirtual.domain.Anuncio;
import ar.edu.unsam.consorciovirtual.domain.ContactoUtil;
import ar.edu.unsam.consorciovirtual.service.ContactoUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ContactoUtilRestController {

    private final ContactoUtilService contactoUtilService;

    //El filtro de busqueda, solo compara la palabraBuscada con ela tributo 'servicio'.
    @GetMapping("/contactosUtiles")
    public List<ContactoUtil> buscarTodos(@RequestParam(defaultValue="") String palabraBuscada) {
        return contactoUtilService.buscarTodos(palabraBuscada);
    }

    @PutMapping("/contactosUtiles/crear/{idLogueado}")
    public void createContacto(@PathVariable Long idLogueado, @RequestBody ContactoUtil nuevoContacto) {
        contactoUtilService.crearContacto(idLogueado, nuevoContacto);
    }

    @PutMapping("/contactosUtiles/modificar/{idLogueado}")
    public void modificarContaco(@PathVariable Long idLogueado, @RequestBody ContactoUtil contactoActualizado) {
        contactoUtilService.modificarContacto(idLogueado, contactoActualizado);
    }

    @PutMapping("/contactosUtiles/eliminar/{idContacto}/{idLogueado}")
    public void bajaLogicaContacto(@PathVariable Long idContacto, @PathVariable Long idLogueado) {
        contactoUtilService.bajaLogica(idContacto, idLogueado);
    }
}
