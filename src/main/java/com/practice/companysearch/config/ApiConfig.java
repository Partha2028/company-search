package com.practice.companysearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tru-proxy-api")
public class ApiConfig {
    private String searchUrl;
    private String officersUrl;
}
