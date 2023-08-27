package com.google.cms;

import com.google.cms.utilities.Initconfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class CardsManagementSystemApplication {
	private final Initconfig initconfig;

	public CardsManagementSystemApplication(Initconfig initconfig) {
		this.initconfig = initconfig;
	}

	public static void main(String[] args) {
		SpringApplication.run(CardsManagementSystemApplication.class, args);
	}
	@Bean
	CommandLineRunner runner() {
		return args -> {
			log.info("---------------------System Init Checks");
			initconfig.initSuperRole();;
			initconfig.initUsers();
			log.info("---------------------System Init Completed");
		};
	}

}
