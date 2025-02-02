# Prime Webhook Verification Service

A Java-based HMAC verification service for validating webhook requests using SHA-512. This service ensures secure communication by verifying API keys and HMAC signatures for incoming webhook requests after validate initial payment.

## Features

- HMAC-SHA512 signature verification
- API key validation
- Environment variable configuration support
- Comprehensive payload validation
- Thread-safe implementation

## Prerequisites

- Java 16 or higher (Records were previewed in Java 14-15 and officially released in Java 16)
- Maven or Gradle (for dependency management)

### Java Version Compatibility

This project uses Java Records, which have the following version support:
- Java 14-15: Available as a preview feature (requires `--enable-preview` flag)
- Java 16+: Fully supported as a standard feature
- Recommended: Java 17 LTS or higher for long-term support

### Java 8 Compatibility

For Java 8 compatibility, replace the `record` with a traditional class. Here's how to convert the Payload:

```java
public class Payload {
    private final String callbackUrl;
    private final String amount;
    private final String primeClientPhone;
    private final String externalRefId;
    private final String statusPayment;
    private final String datePayment;
    private final String primeInternalPaymentRef;

    public Payload(
            String callbackUrl,
            String amount,
            String primeClientPhone,
            String externalRefId,
            String statusPayment,
            String datePayment,
            String primeInternalPaymentRef) {
        this.callbackUrl = callbackUrl;
        this.amount = amount;
        this.primeClientPhone = primeClientPhone;
        this.externalRefId = externalRefId;
        this.statusPayment = statusPayment;
        this.datePayment = datePayment;
        this.primeInternalPaymentRef = primeInternalPaymentRef;
    }

    // Getters
    public String getCallbackUrl() { return callbackUrl; }
    public String getAmount() { return amount; }
    public String getPrimeClientPhone() { return primeClientPhone; }
    public String getExternalRefId() { return externalRefId; }
    public String getStatusPayment() { return statusPayment; }
    public String getDatePayment() { return datePayment; }
    public String getPrimeInternalPaymentRef() { return primeInternalPaymentRef; }

    // Override equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload payload = (Payload) o;
        return Objects.equals(callbackUrl, payload.callbackUrl) &&
               Objects.equals(amount, payload.amount) &&
               Objects.equals(primeClientPhone, payload.primeClientPhone) &&
               Objects.equals(externalRefId, payload.externalRefId) &&
               Objects.equals(statusPayment, payload.statusPayment) &&
               Objects.equals(datePayment, payload.datePayment) &&
               Objects.equals(primeInternalPaymentRef, payload.primeInternalPaymentRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callbackUrl, amount, primeClientPhone, 
                          externalRefId, statusPayment, datePayment, 
                          primeInternalPaymentRef);
    }

    // Override toString()
    @Override
    public String toString() {
        return "Payload{" +
               "callbackUrl='" + callbackUrl + '\'' +
               ", amount='" + amount + '\'' +
               ", primeClientPhone='" + primeClientPhone + '\'' +
               ", externalRefId='" + externalRefId + '\'' +
               ", statusPayment='" + statusPayment + '\'' +
               ", datePayment='" + datePayment + '\'' +
               ", primeInternalPaymentRef='" + primeInternalPaymentRef + '\'' +
               '}';
    }
}
```

Also update the HMACService class to use getter methods instead of record accessors:
```java
// Replace record accessor calls in calculateSignature method
String dataToSign = payload.getStatusPayment() +
                   payload.getPrimeClientPhone() +
                   payload.getAmount();
```

## Installation

1. Clone the repository:
```bash
git clone https://github.com/ouznoreyni/prime-sdk.git
cd prime-sdk/java
```

2. Create a `.env` file in the project root directory:
```bash
HMAC_KEY="your_hmac_secret_key"
API_KEY="your_api_key"
```

## Usage

### Basic Implementation

1. Initialize the HMAC Service(from .env value):
```java
String hmacKey = "your_hmac_secret_key";
String apiKey = "your_api_key";
HMACService hmacService = new HMACService(hmacKey, apiKey);
```

2. Create a webhook payload:
```java
Payload payload = new Payload(
    "https://your-callback-url.com",
    "100.00",
    "+221767760904",
    "external_ref_123",
    "SUCCEED",
    "2025-02-02T21:28:48.351596Z",
    "internal_ref_456"
);
```

3. Set up request headers:
```java
Map<String, String> headers = new HashMap<>();
headers.put("x-api-key", "your_api_key");
headers.put("x-hmac-signature", "calculated_signature");
```

4. Verify the webhook request:
```java
VerificationResult result = hmacService.verify(headers, payload);
if (result.isValid()) {
    // Process the webhook
} else {
    // Handle invalid request
    System.err.println("Verification failed: " + result.error());
}
```

### Calculating Signatures

To calculate a signature for outgoing requests:

```java
String signature = hmacService.calculateSignature(payload);
```

## Payload Structure

The `Payload` record contains the following fields:

| Field | Description | Required |
|-------|-------------|----------|
| callbackUrl | Webhook destination URL | Yes |
| amount | Transaction amount | Yes |
| primeClientPhone | Client's phone number | Yes |
| externalRefId | External reference ID | Yes |
| statusPayment | Payment status | Yes |
| datePayment | Payment processing date | Yes |
| primeInternalPaymentRef | Internal payment reference | Yes |

## Error Handling

The service provides detailed error messages for various scenarios:

- Missing or invalid API key
- Missing or invalid HMAC signature
- Missing required payload fields
- Invalid signature calculation
- Environment configuration errors

## Security Considerations

1. Store the HMAC key and API key securely using environment variables
2. Use HTTPS for all webhook communications
3. Rotate keys periodically
4. Validate all incoming payloads before processing
5. Implement request timeout mechanisms
6. Use strong, unique keys for each integration

## Support

For support, please open an issue in the GitHub repository or contact the development team.