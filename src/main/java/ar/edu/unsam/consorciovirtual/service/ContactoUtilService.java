package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.ContactoUtil;
import ar.edu.unsam.consorciovirtual.domain.TipoRegistro;
import ar.edu.unsam.consorciovirtual.repository.ContactoUtilRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ContactoUtilService {

    private final ContactoUtilRepository contactoUtilRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final RegistroModificacionService registroModificacionService;


    private Boolean esAdministrador(Long idLogueado){
        return usuarioRepository.esAdministrador(idLogueado).intValue() == 1;
    }

    public List<ContactoUtil> buscarTodos(String palabraBuscada) {
        return contactoUtilRepository.findAllByBajaLogicaFalseAndServicioContaining(palabraBuscada);
    }

    public void crearContacto(Long idLogueado, ContactoUtil nuevoContacto) {
        if(esAdministrador(idLogueado)){
            contactoUtilRepository.save(nuevoContacto);
        } else throw new SecurityException("No tiene permisos para crear contactos.");
    }

    public void modificarContacto(Long idLogueado, ContactoUtil contactoActualizado) {
        if(esAdministrador(idLogueado)){
            ContactoUtil contactoViejo = contactoUtilRepository.findById(contactoActualizado.getId()).orElseThrow(() -> new RuntimeException("Contacto no encontrado."));
            contactoViejo.setNombre(contactoActualizado.getNombre());
            contactoViejo.setTelefono(contactoActualizado.getTelefono());
            contactoViejo.setServicio(contactoActualizado.getServicio());
            contactoViejo.setAnotacion(contactoActualizado.getAnotacion());
            contactoUtilRepository.save(contactoViejo);
            registroModificacionService.guardarPorTipoYId(TipoRegistro.TELEFONOUTIL, contactoViejo.getId(), usuarioService.getNombreYApellidoById(idLogueado));
        } else throw new SecurityException("No tiene permisos para modidificar contactos.");
    }

    public void bajaLogica(Long idContacto, Long idLogueado) {
        ContactoUtil contacto = contactoUtilRepository.findById(idContacto).orElseThrow(() -> new RuntimeException("Contacto no encontrado."));
        if(esAdministrador(idLogueado)){
            contacto.setBajaLogica(true);
        } else throw new SecurityException("No tiene permisos para eliminar contactos.");
        contactoUtilRepository.save(contacto);
    }

    public void registrarTodos(List<ContactoUtil> contactos) {
        contactoUtilRepository.saveAll(contactos);
    }

    public ContactoUtil buscarPorId(Long idContacto) {
        return contactoUtilRepository.getById(idContacto);
    }
}
