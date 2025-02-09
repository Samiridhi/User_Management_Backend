package org.example.userapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external.api")
public class ExternalApiConfig {

    private String dataUrl;

    // Getters and setters
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String baseUrl) {
        this.dataUrl = baseUrl;
    }

}
