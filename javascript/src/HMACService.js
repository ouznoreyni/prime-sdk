/**
 * @fileoverview HMAC Verification SDK
 * This SDK provides HMAC-based request verification functionality for secure API communications.
 * It implements HMAC-SHA512 signature generation and verification along with API key validation.
 * @version 1.0.1
 */
import crypto from "crypto";

/**
 * Configuration options for the HMACService
 * @typedef {Object} HMACConfig
 * @property {string} hmacKey - The secret key used for HMAC signature generation
 * @property {string} apiKey - The API key for request authentication
 */

/**
 * Response structure for verification results
 * @typedef {Object} VerificationResult
 * @property {boolean} isValid - Indicates if the verification was successful
 * @property {string} [error] - Error message if verification failed
 */

/**
 * Payload structure expected for signature calculation
 * @typedef {Object} Payload
 * @property {string} statusPayment - Payment status
 * @property {string} primeClientPhone - Client phone number
 * @property {number} amount - Transaction amount
 */

export class HMACService {
  #hmacKey;
  #apiKey;

  /**
   * Creates an instance of HMACService
   * @param {HMACConfig} config - Configuration object
   * @throws {Error} If required configuration parameters are missing
   */
  constructor(config) {
    this.#validateConfig(config);
    this.#hmacKey = config.hmacKey;
    this.#apiKey = config.apiKey;
  }

  /**
   * Validates the configuration object
   * @private
   * @param {HMACConfig} config - Configuration object to validate
   * @throws {Error} If validation fails
   */
  #validateConfig(config) {
    if (!config?.hmacKey || typeof config.hmacKey !== "string") {
      throw new Error("HMAC key is required and must be a string");
    }
    if (!config?.apiKey || typeof config.apiKey !== "string") {
      throw new Error("API key is required and must be a string");
    }
  }

  /**
   * Calculates HMAC signature for the given payload using SHA512
   * @param {Payload} payload - The payload to sign
   * @returns {string} Base64 encoded HMAC signature
   * @throws {Error} If signature calculation fails
   */
  calculateSignature(payload) {
    try {
      this.#validatePayload(payload);

      // Create HMAC instance with SHA512
      const hmac = crypto.createHmac(
        "sha512",
        Buffer.from(this.#hmacKey, "utf8")
      );

      // Build and sign data string
      const dataToSign = this.#buildDataString(payload);
      hmac.update(Buffer.from(dataToSign, "utf8"));

      return hmac.digest("base64");
    } catch (error) {
      throw new Error(`HMAC calculation failed: ${error.message}`);
    }
  }

  /**
   * Validates the payload structure
   * @private
   * @param {Payload} payload - Payload to validate
   * @throws {Error} If payload validation fails
   */
  #validatePayload(payload) {
    const requiredFields = ["statusPayment", "primeClientPhone", "amount"];
    const missingFields = requiredFields.filter((field) => !payload?.[field]);
    if (missingFields.length > 0) {
      throw new Error(`Missing required fields: ${missingFields.join(", ")}`);
    }
  }

  /**
   * Builds the data string for signature calculation
   * @private
   * @param {Payload} payload - Payload to process
   * @returns {string} Concatenated data string
   */
  #buildDataString(payload) {
    return `${payload.statusPayment}${payload.primeClientPhone}${payload.amount}`;
  }

  /**
   * Verifies request headers and payload signature
   * @param {Object.<string, string>} headers - Request headers
   * @param {Payload} payload - Request payload
   * @returns {VerificationResult} Verification result
   */
  verify(headers, payload) {
    try {
      // Normalize and validate headers
      const normalizedHeaders = this.#normalizeHeaders(headers);

      // API key verification
      if (normalizedHeaders["x-api-key"] !== this.#apiKey) {
        return {
          isValid: false,
          error: "Invalid API key",
        };
      }

      // Signature verification
      const providedSignature = normalizedHeaders["x-hmac-signature"];
      const calculatedSignature = this.calculateSignature(payload);

      if (providedSignature !== calculatedSignature) {
        return {
          isValid: false,
          error: "Invalid signature",
        };
      }

      return { isValid: true };
    } catch (error) {
      return {
        isValid: false,
        error: error.message,
      };
    }
  }

  /**
   * Normalizes header keys to lowercase
   * @private
   * @param {Object.<string, string>} headers - Headers to normalize
   * @returns {Object.<string, string>} Normalized headers
   */
  #normalizeHeaders(headers) {
    return Object.entries(headers).reduce((acc, [key, value]) => {
      acc[key.toLowerCase()] = value;
      return acc;
    }, {});
  }
}
