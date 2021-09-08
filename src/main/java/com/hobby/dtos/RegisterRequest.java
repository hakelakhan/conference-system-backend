package com.hobby.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Email(message = "EmailId should be valid") @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    @NotBlank @Size(message = "Password must have at least 8 characters")
    private String password;
}