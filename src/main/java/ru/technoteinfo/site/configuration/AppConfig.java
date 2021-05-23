package ru.technoteinfo.site.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.technoteinfo.site")
@PropertySource("classpath:site.properties")
@PropertySource("classpath:resolutions.properties")
public class AppConfig {
}