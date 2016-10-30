package com.leo;

import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

  public static void main(String[] args) {

    port(Integer.valueOf(System.getenv("PORT")));
    staticFileLocation("/public");

    get("/", (req, res) -> {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("message", "Vamos explorar sua conta do GitHub! =)");
        return new ModelAndView(attributes, "index.ftl");
	}, new FreeMarkerEngine());
    
  }

}
