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
	 
	 String message2 = message +  "<br> <br> <br> <br> <br>" + "<table style=3D\"width:80.0%;border-collapse:collapse\" width=3D\"80%\" cellspa=\r\n"
	 		+ "cing=3D\"0\" cellpadding=3D\"0\" border=3D\"0\">\r\n"
	 		+ " <tbody><tr>\r\n"
	 		+ "  <td style=3D\"padding:0cm 0cm 0cm 0cm\">\r\n"
	 		+ "  <p class=3D\"MsoNormal\" style=3D\"text-align:center\" align=3D\"center\"><b><s=\r\n"
	 		+ "pan style=3D\"font-size:15.0pt;font-family:&quot;Arial&quot;,sans-serif;colo=\r\n"
	 		+ "r:black\">AVISO DE PRIVACIDAD</span></b></p>\r\n"
	 		+ "  </td>\r\n"
	 		+ " </tr>\r\n"
	 		+ " <tr>\r\n"
	 		+ "  <td style=3D\"padding:0cm 0cm 0cm 0cm\">\r\n"
	 		+ "  <p class=3D\"MsoNormal\"><span style=3D\"font-size:10.0pt;font-family:&quot;=\r\n"
	 		+ "Arial&quot;,sans-serif;color:black\">=C2=A0</span></p>\r\n"
	 		+ "  </td>\r\n"
	 		+ " </tr>\r\n"
	 		+ "</tbody></table>\r\n"
	 		+ "\r\n"
	 		+ "</div>\r\n"
	 		+ "\r\n"
	 		+ "<p class=3D\"MsoNormal\"><span style=3D\"display:none\">=C2=A0</span></p>\r\n"
	 		+ "\r\n"
	 		+ "<table style=3D\"width:100.0%;border-collapse:collapse\" width=3D\"100%\" cells=\r\n"
	 		+ "pacing=3D\"0\" cellpadding=3D\"0\" border=3D\"0\">\r\n"
	 		+ " <tbody><tr>\r\n"
	 		+ "  <td style=3D\"border:inset 1.0pt;padding:0cm 0cm 0cm 0cm\">\r\n"
	 		+ "  <p class=3D\"MsoNormal\"><span style=3D\"font-size:10.0pt;font-family:&quot;=\r\n"
	 		+ "Arial&quot;,sans-serif;color:black\">MQ JOYERIAS, con\r\n"
	 		+ "  domicilio en calle Emiliano Zapata #6, colonia Acapantzingo, municipio de=\r\n"
	 		+ " Cuernavaca,\r\n"
	 		+ "  C.P. 62440, en la entidad de Morelos, M=C3=A9xico, utilizar=C3=A1 sus dat=\r\n"
	 		+ "os personales\r\n"
	 		+ "  recabados para: </span></p>\r\n"
	 		+ "  <ul type=3D\"disc\">\r\n"
	 		+ "   <li class=3D\"MsoNormal\" style=3D\"color:black\"><span style=3D\"font-size:1=\r\n"
	 		+ "0.0pt;font-family:&quot;Arial&quot;,sans-serif\">Dar cumplimento al env=C3=\r\n"
	 		+ "=ADo de productos seleccionados\r\n"
	 		+ "       por nuestros clientes en la direcci=C3=B3n proporcionada.</span></li=\r\n"
	 		+ ">\r\n"
	 		+ "  </ul>\r\n"
	 		+ "  <p class=3D\"MsoNormal\" style=3D\"margin-bottom:12.0pt\"><span style=3D\"font=\r\n"
	 		+ "-size:10.0pt;font-family:&quot;Arial&quot;,sans-serif;color:black\">Para may=\r\n"
	 		+ "or informaci=C3=B3n acerca del tratamiento y de los derechos\r\n"
	 		+ "  que puede hacer valer, usted puede acceder al aviso de privacidad integra=\r\n"
	 		+ "l a\r\n"
	 		+ "  trav=C3=A9s de: <a href=3D\"http://187.227.238.91:4500\" target=3D\"_blank\">=\r\n"
	 		+ "http://187.227.238.91:4500</a></span></p>\r\n"
	 		+ "  </td>\r\n"
	 		+ " </tr>\r\n"
	 		+ "</tbody></table></div></span><span><div dir=3D\"ltr\" style=3D\"margin-left:0p=\r\n"
	 		+ "t\" align=3D\"left\"><span><div dir=3D\"ltr\" style=3D\"margin-left:0pt\" align=3D=\r\n"
	 		+ "\"left\"></div></span></div></span><span><div dir=3D\"ltr\" style=3D\"margin-lef=\r\n"
	 		+ "t:0pt\" align=3D\"left\"></div></span></div></div></div></div>";
	 logger.info("message to sendEmail ->  " +  message2);
	 submittedSuccessfully = sendEmailTool(email, message2, "Compra realizada");
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
