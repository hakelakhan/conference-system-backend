package com.hobby.service;

import com.hobby.daos.TalkRepository;
import com.hobby.dtos.TalkDetailsResponseDto;
import com.hobby.dtos.TalkRequestDto;
import com.hobby.dtos.TalkCreationResponseDto;
import com.hobby.dtos.TalksDetails;
import com.hobby.enuns.TalkStatus;
import com.hobby.models.Talk;
import com.hobby.models.User;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class TalkManagementService {

    private final TalkRepository talkDao;

    private final UserDetailsServiceImpl userDetailsService;

    public TalksDetails getAllTalksForASpeaker(String speakerEmailId) {
        User speaker = getUserFromEmailId(speakerEmailId);
        List<Talk> allBySpeakers = talkDao.findAllBySpeakers(speaker);

        List<TalkDetailsResponseDto> detailsAboutTalk = new ArrayList<>();

        allBySpeakers.forEach(talk -> detailsAboutTalk.add(talkToDto(talk)));
        return TalksDetails.builder().talks(detailsAboutTalk).build();
    }

    public TalksDetails getAllTalksForAParticipant(String participantEmailId) {
        User speaker = getUserFromEmailId(participantEmailId);
        List<Talk> allByParticipants = talkDao.findAllByParticipants(speaker);

        List<TalkDetailsResponseDto> detailsAboutTalk = new ArrayList<>();

        allByParticipants.forEach(talk -> detailsAboutTalk.add(talkToDto(talk)));
        return TalksDetails.builder().talks(detailsAboutTalk).build();
    }

    public TalkDetailsResponseDto getTalk(long talkId) {
        TalkDetailsResponseDto response;
        Talk talk = talkDao.findById(talkId).orElse(null);
        return talkToDto(talk);
    }

    public TalksDetails getAllTalks() {
        List<TalkDetailsResponseDto> detailsAboutTalk = new ArrayList<>();

        List<Talk> allTalks = talkDao.findAll();
        allTalks.forEach(talk -> detailsAboutTalk.add(talkToDto(talk)));
        return TalksDetails.builder().talks(detailsAboutTalk).build();
    }

    private TalkDetailsResponseDto talkToDto(Talk talk) {
        if(talk == null)
            return null;
        return TalkDetailsResponseDto.builder()
                .talkId(talk.getTalkId())
                .topic(talk.getTopic())
                .description(talk.getDescription())
                .startTime(talk.getStartTime())
                .endTime(talk.getEndTime())
                .status(talk.getStatus().toString())
                .speakers(talk.getSpeakers().stream().map(User::getEmail).collect(Collectors.toSet()))
                .participants(talk.getParticipants().stream().map(User::getEmail).collect(Collectors.toSet()))
                .organizedBy(talk.getOrganizedBy().getEmail())
                .presentationFileName(talk.getPresentationFileName())
                .build();
    }

    //todo make this  throw checked exception
    public TalkCreationResponseDto createNewTalk(TalkRequestDto talkRequestDto) {
        Talk.TalkBuilder builder = getTalkBuilder(talkRequestDto);

        Talk newTalk = builder.build();
        assignOrganizer(newTalk);
        assignSpeakers(newTalk, talkRequestDto.getSpeakerEmailIds());
        assignParticipants(newTalk, talkRequestDto.getParticipantEmailIds());
        talkDao.save(newTalk);
        return TalkCreationResponseDto.builder().talkId(newTalk.getTalkId()).build();
    }

    private void assignOrganizer(Talk talk) {
        talk.setOrganizedBy(userDetailsService.getActiveUser());
        talkDao.save(talk);
    }

    public void assignParticipants(Talk talk, List<String> emailIds) {
        Set<User> participants = userDetailsService.getUsersRegisteredWithEmails(emailIds);
        talk.setParticipants(participants);
        talkDao.save(talk);
    }

    public void assignSpeakers(Talk talk, List<String> emailIds) {
        Set<User> speakers = userDetailsService.getUsersRegisteredWithEmails(emailIds);
        talk.setSpeakers(speakers);
        talkDao.save(talk);
    }

    public void addSpeakerToTalk(long talkId, String emailId) {
        Talk talk = talkDao.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Did not found a talk with talkId " + talkId ));
        addSpeakerToTalk(talk, emailId);
    }

    public void addSpeakerToTalk(Talk talk, String emailId) {
        User speaker = getUserFromEmailId(emailId);
        talk.getSpeakers().add(speaker);
        talkDao.save(talk);
    }

    public void removeSpeakerFromTalk(Talk talk, String emailId) {
        User speaker = getUserFromEmailId(emailId);
        talk.getSpeakers().remove(speaker);
        talkDao.save(talk);
    }

    public void addParticipantToTalk(long talkId, String emailId) {
        Talk talk = talkDao.findById(talkId).orElseThrow(() -> new IllegalArgumentException("Did not found a talk with talkId " + talkId ));
        addParticipantToTalk(talk, emailId);
    }
    public void addParticipantToTalk(Talk talk, String emailId) {
        User participant = getUserFromEmailId(emailId);
        talk.getParticipants().add(participant);
        talkDao.save(talk);
    }

    public void removeParticipantFromTalk(Talk talk, String emailId) {
        User participant = getUserFromEmailId(emailId);
        talk.getParticipants().remove(participant);
        talkDao.save(talk);
    }

    public User getUserFromEmailId(String emailId) {
        return userDetailsService.getUserRegisteredWithEmail(emailId);
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
        if(!Collections.isEmpty(updatedTalkRequestDto.getParticipantEmailIds())) {
            assignParticipants(talk, updatedTalkRequestDto.getParticipantEmailIds());
        }
        assignOrganizer(talk);
        talkDao.save(talk);
        return TalkCreationResponseDto.builder().talkId(talk.getTalkId()).build();
    }
}
