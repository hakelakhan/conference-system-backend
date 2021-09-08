package com.hobby.models;

import com.hobby.dtos.NewTalkRequestDto;
import com.hobby.enuns.TalkStatus;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    private User organizedBy;
}
