package com.ec.svg.generator.app;

import com.ec.svg.generator.app.engine.Generator;
import com.ec.svg.generator.app.util.SVGLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static com.ec.svg.generator.app.util.PathHelper.testClonePathTag;

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
		String svgText = "essence of the perfection of wisdom the blessed mother homage to the perfection of wisdom the blessed mother thus i have heard";
		generator.generateSVGFromString(svgText);

		//testClonePathTag();

	}


}
