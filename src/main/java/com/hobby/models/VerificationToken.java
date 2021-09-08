package com.hobby.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Builder
@Entity
@Data
@Table(name="verification_token")
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = LAZY)
    private User user;

    private Instant validTill;

    public static VerificationToken createVerificationTokenForUser(User user) {
        return VerificationToken.builder().
                token(UUID.randomUUID().toString()).user(user).validTill(Instant.now().plusSeconds(60 * 20)).build();
    }
}
