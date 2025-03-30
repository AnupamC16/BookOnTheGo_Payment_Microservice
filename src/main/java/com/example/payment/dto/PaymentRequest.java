package com.example.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull
    @Min(1)
    private Long amount;

    @NotBlank
    private String paymentMethodId;

    @NotBlank
    private String userEmail;

    @NotBlank
    private String bookingId;

    @NotBlank
    private String attendeeName;

    @NotBlank
    private String eventName;

    @NotBlank
    private String eventDate;

    @NotBlank
    private String eventTime;

    @NotBlank
    private String venue;

    @NotBlank
    private String eventId;
}
