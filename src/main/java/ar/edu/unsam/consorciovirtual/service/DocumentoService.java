package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.businessExceptions.DataConsistencyException;
import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.domainDTO.DocumentoDTOParaABM;
import ar.edu.unsam.consorciovirtual.domainDTO.DocumentoDTOParaListado;
import ar.edu.unsam.consorciovirtual.repository.DocumentoRepository;
import ar.edu.unsam.consorciovirtual.repository.FacturaRepository;
import ar.edu.unsam.consorciovirtual.repository.GastoRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import ar.edu.unsam.consorciovirtual.utils.ValidationMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final FacturaRepository facturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final RegistroModificacionService registroModificacionService;
    private final GastoRepository gastoRepository;


    public List<DocumentoDTOParaListado> mapearADTO(List<? extends Documento> documentos){
        return documentos.stream().map(DocumentoDTOParaListado::fromDocumento).collect(Collectors.toList());
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

    public void createDocumento(Long idAutor, Documento nuevoDocumento) throws DataConsistencyException {
        Usuario autor = usuarioRepository.buscarAdministradorPorId(idAutor).orElseThrow(() -> new IllegalArgumentException ("No tiene permiso para crear documentos"));
        nuevoDocumento.setAutor(autor);
        validarDocumento(nuevoDocumento);
        if(usuarioService.usuarioEsAdminDeLaApp(idAutor) || usuarioService.usuarioEsAdminDelConsorcio(idAutor)){
            documentoRepository.save(nuevoDocumento);
        }else throw new SecurityException("No tiene permisos para crear un nuevo documento");
    }

    public void createDocumentoDeGasto(Long idAutor, Documento nuevoDocumento) throws DataConsistencyException {
        Usuario autor = usuarioRepository.buscarAdministradorPorId(idAutor).orElseThrow(() -> new IllegalArgumentException ("No tiene permiso para crear documentos"));
        Gasto gasto = gastoRepository.findGastoByUrl(nuevoDocumento.getEnlaceDeDescarga());
        nuevoDocumento.setTitulo(gasto.getTitulo());
        nuevoDocumento.setDescripcion("Comprobante relacionado al gasto que se menciona en el título, " +
                "para más información buscar en pestaña de gastos con el mismo nombre");
        nuevoDocumento.setAutor(autor);
        validarDocumento(nuevoDocumento);
        if(usuarioService.usuarioEsAdminDeLaApp(idAutor) || usuarioService.usuarioEsAdminDelConsorcio(idAutor)){
            documentoRepository.save(nuevoDocumento);
            Documento elDocumento = documentoRepository.findByEnlaceDeDescarga(nuevoDocumento.getEnlaceDeDescarga()).orElseThrow(() -> new IllegalArgumentException ("Error al asociar comprobante y gasto"));
            gasto.setComprobante(elDocumento);
            gastoRepository.save(gasto);
        }else throw new IllegalArgumentException("No tiene permisos para crear un nuevo documento");
    }

    public void modificarDocumento(Long idUsuario, Documento documentoActualizado) throws DataConsistencyException {
        Documento documentoViejo = documentoRepository.findById(documentoActualizado.getId()).orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        if(usuarioService.usuarioEsAdminDeLaApp(idUsuario) || documentoViejo.getAutor().getId() == idUsuario){
            documentoViejo.setDescripcion(documentoActualizado.getDescripcion());
            documentoViejo.setTitulo(documentoActualizado.getTitulo());
            documentoViejo.setEnlaceDeDescarga(documentoActualizado.getEnlaceDeDescarga());
            documentoViejo.setFechaModificacion(LocalDate.now());
        } else throw new SecurityException("No puede modificar un documento que usted no ha creado");
        validarDocumento(documentoViejo);
        documentoRepository.save(documentoViejo);
        registroModificacionService.guardarPorTipoYId(TipoRegistro.DOCUMENTO, documentoViejo.getId(), usuarioService.getNombreYApellidoById(idUsuario));
    }

    public void modificarFacturaDeGasto(Long idFactura, Long idUsuario, Factura nuevaFactura) {
        Factura facturaVieja = facturaRepository.findById(idFactura).orElseThrow(() -> new RuntimeException("Factura no encontrado"));
        if(usuarioService.usuarioEsAdminDeLaApp(idUsuario) || facturaVieja.getAutor().getId() == idUsuario){
            facturaVieja.setFechaFactura(nuevaFactura.getFechaFactura());
            facturaVieja.setTipoFactura(nuevaFactura.getTipoFactura());
            facturaVieja.setPuntoDeVenta(nuevaFactura.getPuntoDeVenta());
            facturaVieja.setNumeroFactura(nuevaFactura.getNumeroFactura());
            facturaVieja.setCuitReceptor(nuevaFactura.getCuitReceptor());
            facturaVieja.setCuitProveedor(nuevaFactura.getCuitProveedor());
            facturaVieja.setCae(nuevaFactura.getCae());
            facturaVieja.setImporte(nuevaFactura.getImporte());
        } else throw new SecurityException("No puede modificar un documento que usted no ha creado");
        if(facturaVieja.esValido()){
            documentoRepository.save(facturaVieja);
        }else throw new SecurityException("Los cambios no son válidos");

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

    private void validarDocumento(Documento documento) throws DataConsistencyException {

        System.out.println(documento.getAutor());

        if(
           ValidationMethods.stringNullOVacio(documento.getTitulo()) ||
           ValidationMethods.stringNullOVacio(documento.getDescripcion()) ||
           ValidationMethods.stringNullOVacio(documento.getEnlaceDeDescarga()) ||
           ValidationMethods.datoNull(documento.getFechaCreacion()) ||
           ValidationMethods.datoNull(documento.getFechaModificacion()) ||
           ValidationMethods.datoNull(documento.getAutor())
        ) throw new DataConsistencyException("Ha ocurrido un error con los datos ingresados. Verificalos e intentá de nuevo.");
    }

    public FacturaDTOParaGasto buscarFacturaPorId(Long id) {
        Factura factura = facturaRepository.findByIdAndBajaLogicaFalse(id).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        return FacturaDTOParaGasto.fromFactura(factura);
    }


}
