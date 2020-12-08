package app.taxco.email.rest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.taxco.email.dto.GenericResponse;
import app.taxco.email.dto.SendEmailOrder;
import app.taxco.email.services.EmailService;

@RestController
@RequestMapping(value="/email/v0/")
public class Email {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private EmailService emailService;
	
	private  static Logger logger = LogManager.getLogger(Email.class);
	
	@PostMapping(value="/send", produces="application/json")
	public ResponseEntity<Object> sendEmail(@RequestBody SendEmailOrder email) {
		GenericResponse reponse = new GenericResponse();
		logger.info("executing 'sendEmail()' method from Email");
		 Boolean submittedSuccessfully = false;
		try {
			logger.info("request -> " + mapper.writeValueAsString(email));
			submittedSuccessfully = emailService.sendEmail(email.getEmail(),email.getUserName() ,email.getAmount(),email.getItems());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (submittedSuccessfully) {
			reponse.setMessage("Correo enviado correctamente");
			return new ResponseEntity<Object>(reponse,HttpStatus.OK);
		} else {
			reponse.setMessage("Error al enviar a dispositivos");
			return new ResponseEntity<Object>(reponse,HttpStatus.CONFLICT);
		}	
	}

}
