package com.jml.emailextraction.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class MessageDto {
    private String subject;
    private From from;
    private String bodyPreview;
    private Body body;

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    public static class From {
        private EmailAddress emailAddress;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    public static class EmailAddress {
        private String name;
        private String address;

    }

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    public static class Body {
        private String contentType;
        private String content;
    }
}
