package sn.prime;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static final Properties ENV = new Properties();


    public static void main(String[] args) {
        try {
            // Load environment variables from .env file
            loadEnv();

            // Get environment variables
            String hmacKey = ENV.getProperty("HMAC_KEY");
            String apiKey = ENV.getProperty("API_KEY");

            if (hmacKey == null || apiKey == null) {
                throw new RuntimeException("Required environment variables HMAC_KEY and API_KEY must be set");
            }

            // Initialize HMACService
            HMACService hmacService = new HMACService(hmacKey, apiKey);

            // Example headers
            Map<String, String> headers = new HashMap<>();
            headers.put("x-api-key", "prime_api_key_ec09544f-5fc1-416f-89d6-7ed7bfbf5e4d");
            headers.put("x-hmac-signature", "kCvGd2x3h04q4HPG7FQ1ObGK/c1s6TmoxZC8rRi5gWLJaLuwNajg1mXuOPdXicggWL46BNTz6fK7iE7srcej+Q==");

            // Example payload
            var payload = new Payload(
                    "https://api-mock.cortech.cloud/api/m/95691c2b-e38c-44b2-986f-df",
                    "6.00",
                    "+221767760904",
                    "externalRefId4",
                    "SUCCEED",
                    "2025-02-02T21:28:48.351596Z",
                    "dab17de0-4ed8-4d4a-92d1-a6bba2491bd4"
            );

            // Verify the request
            var result = hmacService.verify(headers, payload);
            System.out.println(result);

        } catch (IOException e) {
            System.err.println("Error loading .env file: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void loadEnv() throws IOException {
        Path envPath = Path.of(".env");
        if (Files.exists(envPath)) {
            Files.lines(envPath)
                    .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("#"))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            // Remove quotes if present
                            if (value.startsWith("\"") && value.endsWith("\"")) {
                                value = value.substring(1, value.length() - 1);
                            }
                            ENV.setProperty(key, value);
                        }
                    });
        } else {
            throw new IOException(".env file not found in: " + envPath.toAbsolutePath());
        }
    }
}