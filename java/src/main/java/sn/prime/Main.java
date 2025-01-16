package sn.prime;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Configuration
        Map<String, String> config = new HashMap<>();
        config.put("hmacKey", "prime_hmac_key_f226959b-50a3-4cd2-9a2f-64b44cdb1258");
        config.put("apiKey", "prime_api_key_2bc2e105-8432-4a81-be8a-dbfb0209eba4");

        // Initialize service
        HMACService hmacService = new HMACService(config);

        // Test headers (from real request)
        Map<String, String> headers = new HashMap<>();
        headers.put("host", "api-mock.cortech.cloud");
        headers.put("x-real-ip", "154.124.76.187");
        headers.put("x-forwarded-for", "154.124.76.187");
        headers.put("x-forwarded-proto", "https");
        headers.put("connection", "upgrade");
        headers.put("content-length", "346");
        headers.put("accept-encoding", "gzip");
        headers.put("user-agent", "ReactorNetty/1.1.22");
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");
        headers.put("x-api-key", "prime_api_key_2bc2e105-8432-4a81-be8a-dbfb0209eba4");
        headers.put("x-hmac-signature", "X+o3GN/avoky0h1nEGDdqDuPBhmViuDDF4k3YNgyEto=");

        // Test payload (from real request)
        Map<String, String> payload = new HashMap<>();
        payload.put("callbackUrl", "https://api-mock.cortech.cloud/api/m/3dfce244-b7ea-48d4-9ff9-cbe8b06351cd");
        payload.put("amount", "1.00");
        payload.put("primeClientPhone", "+221767760904");
        payload.put("externalRefId", "externalRefId2");
        payload.put("statusPayment", "SUCCEED");
        payload.put("datePayment", "2025-01-16T15:40:26.128312Z");
        payload.put("primeInternalPaymentRef", "649a4fdc-a865-4c00-b8d4-ce4ac501fb60");

        System.out.println("Testing with real request data:");
        System.out.println("=============================");

        // Test verification
        HMACService.VerificationResult result = hmacService.verify(headers, payload);
        System.out.println("Verification Result: " + (result.isValid() ? "VALID" : "INVALID"));
        if (!result.isValid()) {
            System.out.println("Error: " + result.error());
        }

        // Show signature comparison
        System.out.println("\nSignature Comparison:");
        System.out.println("Expected Signature: " + headers.get("x-hmac-signature"));
        System.out.println("Calculated Signature: " + hmacService.calculateSignature(payload));

        // Show data used for signature
        String dataString = payload.get("statusPayment") +
                payload.get("primeClientPhone") +
                payload.get("amount");
        System.out.println("\nData String Used: " + dataString);
    }
}