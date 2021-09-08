package com.hobby.models;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;

@Builder
@Getter
public class NotificationEmail {
        @Email(message="Invalid email address")
        private String recepient;

        private String subject;
        private String body;

}
