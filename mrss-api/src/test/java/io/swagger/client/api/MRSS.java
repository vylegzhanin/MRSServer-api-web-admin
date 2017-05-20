package io.swagger.client.api;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.model.InlineResponse200;
import io.swagger.client.model.LoginRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/21/2017.
 */
class MRSS {

    private static String userName;
    private static String password;

    static {
        final Properties configuration = new Properties();
        final Path path = Paths.get("/MRSS", "configuration.properties");
        if (Files.isRegularFile(path))
            try {
                try (InputStream inputStream = Files.newInputStream(path)) {
                    configuration.load(inputStream);
                }
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        final String basePath = configuration.getProperty("basePath", "http://localhost:12800");
        userName = configuration.getProperty("userName", "admin");
        password = configuration.getProperty("password", "admin");
        Configuration.getDefaultApiClient().setBasePath(basePath);
    }

    static ApiClient setupAuthenticationAPIsApi() {
        final LoginRequest loginRequest = new LoginRequest()
                .username(userName)
                .password(password);
        try {
            InlineResponse200 response = new AuthenticationAPIsApi().login(loginRequest);
            final ApiClient apiClient = Configuration.getDefaultApiClient();
            apiClient.addDefaultHeader("Authorization", "Bearer " + response.getAccessToken());
            return apiClient;
        } catch (ApiException e) {
            throw new AssertionError(e);
        }
    }
}
