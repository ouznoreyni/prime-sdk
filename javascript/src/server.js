/**
 * @fileoverview Example Express server implementing the HMAC Verification SDK
 * This file demonstrates how to integrate the HMACService into an Express.js application
 * for secure API endpoint verification.
 */
import dotenv from "dotenv";
import express from "express";
import { HMACService } from "./HMACService.js";

// Load environment variables
dotenv.config();

// Validate required environment variables
const requiredEnvVars = ["HMAC_KEY", "API_KEY"];
const missingEnvVars = requiredEnvVars.filter(
  (varName) => !process.env[varName]
);
if (missingEnvVars.length > 0) {
  console.error(
    `Missing required environment variables: ${missingEnvVars.join(", ")}`
  );
  process.exit(1);
}

// Initialize HMAC service
const hmacService = new HMACService({
  hmacKey: process.env.HMAC_KEY,
  apiKey: process.env.API_KEY,
});

// Create Express application
const app = express();

// Middleware for parsing JSON bodies
app.use(express.json());

/**
 * Error handler middleware
 */
app.use((err, req, res, next) => {
  console.error("Server error:", err);
  res.status(500).json({
    status: "error",
    message: "Internal server error",
  });
});

/**
 * Callback endpoint for processing verified requests
 * @route POST /api/callback
 */
app.post("/api/callback", (req, res) => {
  // Normalize request headers
  const normalizedHeaders = {};
  Object.keys(req.headers).forEach((key) => {
    normalizedHeaders[key.toLowerCase()] = req.headers[key];
  });

  // Verify required headers
  const requiredHeaders = ["x-api-key", "x-hmac-signature"];
  const missingHeaders = requiredHeaders.filter(
    (header) => !normalizedHeaders[header]
  );
  if (missingHeaders.length > 0) {
    return res.status(401).json({
      status: "error",
      message: `Missing required headers: ${missingHeaders.join(", ")}`,
    });
  }

  // Verify request
  const verification = hmacService.verify(normalizedHeaders, req.body);
  if (!verification.isValid) {
    return res.status(401).json({
      status: "error",
      message: verification.error,
    });
  }

  // Process verified request
  try {
    // Add your business logic here
    res.json({
      status: "success",
      message: "Callback processed successfully",
    });
  } catch (error) {
    res.status(500).json({
      status: "error",
      message: "Error processing callback",
    });
  }
});

// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
