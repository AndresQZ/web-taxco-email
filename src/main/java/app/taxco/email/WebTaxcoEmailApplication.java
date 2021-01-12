package app.taxco.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class WebTaxcoEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebTaxcoEmailApplication.class, args);
	}

}
