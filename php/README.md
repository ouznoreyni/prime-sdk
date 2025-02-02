# Prime Webhook Verification Service - PHP Implementation

A PHP implementation of the HMAC verification service for validating webhook requests using SHA-512. This service ensures secure communication by verifying API keys and HMAC signatures for incoming webhook requests after validate initial payment.

## Features

- HMAC-SHA512 signature verification
- API key validation
- Environment variable configuration
- Request header normalization
- Comprehensive payload validation

## Prerequisites

- PHP 7.4 or higher (for typed properties support)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/ouznoreyni/prime-sdk.git
cd prime-sdk/php
```

2. Create a `.env` file in the project root directory:
```env
API_KEY=prime_api_key_ec09544f-5fc1-416f-89d6-7ed7bfbf5e4d
HMAC_KEY=prime_hmac_key_8b2182d5-9212-4130-95f6-56c224a16667
```

## Usage

### Basic Implementation

1. Initialize the HMAC Service:
```php
require_once 'HMACService.php';

$hmacService = new HMACService([
    'hmacKey' => 'your_hmac_secret_key',
    'apiKey'  => 'your_api_key'
]);
```

2. Prepare the request headers and payload:
```php
$headers = [
    'x-api-key' => 'your_api_key',
    'x-hmac-signature' => 'calculated_signature'
];

$payload = [
    'statusPayment' => 'SUCCEED',
    'primeClientPhone' => '+221767760904',
    'amount' => '100.00',
    'callbackUrl' => 'https://your-callback-url.com',
    'externalRefId' => 'external_ref_123',
    'datePayment' => '2025-02-02T21:28:48.351596Z',
    'primeInternalPaymentRef' => 'internal_ref_456'
];
```

3. Verify the webhook request:
```php
$result = $hmacService->verify($headers, $payload);
if ($result['isValid']) {
    // Process the webhook
} else {
    // Handle invalid request
    error_log('Verification failed: ' . $result['error']);
}
```

### Loading Environment Variables

```php
$envPath = '.env';
if (!file_exists($envPath)) {
    die("Error: .env file not found\n");
}

$env = parse_ini_file($envPath);
if (!$env || !isset($env['HMAC_KEY']) || !isset($env['API_KEY'])) {
    die("Error: Invalid .env file\n");
}

$hmacService = new HMACService([
    'hmacKey' => $env['HMAC_KEY'],
    'apiKey' => $env['API_KEY']
]);
```

### Calculating Signatures

To calculate a signature for outgoing requests:

```php
$signature = $hmacService->calculateSignature($payload);
```

## Payload Structure

The payload array must contain the following required fields:

| Field | Description | Required |
|-------|-------------|----------|
| statusPayment | Payment status | Yes |
| primeClientPhone | Client's phone number | Yes |
| amount | Transaction amount | Yes |
| callbackUrl | Webhook destination URL | No |
| externalRefId | External reference ID | No |
| datePayment | Payment processing date | No |
| primeInternalPaymentRef | Internal payment reference | No |

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
4. Validate all incoming payloads
5. Implement request timeout mechanisms
6. Use strong, unique keys for each integration

## PHP Version Compatibility

- PHP 7.4+: Full support with typed properties
- PHP 7.0-7.3: Remove property type hints:
```php
private $hmacKey; // Instead of private string $hmacKey;
private $apiKey;  // Instead of private string $apiKey;
```


## Support

For support, please open an issue in the GitHub repository or contact the development team.