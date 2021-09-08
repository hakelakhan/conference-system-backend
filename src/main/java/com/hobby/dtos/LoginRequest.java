package com.hobby.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty @Email
    private String email;

    @NotEmpty
    private String password;
}
