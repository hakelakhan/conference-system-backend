package com.hobby.service;

import com.hobby.daos.TalkRepository;
import com.hobby.dtos.NewTalkRequestDto;
import com.hobby.dtos.TalkCreationResponseDto;
import com.hobby.enuns.TalkStatus;
import com.hobby.models.Talk;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TalkManagementService {

    private final TalkRepository talkDao;

    //todo make this  throw checked exception
    public TalkCreationResponseDto createNewTalk(NewTalkRequestDto newTalkRequestDto) {
        Talk.TalkBuilder builder = getTalkBuilder(newTalkRequestDto);
        //assignSpeakers(builder, newTalkRequestDto.getSpeakerEmailIds())
        //assignOrganizer(builder);
        Talk newTalk = builder.build();
        talkDao.save(newTalk);
        return TalkCreationResponseDto.builder().talkId(newTalk.getTalkId()).build();
    }

    private Talk.TalkBuilder getTalkBuilder(NewTalkRequestDto newTalkRequestDto) {
        return Talk.builder()
                .topic(newTalkRequestDto.getTopic())
                .description(newTalkRequestDto.getDescription())
                .startTime(newTalkRequestDto.getStartTime())
                .endTime(newTalkRequestDto.getEndTime())
                .status(TalkStatus.CREATED);
    }

    public TalkCreationResponseDto updateTalk(long talkId, NewTalkRequestDto updatedTalkRequestDto) {
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
        /* todo set speakers and organizer
        if(!Collections.isEmpty(updatedTalkRequestDto.getSpeakerEmailIds())) {
            talk.setSpeakers(updatedTalkRequestDto.getSpeakerEmailIds());
        }

         */
        talkDao.save(talk);
        return TalkCreationResponseDto.builder().talkId(talk.getTalkId()).build();
    }
}
