package com.delorenzo.Cinema.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="cinema")
public class ApplicationProperties {

    private int weeksToLive;

}
