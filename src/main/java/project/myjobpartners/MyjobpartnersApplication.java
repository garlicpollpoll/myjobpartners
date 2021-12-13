package project.myjobpartners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MyjobpartnersApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location=" +
			"classpath:application.yml";

//	public static void main(String[] args) {
//		SpringApplication.run(MyjobpartnersApplication.class, args);
//	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(MyjobpartnersApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
	}

}
