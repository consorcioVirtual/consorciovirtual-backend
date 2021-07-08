package ar.edu.unsam.consorciovirtual.service;

import ar.edu.unsam.consorciovirtual.domain.Departamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.Properties;

@RequiredArgsConstructor
@Service
@Transactional
public class GestorDeCorreo {
    private final String user = "consorcioVirtualArgentina@gmail.com";
    private final String pass = "GrupoUno";

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

    public void enviarResumenDeExpensa(Departamento departamento, String archivo, YearMonth periodo) {

        Properties properties = configuracion();
        // Obtener la sesion
        Session session = Session.getInstance(properties, null);

        try {
            // Crear el cuerpo del mensaje
            MimeMessage mimeMessage = new MimeMessage(session);

            // Agregar quien envía el correo
            mimeMessage.setFrom(new InternetAddress(user, "Consorcio Virtual"));

            // Agregar los destinatarios al mensaje
            mimeMessage.setRecipients(Message.RecipientType.TO, departamento.getPropietario().getCorreo());
            if(departamento.getInquilino() != null){
                mimeMessage.setRecipients(Message.RecipientType.TO, departamento.getInquilino().getCorreo());
            }

            // Agregar el asunto al correo
            mimeMessage.setSubject("Resumen de expensas");

            // Creo la parte del mensaje
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText("Sr/Sra. Propietario/a o Inquilino/a:" +
                    "\nLe adjuntamos el resumen de expensas correspondiente a la unidad " +
                            departamento.getUnidad() + " del período " +periodo.toString() + "\nQue tenga buen día.");

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
}
