package pl.reportsController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;

@SpringBootApplication
public class ReportsControllerApplication implements WebMvcConfigurer {

	private HashMap<Long, String> users_tokens = new HashMap<>();
	public static void main(String[] args) {
		SpringApplication.run(ReportsControllerApplication.class, args);
	}
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedOrigins("http://localhost:3000")
				.allowedHeaders("*")
				.allowCredentials(true);
	}


	public void addUserKeyToMap(Long id){

	}
}

