package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.DocumentoRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.unsam.consorciovirtual.domain.Constants.CARPETA_DE_ARCHIVOS;

@RequiredArgsConstructor
@Service
@Transactional
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final RegistroModificacionService registroModificacionService;


    public List<DocumentoDTOParaListado> mapearADTO(List<? extends Documento> documentos){
        return documentos.stream().map(documento-> DocumentoDTOParaListado.fromDocumento(documento)).collect(Collectors.toList());
    }

    public List<DocumentoDTOParaListado> buscarTodos(String palabraBuscada){
        List<? extends Documento> documentos = documentoRepository.findByBajaLogicaFalse(palabraBuscada);
        List<DocumentoDTOParaListado> documentosDTO = mapearADTO(documentos);
        documentosDTO.forEach(x -> x.setModificado(registroModificacionService.getUltimaModificacion(TipoRegistro.DOCUMENTO, x.getId())));
        return documentosDTO;
    }

    public Documento buscarPorId(Long id){
        return documentoRepository.findByIdAndBajaLogicaFalse(id).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
    }

    public DocumentoDTOParaABM buscarDocumentoParaABMPorId(Long id) {
        Documento documento = documentoRepository.findByIdAndBajaLogicaFalse(id).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        return DocumentoDTOParaABM.fromDocumento(documento);
    }

    public void dercargarDocumento(Long id, HttpServletResponse response) {
        Documento documento = this.buscarPorId(id);
        FileInputStream archivo;
        try{
            archivo = new FileInputStream(documento.getEnlaceDeDescarga());
            System.out.println(documento.getEnlaceDeDescarga());
            int longitud = archivo.available();
            byte[] datos = new byte[longitud];
            archivo.read(datos);
            archivo.close();
            response.setHeader("Content-Disposition","attachment;filename="+documento.getTitulo());
            String[] divisionPorPunto = documento.getEnlaceDeDescarga().split("\\.");
            response.setContentType(divisionPorPunto[divisionPorPunto.length-1]);
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(datos);
            ouputStream.flush();
            ouputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void registrarTodos(List<Factura> facturas) {
        documentoRepository.saveAll(facturas);
    }

    public void createDocumento(Long idAutor, Documento nuevoDocumento) {
        Usuario autor = usuarioRepository.buscarAdministradorPorId(idAutor).orElseThrow(() -> new IllegalArgumentException ("No tiene permiso para crear documentos"));
        if(nuevoDocumento.esValido() && (usuarioService.usuarioEsAdminDeLaApp(idAutor) || usuarioService.usuarioEsAdminDelConsorcio(idAutor))){
            nuevoDocumento.setAutor(autor);
            documentoRepository.save(nuevoDocumento);
        }else throw new IllegalArgumentException("Los datos ingresados no son válidos");
    }

    public void modificarDocumento(Long idUsuario, Documento documentoActualizado) {
        Documento documentoViejo = documentoRepository.findById(documentoActualizado.getId()).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        if(usuarioService.usuarioEsAdminDeLaApp(idUsuario) || documentoViejo.getAutor().getId() == idUsuario){
            documentoViejo.setDescripcion(documentoActualizado.getDescripcion());
            documentoViejo.setTitulo(documentoActualizado.getTitulo());
            documentoViejo.setEnlaceDeDescarga(documentoActualizado.getEnlaceDeDescarga());
            documentoViejo.setFechaModificacion(LocalDate.now());
        } else throw new IllegalArgumentException("No puede modificar un documento que usted no creo");

        documentoRepository.save(documentoViejo);
        registroModificacionService.guardarPorTipoYId(TipoRegistro.DOCUMENTO, documentoViejo.getId(), usuarioService.getNombreYApellidoById(idUsuario));
    }

    public void setBajaLogicaDocumento(Long idDocumento, Long idUsuario) {
        Documento documento = documentoRepository.findById(idDocumento).orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        if(usuarioService.usuarioEsAdminDeLaApp(idUsuario) || documento.getAutor().getId() == idUsuario){
            documento.setBajaLogica(true);
            documento.setFechaModificacion(LocalDate.now());
        } else throw new IllegalArgumentException("No puede eliminar un documento que usted no creo");

        documentoRepository.save(documento);
    }

    public Documento crearDocumentoEnBaseAPDFDelSistema(String nombreSimple, String nombreArchivo){
        Usuario administradorConsorcio = usuarioRepository.buscarAdministradorDeConsorcioActivo().orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Documento unDocumento = new Documento();
        unDocumento.setTitulo(nombreSimple);
        unDocumento.setDescripcion("Archivo PDF correpsondiente a: " + nombreSimple);
        unDocumento.setEnlaceDeDescarga(nombreArchivo);
        unDocumento.setAutor(administradorConsorcio);
        return unDocumento;
    }


}
