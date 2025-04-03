package com.example.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String userEmail;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String venue;
    private String bookingId;
    private String eventId;
}
