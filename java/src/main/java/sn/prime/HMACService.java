package sn.prime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * HMAC Verification Service for validating webhook requests using SHA-512.
 */
public class HMACService {
    private final String hmacKey;
    private final String apiKey;

    /**
     * Creates a new HMAC Service instance with provided HMAC_KEY and API_KEY.
     *
     * @param hmacKey The secret key used for HMAC signature generation
     * @param apiKey  The API key for request authentication
     * @throws IllegalArgumentException if required configuration is missing
     */
    public HMACService(String hmacKey, String apiKey) {
        if (hmacKey == null || hmacKey.isEmpty() || apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("HMAC key and API key are required");
        }
        this.hmacKey = hmacKey;
        this.apiKey = apiKey;
    }

    /**
     * Verifies request headers and payload signature.
     *
     * @param headers Request headers containing X-Api-Key and X-Hmac-Signature
     * @param payload Request payload as a Payload object
     * @return VerificationResult containing validation status and error message if any
     */
    public VerificationResult verify(Map<String, String> headers, Payload payload) {
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
     * Calculates HMAC signature for the given payload using SHA-512.
     *
     * @param payload Request payload as a Payload object
     * @return Base64 encoded HMAC signature
     * @throws RuntimeException if signature calculation fails
     */
    public String calculateSignature(Payload payload) {
        try {
            // Validate required fields
            validatePayload(payload);

            // Build data string
            String dataToSign = payload.statusPayment() +
                    payload.primeClientPhone() +
                    payload.amount();

            // Create HMAC SHA-512 instance
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(
                    hmacKey.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA512"
            );
            sha512Hmac.init(secretKey);

            // Calculate HMAC
            byte[] hmacBytes = sha512Hmac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));

            // Encode to Base64
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate signature: " + e.getMessage());
        }
    }

    /**
     * Validates the payload for required fields.
     *
     * @param payload Request payload as a Payload object
     * @throws IllegalArgumentException if any required field is missing or invalid
     */
    private void validatePayload(Payload payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }

        if (payload.statusPayment() == null || payload.statusPayment().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: statusPayment");
        }

        if (payload.primeClientPhone() == null || payload.primeClientPhone().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: primeClientPhone");
        }

        if (payload.amount() == null || payload.amount().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: amount");
        }
    }

    /**
     * Result class for verification operations.
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