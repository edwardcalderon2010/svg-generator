package com.ec.svg.generator.app;

import com.ec.svg.generator.app.util.PathHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SvgGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SvgGeneratorApplication.class, args);
	}

	public SvgGeneratorApplication() {
		System.out.println("Starting");
		PathHelper.initLetter();
	}

}
