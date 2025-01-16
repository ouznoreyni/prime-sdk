<?php

require_once 'HMACService.php';

// Load environment variables
$env = parse_ini_file('.env');

// Initialize service
$hmacService = new HMACService([
    'hmacKey' => $env['HMAC_KEY'],
    'apiKey' => $env['API_KEY']
]);

// Real headers from the request
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
    "x-api-key" => "prime_api_key_2bc2e105-8432-4a81-be8a-dbfb0209eba4",
    "x-hmac-signature" => "X+o3GN/avoky0h1nEGDdqDuPBhmViuDDF4k3YNgyEto="
];

// Real payload from the request
$payload = [
    "callbackUrl" => "https://api-mock.cortech.cloud/api/m/3dfce244-b7ea-48d4-9ff9-cbe8b06351cd",
    "amount" => "1.00",
    "primeClientPhone" => "+221767760904",
    "externalRefId" => "externalRefId2",
    "statusPayment" => "SUCCEED",
    "datePayment" => "2025-01-16T15:40:26.128312Z",
    "primeInternalPaymentRef" => "649a4fdc-a865-4c00-b8d4-ce4ac501fb60"
];

echo "Testing with real request data:\n";
echo "=============================\n";

// Verify the request
$result = $hmacService->verify($headers, $payload);

echo "Verification Result: " . ($result['isValid'] ? "VALID" : "INVALID") . "\n";
if (!$result['isValid']) {
    echo "Error: " . $result['error'] . "\n";
}

// Show calculated signature for comparison
echo "\nSignature Comparison:\n";
echo "Expected Signature: " . $headers['x-hmac-signature'] . "\n";
echo "Calculated Signature: " . $hmacService->calculateSignature($payload) . "\n";

// Show data used for signature calculation
$dataString = $payload['statusPayment'] . $payload['primeClientPhone'] . $payload['amount'];
echo "\nData String Used: " . $dataString . "\n";