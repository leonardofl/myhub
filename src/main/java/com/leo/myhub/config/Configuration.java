package com.leo.myhub.config;

public class Configuration {

    public String get(ConfigVar configVar) {
        if (configVar == null) {
            throw new IllegalArgumentException();
        }
        String value = System.getenv(configVar.toString());
        if (value == null) {
        	throw new IllegalArgumentException("Config var " + configVar + " not found on app configuration.");
        }
        return value;
    }

}
