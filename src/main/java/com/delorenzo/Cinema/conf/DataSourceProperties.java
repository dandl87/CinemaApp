package com.delorenzo.Cinema.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("data")
public record DataSourceProperties(List<DataSource> source) {
}
