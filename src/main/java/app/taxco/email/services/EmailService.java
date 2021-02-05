package app.taxco.email.services;

import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender sender;
	
	private  static Logger logger = LogManager.getLogger(EmailService.class);
	
	public boolean sendEmail(String email, String userName, double amount, List<String> items, String folio) {
	 Boolean submittedSuccessfully = false;
	 logger.info("email -> " + email);
	 logger.info("amount -> " + amount);
	 String message = items.stream().map(item -> item).reduce("", (cadena, item) -> (cadena + "<br>" + item)).toString();
	 message = "Hola " + userName + "<br> Compra realizada, te compartimos el detalle. <br>" +
	 "folio: " + folio + "<br>" + message + "<br> Total : $" + amount;
	 logger.info("message to sendEmail ->  " +  message);
	 submittedSuccessfully = sendEmailTool(email, message, "Compra realizada");
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
		
			ClassPathResource image = new ClassPathResource("static/signature.jpg");
			helper.setTo(email);
			helper.setText(textMessage, true);
			helper.addInline("signature", image);
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
		 String message = "Para restablecer su contraseña ingrese a este <span><a href=\"http://187.227.238.91:4500/#/restablecer?data="+data+"\">link</a></span>";
		 logger.info("message -> " + message);
		 submittedSuccessfully =  this.sendEmailTool(email, message, "Restablecer Contraseña");
		 return submittedSuccessfully;
	}
	
	
	 
	

}
