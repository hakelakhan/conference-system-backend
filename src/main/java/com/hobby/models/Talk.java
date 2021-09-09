package com.hobby.models;

import com.hobby.enuns.TalkStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Talk {
    @Id
    @GeneratedValue
    private long talkId;

    private String topic;

    @Column(length = 5000)
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany
    private Set<User> speakers;

    @OneToMany
    private Set<User> participants;

    private TalkStatus status;

    private String presentationFileName;

    @OneToOne
    private User organizedBy;
}
