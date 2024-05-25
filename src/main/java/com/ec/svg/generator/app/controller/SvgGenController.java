package com.ec.svg.generator.app.controller;

import com.ec.svg.generator.app.dto.SvgResponse;
import com.ec.svg.generator.app.engine.Generator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="svg_gen")
public class SvgGenController {

    private Generator generator;

    public SvgGenController(Generator generator) {
        this.generator = generator;
    }
    @GetMapping(path="/hello")
    public @ResponseBody String hello() {
        return "hello!";
    }

    @CrossOrigin(origins = "http://localhost")
    @GetMapping(path="generate/{inputText}")
    public @ResponseBody String generate(@PathVariable("inputText") String inputString) {
        String result = "";
        if (generator != null) {
            result = generator.generateSVGOutput(inputString);
        }
        return result;
    }

    @GetMapping(path="embed/{inputText}")
    public @ResponseBody String generateEmbedded(@PathVariable("inputText") String inputString) {
        String result = "";
        if (generator != null) {
            result = generator.generateSVGEmbedded(inputString);
        }
        return result;
    }

    @GetMapping(path="generateAsJSON/{inputText}")
    public @ResponseBody SvgResponse generateAsJSON(@PathVariable("inputText") String inputString) {
        String svgPayload = "";
        if (generator != null ) {
            svgPayload = generator.generateSVGOutput(inputString);
        }
        return new SvgResponse("generateAsJSON_id",svgPayload);
    }

}
