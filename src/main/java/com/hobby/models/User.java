package com.hobby.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hobby.enuns.RegistrationStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "webconference_users")
public class User {
    @Id
    @GeneratedValue
    private long userId;

    @NotEmpty
    private String fullName;

    @Email(message = "Email Id must be valid")
    @Column(name="email", unique = true)
    private String email;

    @NotEmpty @Size(min = 8, message = "Password must be at least 8 characters long")
    @JsonIgnore
    private String password;

    private RegistrationStatus registrationStatus;

    private LocalDateTime createdTime;
    private LocalDateTime lastLoggedInTime;

    @OneToMany
    private Set<Talk> talks;

}
