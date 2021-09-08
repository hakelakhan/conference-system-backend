package com.hobby.service;

import com.hobby.daos.TalkRepository;
import com.hobby.dtos.TalkRequestDto;
import com.hobby.dtos.TalkCreationResponseDto;
import com.hobby.enuns.TalkStatus;
import com.hobby.models.Talk;
import com.hobby.models.User;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class TalkManagementService {

    private final TalkRepository talkDao;

    private final UserDetailsServiceImpl userDetailsService;

    //todo make this  throw checked exception
    public TalkCreationResponseDto createNewTalk(TalkRequestDto talkRequestDto) {
        Talk.TalkBuilder builder = getTalkBuilder(talkRequestDto);

        Talk newTalk = builder.build();
        assignOrganizer(newTalk);
        assignSpeakers(newTalk, talkRequestDto.getSpeakerEmailIds());
        talkDao.save(newTalk);
        return TalkCreationResponseDto.builder().talkId(newTalk.getTalkId()).build();
    }

    private void assignOrganizer(Talk talk) {
        talk.setOrganizedBy(userDetailsService.getActiveUser());
    }

    public void assignSpeakers(Talk talk, List<String> emailIds) {
        Set<User> speakers = userDetailsService.getCollectionOfRegisteredUsers(emailIds);
        talk.setSpeakers(speakers);
    }

    private Talk.TalkBuilder getTalkBuilder(TalkRequestDto talkRequestDto) {
        return Talk.builder()
                .topic(talkRequestDto.getTopic())
                .description(talkRequestDto.getDescription())
                .startTime(talkRequestDto.getStartTime())
                .endTime(talkRequestDto.getEndTime())
                .status(TalkStatus.CREATED);
    }

    public TalkCreationResponseDto updateTalk(long talkId, TalkRequestDto updatedTalkRequestDto) {
        Optional<Talk> talkById = talkDao.findById(talkId);
        Talk talk = talkById.orElseThrow(() -> new RuntimeException("Could not find talk with talkd id" + talkId));
        if(Strings.isNotBlank(updatedTalkRequestDto.getTopic())) {
            talk.setTopic(updatedTalkRequestDto.getTopic());
        }
        if(Strings.isNotBlank(updatedTalkRequestDto.getDescription())) {
            talk.setDescription(updatedTalkRequestDto.getDescription());
        }
        if(updatedTalkRequestDto.getStartTime() != null) {
            talk.setStartTime(updatedTalkRequestDto.getStartTime());
        }
        if(updatedTalkRequestDto.getEndTime() != null) {
            talk.setEndTime(updatedTalkRequestDto.getEndTime());
        }
        if(!Collections.isEmpty(updatedTalkRequestDto.getSpeakerEmailIds())) {
            assignSpeakers(talk, updatedTalkRequestDto.getSpeakerEmailIds());
        }
        assignOrganizer(talk);
        talkDao.save(talk);
        return TalkCreationResponseDto.builder().talkId(talk.getTalkId()).build();
    }
}
