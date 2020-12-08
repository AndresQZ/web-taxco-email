package app.taxco.email.services;

import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender sender;
	
	private  static Logger logger = LogManager.getLogger(EmailService.class);
	
	public boolean sendEmail(String email, String userName, double amount, List<String> items ) {
	 Boolean submittedSuccessfully = false;
	 logger.info("email -> " + email);
	 logger.info("amount -> " + amount);
	 String message = items.stream().map(item -> item).reduce("", (cadena, item) -> (cadena + "<br>" + item)).toString();
	 message = "Hola " + userName + "<br> Compra realizada, te compartimos el detalle. <br>" + message + "<br> Total : $" + amount;
	 logger.info("message to sendEmail ->  " +  message);
	 submittedSuccessfully = sendEmailTool(email, message, "Compra realizada");
	 return submittedSuccessfully;
		
	}
	
	
	private boolean sendEmailTool(String email , String textMessage,String subject) {
		boolean send = false;
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		logger.info("executing 'sendEmailTool()' method -> " + email);
		try {
			helper.setTo(email);
			helper.setText(textMessage, true);
			helper.setSubject(subject);
			sender.send(message);
			send = true;
			logger.info("Mail enviado!");
		} catch (MessagingException e) {
			logger.error("Hubo un error al enviar el mail: {}", e);
		}
		return send;
	}
	
	
	 
	

}
