package ar.edu.unsam.consorciovirtual.utils;

import ar.edu.unsam.consorciovirtual.domain.*;
import ar.edu.unsam.consorciovirtual.repository.NotaRepository;
import ar.edu.unsam.consorciovirtual.repository.ReclamoRepository;
import ar.edu.unsam.consorciovirtual.repository.SolicitudTecnicaRepository;
import ar.edu.unsam.consorciovirtual.repository.UsuarioRepository;
import ar.edu.unsam.consorciovirtual.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RequiredArgsConstructor
@Service
@Transactional
public class GestorDeCorreo {
    private final String user = "consorcioVirtualArgentina@gmail.com";
    private final String pass = "GrupoUno";
    private final ReclamoRepository reclamoRepository;
    private final SolicitudTecnicaRepository solicitudRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    private Properties configuracion(){
        // La configuración para enviar correo
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", user);
        properties.put("mail.password", pass);
        return properties;
    }

    private void enviar(MimeMessage mimeMessage, Session session){
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect(user, pass);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enviarReciboDeExpensas(ExpensaDeDepartamento expensa, String archivo){
        String asunto = "Recibo de expensas";
        String texto = "Sr/Sra. Propietario/a o Inquilino/a:" +
                "\n\nLe adjuntamos el recibo de pago por las expensas del período " + expensa.getPeriodo().toString() +
                " correspondiente a la unidad " + expensa.getDepartamento().getUnidad() +"."+
                "\n\nRecuerde que, si usted abonó por mercado pago, el presente recibo solo es válido junto con" +
                " el ticket de pago. Por otra parte, si el pago fue en efectivo, debe solicitarle al administrador de" +
                " su edificio que imprima este recibo y se lo entregue firmado." +
                "\n\nQue tenga buen día.\nConsorcio Virtual";

        enviarArchivoRelacionadoAExpensa(expensa.getDepartamento(), archivo, asunto, texto);
    }

    public void enviarResumenDeExpensa(Departamento departamento, String archivo, YearMonth periodo) {
        String asunto = "Resumen de expensas";
        String texto ="Sr/Sra. Propietario/a o Inquilino/a:" +
                "\n\nLe adjuntamos el resumen de expensas correspondiente a la unidad " +
                departamento.getUnidad() + " del período " +periodo.toString() + "." +
                "\n\nQue tenga buen día.\nConsorcio Virtual";
        enviarArchivoRelacionadoAExpensa(departamento, archivo, asunto, texto);
    }

    private void enviarArchivoRelacionadoAExpensa(Departamento departamento, String archivo, String asunto, String texto) {

        Properties properties = configuracion();
        // Obtener la sesion
        Session session = Session.getInstance(properties, null);

        try {
            // Crear el cuerpo del mensaje
            MimeMessage mimeMessage = new MimeMessage(session);

            // Agregar quien envía el correo
            mimeMessage.setFrom(new InternetAddress(user, "Consorcio Virtual"));

            // Agregar los destinatarios al mensaje
            mimeMessage.addRecipients(Message.RecipientType.TO, departamento.getPropietario().getCorreo());
            if(departamento.getInquilino() != null){
                mimeMessage.addRecipients(Message.RecipientType.TO, departamento.getInquilino().getCorreo());
            }

            // Agregar el asunto al correo
            mimeMessage.setSubject(asunto);

            // Creo la parte del mensaje
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(texto);

            // Configurar el archivo adjunto
            MimeBodyPart mimeBodyPartAdjunto = new MimeBodyPart();
            mimeBodyPartAdjunto.attachFile(archivo);

            // Crear el multipart para agregar la parte del mensaje anterior y el adjunto
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            multipart.addBodyPart(mimeBodyPartAdjunto);

            // Agregar el multipart al cuerpo del mensaje
            mimeMessage.setContent(multipart);

            // Enviar el mensaje
            enviar(mimeMessage, session);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Correo enviado");
    }

    public void enviarMensajeNuevoUsuario(Usuario usuario){
        String asunto = "Usted fue registrado en Consorcio Virtual, la mejor solución para consorcios de edificios";
        String cuerpo = "Sr/Sra. "+ usuario.getNombre() + " " + usuario.getApellido() +":" +
                "\n\nLe informamos que fue registrado en la aplicación Consorcio Virtual como " +
                usuario.getTipo() +", " +
                " puede conectarse a la misma ingresando a www.consorciovirtual.com.ar" +
                " utilizando los siguientes datos: \n" +
                "\nUsuario:"+ " " + usuario.getCorreo() +
                "\nContraseña: su número de DNI (sin puntos)" +
                "\n\nPara su seguridad, le sugerimos que cambie su contraseña la primera vez" +
                " que ingrese a la aplicación.\nQue tenga buen día."+
                "\n\nP.D.: Si usted tiene un problema para ingresar o cree que no debió ser agregado " +
                "a esta aplicación, por favor notifíquelo a consorcioVirtualArgentina@gmail.com";
        List<String> remitentes = new ArrayList<String>();
        remitentes.add(usuario.getCorreo());

        enviarMensajeSinAdjunto(asunto, cuerpo, remitentes);
    }

    public void enviarMensajeNuevaNota(Long idContenedorNota, Long idUsuario, String tipo) {
        try {
            Usuario usuario = null;
            List<String> remitentes = new ArrayList<String>();
            String nombreContendor = "";
            String pestania = "";
            String numero = idContenedorNota.toString();

            while (numero.length() < 5) {
                numero = "0" + numero;
            }

            if (tipo == "Reclamo") {
                Reclamo reclamo = reclamoRepository.getById(idContenedorNota);
                usuario = reclamo.getAutor();
                nombreContendor = "el reclamo";
                pestania = "Reclamos";
            }
            if (tipo == "Solicitud") {
                SolicitudTecnica solicitud = solicitudRepository.getById(idContenedorNota);
                usuario = solicitud.getAutor();
                nombreContendor = "la solicitud";
                pestania = "Solicitudes Técnicas";
            }

            if (usuarioService.usuarioEsAdminDelConsorcio(idUsuario)) {
                remitentes.add(usuario.getCorreo());
                if (usuario.getTipo() == TipoUsuario.Inquilino) {
                    String correoPropietario = usuarioService.buscarCorreoDePropietarioPorInquilino(idUsuario);
                    remitentes.add(correoPropietario);
                }
            } else {
                Usuario admin = usuarioRepository.buscarAdministradorDeConsorcioActivo().orElseThrow(() -> new RuntimeException("No se encontró administrador"));
                String correoAdministrador = admin.getCorreo();
                remitentes.add(correoAdministrador);
            }

            String asunto = "Hay una nueva nota en " + nombreContendor + " nº" + numero;
            String cuerpo = "Sr/Sra. Propietario/a / Inquilino/a / Administrador/a:" +
                    "\n\nLe informamos que hay una nueva nota en " + nombreContendor + " nº" + numero +
                    ". Puede verla ingresando a la web de consorcio virtual en la pestaña " + pestania + "." +
                    "\nQue tenga buen día." +
                    "\n\nP.D.: Si usted no forma parte de la apliación web Consorcio Virtual " +
                    "por favor notifíquelo a consorcioVirtualArgentina@gmail.com";

            enviarMensajeSinAdjunto(asunto, cuerpo, remitentes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarMensajeNuevoAnuncio(String titulo, Long idUsuario) {
        try{
            String asunto = "Se creó un nuevo anuncio en la cartelera virtual de tu consorcio";
            String cuerpo = "Sr/Sra. Propietario/a / Inquilino/a / Administrador/a:" +
                    "\n\nLe informamos que se creo el anuncio "+ titulo +", en la cartelera virtual de su consorcio"+
                    ". Puede ver más detalles ingresando a la web de consorcio virtual en la pestaña de Anuncios." +
                    "\nQue tenga buen día." +
                    "\n\nP.D.: Si usted no forma parte de la apliación web Consorcio Virtual " +
                    "por favor notifíquelo a consorcioVirtualArgentina@gmail.com";

            List<String> remitentes= usuarioRepository.buscarTodosLosCorreosMenosUno(idUsuario);
            enviarMensajeSinAdjunto(asunto, cuerpo, remitentes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarMensajeSinAdjunto(String asunto, String cuerpo, List<String> remitentes){
        Properties properties = configuracion();
        // Obtener la sesion
        Session session = Session.getInstance(properties, null);

        try {
            // Crear el cuerpo del mensaje
            MimeMessage mimeMessage = new MimeMessage(session);

            // Agregar quien envía el correo
            mimeMessage.setFrom(new InternetAddress(user, "Consorcio Virtual"));

            // Agregar los destinatarios al mensaje
            remitentes.forEach(
                    x-> {
                        try {
                            mimeMessage.addRecipients(Message.RecipientType.TO, x);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
            );

            // Agregar el asunto al correo
            mimeMessage.setSubject(asunto);

            // Creo la parte del mensaje
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(cuerpo);

            // Crear el multipart para agregar la parte del mensaje anterior y el adjunto
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            // Agregar el multipart al cuerpo del mensaje
            mimeMessage.setContent(multipart);

            // Enviar el mensaje
            enviar(mimeMessage, session);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Correo enviado");
    }


}
