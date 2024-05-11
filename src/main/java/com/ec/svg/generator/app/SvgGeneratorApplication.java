package com.ec.svg.generator.app;

import com.ec.svg.generator.app.engine.Generator;
import com.ec.svg.generator.app.util.PathHelper;
import com.ec.svg.generator.app.util.SVGLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SvgGeneratorApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(SvgGeneratorApplication.class, args);
	}

	public SvgGeneratorApplication(ConfigurableApplicationContext ctx) {

		System.out.println("###### Starting");

		SVGLoader loader = ctx.getBean(SVGLoader.class);
		loader.load();
		Generator generator = ctx.getBean(Generator.class);
		String svgText = "fantastic!";
		generator.generateSVGFromString(svgText);

//		PathHelper.initLetter();
	}


}
