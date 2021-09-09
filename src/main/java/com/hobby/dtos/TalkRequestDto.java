package com.hobby.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class TalkRequestDto {
    private String topic;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> speakerEmailIds;
    private List<String> participantEmailIds;
}
