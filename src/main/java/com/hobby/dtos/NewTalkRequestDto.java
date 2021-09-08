package com.hobby.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class NewTalkRequestDto {
    private String topic;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<String> speakerEmailIds;
}
