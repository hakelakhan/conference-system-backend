package com.hobby.controllers;

import com.hobby.dtos.TalkRequestDto;
import com.hobby.dtos.TalkCreationResponseDto;
import com.hobby.service.TalkManagementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@RequestMapping("/api/talk-management")
public class TalkManagementController {
    private final TalkManagementService talkManagementService;

    @PostMapping("/new-talk")
    public TalkCreationResponseDto createTalk(@RequestBody TalkRequestDto talkRequestDto) {
        return talkManagementService.createNewTalk(talkRequestDto);
    }

    @PostMapping("/update-talk/{talkId}")
    public TalkCreationResponseDto updateTalk(@PathVariable("talkId") long talkId, @RequestBody TalkRequestDto updatedTalkRequestDto) {
        return talkManagementService.updateTalk(talkId, updatedTalkRequestDto);
    }
}
