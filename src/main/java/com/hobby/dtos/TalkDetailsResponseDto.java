package com.hobby.dtos;

import com.hobby.enuns.TalkStatus;
import com.hobby.models.User;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class TalkDetailsResponseDto {
    private long talkId;
    private String topic;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<String> speakers;
    private Set<String> participants;
    private String status;
    private String presentationFileName;
    private String organizedBy;
}
