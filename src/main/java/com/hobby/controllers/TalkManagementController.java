package com.hobby.controllers;

import com.hobby.dtos.TalkDetailsResponseDto;
import com.hobby.dtos.TalkRequestDto;
import com.hobby.dtos.TalkCreationResponseDto;
import com.hobby.dtos.TalksDetails;
import com.hobby.service.TalkManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("/api/talk-management")
@RestController
public class TalkManagementController {
    private final TalkManagementService talkManagementService;

    @PostMapping("/new-talk")
    public TalkCreationResponseDto createTalk(@RequestBody TalkRequestDto talkRequestDto) {
        return talkManagementService.createNewTalk(talkRequestDto);
    }

    @GetMapping("/talks")
    public TalksDetails getAllTalks() {
        return talkManagementService.getAllTalks();
    }

    @GetMapping("/talks/{talkId}")
    public TalkDetailsResponseDto getTalk(@PathVariable("talkId") long talkId) {
        TalkDetailsResponseDto details = talkManagementService.getTalk(talkId);
        return details;
    }

    @GetMapping("/talks/byspeaker")
    public TalksDetails getTalkForSpeaker(@RequestParam("speaker-email") String email) {
        return talkManagementService.getAllTalksForASpeaker(email);
    }

    @GetMapping("/talks/byparticipant")
    public TalksDetails getTalkForParticipant(@RequestParam("participant-email") String email) {
        return talkManagementService.getAllTalksForAParticipant(email);
    }

    @PostMapping("/update-talk/{talkId}")
    public TalkCreationResponseDto updateTalk(@PathVariable("talkId") long talkId, @RequestBody TalkRequestDto updatedTalkRequestDto) {
        return talkManagementService.updateTalk(talkId, updatedTalkRequestDto);
    }

    @PostMapping("/update-talk/add-speaker/{talkId}")
    public TalkCreationResponseDto addSpeaker(@PathVariable("talkId") long talkId, @RequestParam("speaker-email") String email) {
        talkManagementService.addSpeakerToTalk(talkId, email);
        return TalkCreationResponseDto.builder().talkId(talkId).build();

    }

    @PostMapping("/update-talk/remove-speaker/{talkId}")
    public TalkCreationResponseDto removeSpeaker(@RequestParam("speaker-email") String email) {
        return null;

    }
    @PostMapping("/update-talk/remove-participant/{talkId}")
    public TalkCreationResponseDto removeParticipant(@RequestParam("participant-email") String email) {
        return null;

    }

    @PostMapping("/update-talk/add-participant/{talkId}")
    public TalkCreationResponseDto addParticipant(@PathVariable("talkId") long talkId, @RequestParam("participant-email") String email) {
        talkManagementService.addParticipantToTalk(talkId, email);
        return TalkCreationResponseDto.builder().talkId(talkId).build();
    }
}
