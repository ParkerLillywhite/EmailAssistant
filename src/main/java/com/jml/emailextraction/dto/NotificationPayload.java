package com.jml.emailextraction.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPayload {
    private List<NotificationDto> value;
}
