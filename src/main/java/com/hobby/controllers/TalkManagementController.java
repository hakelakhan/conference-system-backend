package com.hobby.controllers;

import com.hobby.dtos.NewTalkRequestDto;
import com.hobby.dtos.TalkCreationResponseDto;
import com.hobby.service.TalkManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@RequestMapping("/api/talk-management")
public class TalkManagementController {
    private final TalkManagementService talkManagementService;

    public TalkCreationResponseDto createTalk(NewTalkRequestDto newTalkRequestDto) {
        return talkManagementService.createNewTalk(newTalkRequestDto);
    }

    public TalkCreationResponseDto updateTalk(long talkId, NewTalkRequestDto updatedTalkRequestDto) {
        return talkManagementService.updateTalk(talkId, updatedTalkRequestDto);

    }

}
