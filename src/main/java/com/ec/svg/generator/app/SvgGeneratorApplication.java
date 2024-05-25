package com.ec.svg.generator.app;

import com.ec.svg.generator.app.engine.Generator;
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
//		Generator generator = ctx.getBean(Generator.class);
//		String svgText = "";
//		svgText = "Essence of the perfection of wisdom, the Blessed Mother. Homage to the perfection of wisdom, the Blessed Mother. " +
//		"Thus I have heard. At one time the Blessed One was dwelling in Rajagriha on Massed Vultures Mountain together with a great assembly of monks and nuns and a great assembly of Bodhisattvas. " +
//		"At that time the Blessed One was absorbed in the concentration of the countless aspects of phenomena called, Profound Illumination.";
//		svgText = "Go placidly amid the noise and the haste, and remember what peace there may be in silence. As far as possible, without surrender, be on good terms with all persons.";
//		svgText = "Congratulations Ben & Iryna!";
//		svgText = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
//		svgText = "Essence of the perfection of wisdom, the Blessed Mother. Homage to the perfection of wisdom, the Blessed Mother. Thus I have heard." +
//		"At one time the Blessed One was dwelling in Rajagriha on Massed Vultures Mountain together with a great assembly of monks and nuns and a great assembly of Bodhisattvas.";
//		generator.generateSVGFromString(svgText);

		//testClonePathTag();

	}


}
