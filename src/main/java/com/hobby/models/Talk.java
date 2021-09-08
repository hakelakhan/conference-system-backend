package com.hobby.models;

import com.hobby.enuns.TalkStatus;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
public class Talk {
    @Id
    @GeneratedValue
    private long talkId;

    private String topic;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany
    private Set<User> speakers;

    private TalkStatus status;

    private String presentationFileName;

    @OneToOne
    private User organizedBy;
}
