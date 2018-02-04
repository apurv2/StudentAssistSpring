package com.studentAssist;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ApplicationApi implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationApi.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		XTrustProvider.install();

	}
}
