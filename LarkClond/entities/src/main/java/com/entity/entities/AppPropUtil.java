package com.entity.entities;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class AppPropUtil {

    private static final String UNDEFINED = "";
    private static final String APPLICATION_PROPERTIES = "application.properties";
    private static Properties properties;
    static {
        try {
            ClassPathResource resource = new ClassPathResource(APPLICATION_PROPERTIES);
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return properties.getProperty(key, UNDEFINED);
    }

}
