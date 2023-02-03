package com.trogiare.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class GcsConfig {

    @Bean
    public Storage storage() throws Exception {
        String credentialsFilePath = "credentials.json";
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(getFileFromResources(credentialsFilePath));
        System.out.println("x");
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
    public static InputStream getFileFromResources(String fileName) {
        ClassLoader classLoader = GcsConfig.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}
