<?php
/**
 * HMAC Verification Service
 *
 * This service handles HMAC-based request verification for secure API communications.
 * It verifies both the API key and HMAC signature of incoming requests using SHA512.
 *
 * Usage example:
 * ```php
 * $hmacService = new HMACService([
 *     'hmacKey' => 'your-hmac-key',
 *     'apiKey'  => 'your-api-key'
 * ]);
 * 
 * $result = $hmacService->verify($headers, $payload);
 * if ($result['isValid']) {
 *     // Process the verified request
 * }
 * ```
 * 
 * @version 1.0.0
 */
class HMACService {
    /** @var string The secret key used for HMAC signature generation */
    private string $hmacKey;
    /** @var string The API key for request authentication */
    private string $apiKey;

    /**
     * Creates a new HMAC Service instance
     *
     * @param array $config Configuration array containing 'hmacKey' and 'apiKey'
     *                     hmacKey: The secret key for HMAC signature generation
     *                     apiKey: The API key for request authentication
     * @throws InvalidArgumentException If required configuration is missing
     */
    public function __construct(array $config) {
        if (!isset($config['hmacKey']) || !is_string($config['hmacKey'])) {
            throw new InvalidArgumentException("HMAC key is required and must be a string");
        }
        if (!isset($config['apiKey']) || !is_string($config['apiKey'])) {
            throw new InvalidArgumentException("API key is required and must be a string");
        }

        $this->hmacKey = $config['hmacKey'];
        $this->apiKey = $config['apiKey'];
    }

    /**
     * Verifies request headers and payload signature
     *
     * @param array $headers Request headers containing 'X-Api-Key' and 'X-Hmac-Signature'
     * @param array $payload Request payload with required fields:
     *                      - statusPayment: Payment status (e.g., "SUCCEED")
     *                      - primeClientPhone: Client phone number
     *                      - amount: Transaction amount
     * 
     * @return array Verification result with structure:
     *               ['isValid' => bool, 'error' => string|null]
     */
    public function verify(array $headers, array $payload): array {
        try {
            // Normalize header keys to lowercase for case-insensitive comparison
            $normalizedHeaders = array_change_key_case($headers, CASE_LOWER);

            // Validate required headers
            if (!isset($normalizedHeaders['x-api-key'])) {
                return ['isValid' => false, 'error' => 'Missing API key'];
            }
            if (!isset($normalizedHeaders['x-hmac-signature'])) {
                return ['isValid' => false, 'error' => 'Missing HMAC signature'];
            }

            // Verify API key
            if ($normalizedHeaders['x-api-key'] !== $this->apiKey) {
                return ['isValid' => false, 'error' => 'Invalid API key'];
            }

            // Validate payload
            $requiredFields = ['statusPayment', 'primeClientPhone', 'amount'];
            foreach ($requiredFields as $field) {
                if (!isset($payload[$field])) {
                    return ['isValid' => false, 'error' => "Missing required field: $field"];
                }
            }

            // Calculate and verify HMAC signature
            $providedSignature = $normalizedHeaders['x-hmac-signature'];
            $calculatedSignature = $this->calculateSignature($payload);

            if ($providedSignature !== $calculatedSignature) {
                return ['isValid' => false, 'error' => 'Invalid signature'];
            }

            return ['isValid' => true];
        } catch (Exception $error) {
            return ['isValid' => false, 'error' => $error->getMessage()];
        }
    }

    /**
     * Calculates HMAC signature for the given payload using SHA512
     *
     * The signature is calculated using SHA512 algorithm and base64 encoded.
     * The data string for signature is constructed by concatenating:
     * statusPayment + primeClientPhone + amount
     *
     * @param array $payload Request payload
     * @return string Base64 encoded HMAC signature
     */
    public function calculateSignature(array $payload): string {
        // Concatenate required fields in specific order
        $dataToSign = $payload['statusPayment'] . $payload['primeClientPhone'] . $payload['amount'];

        // Calculate HMAC using SHA512
        $hmac = hash_hmac('sha512', $dataToSign, $this->hmacKey, true);

        // Return base64 encoded signature
        return base64_encode($hmac);
    }
}