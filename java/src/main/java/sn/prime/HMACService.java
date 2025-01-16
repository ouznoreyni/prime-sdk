package sn.prime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * HMAC Verification Service for validating webhook requests
 */
public class HMACService {
    private final String hmacKey;
    private final String apiKey;

    /**
     * Creates a new HMAC Service instance
     *
     * @param config Configuration map containing hmacKey and apiKey
     * @throws IllegalArgumentException if required configuration is missing
     */
    public HMACService(Map<String, String> config) {
        if (config.get("hmacKey") == null || config.get("apiKey") == null) {
            throw new IllegalArgumentException("HMAC key and API key are required");
        }
        this.hmacKey = config.get("hmacKey");
        this.apiKey = config.get("apiKey");
    }

    /**
     * Verifies request headers and payload signature
     *
     * @param headers Request headers containing X-Api-Key and X-Hmac-Signature
     * @param payload Request payload
     * @return VerificationResult containing validation status and error message if any
     */
    public VerificationResult verify(Map<String, String> headers, Map<String, String> payload) {
        try {
            // Check API key
            String providedApiKey = headers.get("x-api-key");
            if (providedApiKey == null || !providedApiKey.equals(this.apiKey)) {
                return new VerificationResult(false, "Invalid API key");
            }

            // Check HMAC signature
            String providedSignature = headers.get("x-hmac-signature");
            if (providedSignature == null) {
                return new VerificationResult(false, "Missing HMAC signature");
            }

            String calculatedSignature = calculateSignature(payload);
            if (!providedSignature.equals(calculatedSignature)) {
                return new VerificationResult(false, "Invalid signature");
            }

            return new VerificationResult(true, null);
        } catch (Exception e) {
            return new VerificationResult(false, "Verification failed: " + e.getMessage());
        }
    }

    /**
     * Calculates HMAC signature for the given payload
     *
     * @param payload Request payload
     * @return Base64 encoded HMAC signature
     * @throws RuntimeException if signature calculation fails
     */
    public String calculateSignature(Map<String, String> payload) {
        try {
            // Validate required fields
            validatePayload(payload);

            // Build data string
            String dataToSign = payload.get("statusPayment") +
                    payload.get("primeClientPhone") +
                    payload.get("amount");

            // Create HMAC SHA256 instance
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    hmacKey.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            sha256Hmac.init(secretKey);

            // Calculate HMAC
            byte[] hmacBytes = sha256Hmac.doFinal(
                    dataToSign.getBytes(StandardCharsets.UTF_8)
            );

            // Encode to Base64
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate signature: " + e.getMessage());
        }
    }

    private void validatePayload(Map<String, String> payload) {
        String[] requiredFields = {"statusPayment", "primeClientPhone", "amount"};
        for (String field : requiredFields) {
            if (!payload.containsKey(field) || payload.get(field) == null || payload.get(field).isEmpty()) {
                throw new IllegalArgumentException("Missing required field: " + field);
            }
        }
    }

    /**
         * Result class for verification operations
         */
        public record VerificationResult(boolean isValid, String error) {

        @Override
            public String toString() {
                return "VerificationResult{" +
                        "isValid=" + isValid +
                        (error != null ? ", error='" + error + "'" : "") +
                        '}';
            }
        }
}