## Secure API Verification Server

This is an example Express.js server that implements HMAC-based request verification using the `HMACService` SDK. The server ensures secure communication by validating API keys and HMAC signatures for incoming requests.


## Overview

This server demonstrates how to integrate the `HMACService` into an Express.js application for secure API endpoint verification. It uses HMAC-SHA512 for signature generation and validation, ensuring that only authorized clients can send valid requests.

---

## Features

- **HMAC-SHA512 Signature Verification**: Ensures the integrity and authenticity of incoming requests.
- **API Key Validation**: Verifies the validity of the API key provided in the request headers.
- **Express.js Integration**: Built using the Express.js framework for handling HTTP requests.
- **Global Error Handling**: Catches and responds to unexpected errors gracefully.

---

## Prerequisites

Before running the server, ensure you have the following installed:

- **Node.js** (v14 or higher): [Download Node.js](https://nodejs.org/)
- **npm** (comes with Node.js): Package manager for installing dependencies.
- **dotenv**: For managing environment variables.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ouznoreyni/prime-sdk.git
   cd prime-sdk/javascript
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Create a `.env` file in the root directory with the following content:
   ```env
   HMAC_KEY=your-secret-hmac-key
   API_KEY=your-api-key
   PORT=3000
   ```
   Replace `your-secret-hmac-key` and `your-api-key` with actual values.

---

## Configuration

The server uses environment variables for configuration. You can customize the following settings in the `.env` file:

| Variable     | Description                                | Default Value |
|--------------|--------------------------------------------|---------------|
| `HMAC_KEY`   | Secret key used for HMAC signature         | None          |
| `API_KEY`    | API key for request authentication         | None          |
| `PORT`       | Port on which the server will listen       | `3000`        |

---

## Usage

### Endpoints to test

#### POST `/api/callback`

This endpoint processes verified requests. It validates the API key and HMAC signature before processing the callback.

##### Example Request

Using `curl`:

```bash
curl -X POST http://localhost:3000/api/callback \
-H "Content-Type: application/json" \
-H "x-api-key: your-api-key" \
-H "x-hmac-signature: BASE64_ENCODED_SIGNATURE" \
-d '{
  "{
  "callbackUrl": "https://api-mock.cortech.cloud/api/m/95691c2b-e38c-44b2-986f-dff2c3db2ea7",
  "amount": "6.00",
  "primeClientPhone": "+221767760904",
  "externalRefId": "externalRefId4",
  "statusPayment": "SUCCEED",
  "datePayment": "2025-02-02T21:28:48.351596Z",
  "primeInternalPaymentRef": "dab17de0-4ed8-4d4a-92d1-a6bba2491bd4"
}
}'
```

Replace `BASE64_ENCODED_SIGNATURE` with the actual HMAC-SHA512 signature generated using the `HMAC_KEY`.


## Contact

For questions or support, please contact [ousmanediopp268@example.com](mailto:your-email@example.com).

Thank you for using this secure API verification server

