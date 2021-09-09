package com.hobby.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TalksDetails {
    List<TalkDetailsResponseDto> talks;
}
