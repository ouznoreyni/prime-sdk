<?php
require_once 'HMACService.php';

// Load environment variables from .env file
$envPath = '.env';
if (!file_exists($envPath)) {
    die("Error: .env file not found. Please create it and define HMAC_KEY and API_KEY.\n");
}

$env = @parse_ini_file($envPath);
if (!$env || !isset($env['HMAC_KEY']) || !isset($env['API_KEY'])) {
    die("Error: Invalid .env file. Ensure it contains HMAC_KEY and API_KEY.\n");
}

// Initialize HMACService
$hmacService = new HMACService([
    'hmacKey' => $env['HMAC_KEY'],
    'apiKey' => $env['API_KEY']
]);

// Headers from the request
$headers = [
    "host" => "api-mock.cortech.cloud",
    "x-real-ip" => "154.124.76.187",
    "x-forwarded-for" => "154.124.76.187",
    "x-forwarded-proto" => "https",
    "connection" => "upgrade",
    "content-length" => "346",
    "accept-encoding" => "gzip",
    "user-agent" => "ReactorNetty/1.1.22",
    "accept" => "application/json",
    "content-type" => "application/json",
    "x-api-key" => $env['API_KEY'], // Use API_KEY from .env
    "x-hmac-signature" => "kCvGd2x3h04q4HPG7FQ1ObGK/c1s6TmoxZC8rRi5gWLJaLuwNajg1mXuOPdXicggWL46BNTz6fK7iE7srcej+Q==" 
];

// Payload from the request
$payload = [
    "callbackUrl" => "https://api-mock.cortech.cloud/api/m/95691c2b-e38c-44b2-986f-dff2c3db2ea7",
    "amount" => "6.00",
    "primeClientPhone" => "+221767760904",
    "externalRefId" => "externalRefId4",
    "statusPayment" => "SUCCEED",
    "datePayment" => "2025-02-02T21:28:48.351596Z",
    "primeInternalPaymentRef" => "dab17de0-4ed8-4d4a-92d1-a6bba2491bd4"
];

echo "Testing with real request data:\n";
echo "=============================\n";

// Verify the request
$result = $hmacService->verify($headers, $payload);

// Output verification result
echo "Verification Result: " . ($result['isValid'] ? "VALID" : "INVALID") . "\n";
if (!$result['isValid']) {
    echo "Error: " . $result['error'] . "\n";
}

// Show signature comparison for debugging
echo "\nSignature Comparison:\n";
echo "Expected: " . $headers['x-hmac-signature'] . "\n";
echo "Actual:   " . $hmacService->calculateSignature($payload) . "\n";