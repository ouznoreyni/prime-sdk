package sn.prime;

/**
 * Data Transfer Object (DTO) for the webhook payload.
 * @param callbackUrl The URL to send the webhook to
 * @param amount The transaction amount
 * @param primeClientPhone The client's phone number
 * @param externalRefId External reference ID for the transaction
 * @param statusPayment Current status of the payment
 * @param datePayment Date when the payment was processed
 * @param primeInternalPaymentRef Internal reference ID for the payment
 */
public record Payload(
        String callbackUrl,
        String amount,
        String primeClientPhone,
        String externalRefId,
        String statusPayment,
        String datePayment,
        String primeInternalPaymentRef
) {}