package app.taxco.email.services;

import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender sender;
	
	@Value("${web.taxco.page.url}")
	private String pageUrl;
	
	@Value("${web.taxco.gateway.port}")
	private String gateway_url;
	
	private  static Logger logger = LogManager.getLogger(EmailService.class);
	
	public boolean sendEmail(String email, String userName, double amount, List<String> items, String folio) {
	 Boolean submittedSuccessfully = false;
	 logger.info("email -> " + email);
	 logger.info("amount -> " + amount);
	 String message = items.stream().map(item -> item).reduce("", (cadena, item) -> (cadena + "<br>" + item)).toString();
	 message = "Hola " + userName + "<br> Compra realizada, te compartimos el detalle. <br>" +
	 "folio: " + folio + "<br>" + message + "<br> Total : $" + amount;
	 
	 String messageWithPrivacity = message +  "<br> <br>" + "<h4 style=\"font-size:medium; text-align: center;\">AVISO DE PRIVACIDAD</h4> " 
	 +  "<div style=\"border: 3px solid rgb(99, 96, 96);padding: 10px; margin: 20px;\">  MQ JOYERIAS, con domicilio en calle Emiliano Zapata #6, colonia Acapantzingo, municipio de Cuernavaca, C.P. 62440,"
	 + " en la entidad de  Morelos, M\u00E9xico, utilizar\u00E1 sus datos personales recabados para: "
	 + "<ul>"
	 +   "<li>"
	 +    "Dar cumplimento al env\u00EDo de productos seleccionados por nuestros clientes en la direcci\u00F3n proporcionada."
	 +   "</li>"
	 +   "</ul>"
	 +  "Para mayor informaci\u00F3n acerca del tratamiento y de los derechos que puede hacer valer, usted puede acceder al aviso de privacidad integral a trav\u00E9s de: http://187.227.238.91:4500 "
	 + "</div>";
	
	 
	 logger.info("message to sendEmail -> messageWithPrivacity :   " + messageWithPrivacity);
	 submittedSuccessfully = sendEmailTool(email, messageWithPrivacity, "Compra realizada");
	 return submittedSuccessfully;
		
	}
	
	
	private boolean sendEmailTool(String email , String textMessage,String subject) {
		boolean send = false;
		try {
			
		  MimeMessage message = sender.createMimeMessage();
		  MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		  logger.info("executing 'sendEmailTool()' method ");
		  logger.info("email to send -> " + email);
		  logger.info("message to send -> " + textMessage);
		
			ClassPathResource logo = new ClassPathResource("static/logo.jpg");
			helper.setTo(email);
			helper.setText(textMessage, true);
			helper.addInline("signature", logo);
			//elper.ad(logo, logo);
			helper.setSubject(subject);
			sender.send(message);
			send = true;
			logger.info("Mail enviado!");
		} catch (MessagingException e) {
			logger.error("Hubo un error al enviar el mail: {}", e.getMessage());
		}
		return send;
	}
	
	
	public Boolean sendResetPassword(String data, String email) {
		 Boolean submittedSuccessfully = false;
		 logger.info("executing 'sendResetPassword' method from EmailService");
		 String message = "Para restablecer su contraseña ingrese a este <span><a href=\"http://"+pageUrl+":"+ gateway_url +"/#/restablecer?data="+data+"\">link</a></span>";
		 logger.info("message -> " + message);
		 submittedSuccessfully =  this.sendEmailTool(email, message, "Restablecer Contraseña");
		 return submittedSuccessfully;
	}
	
	
	 
	

}
