package com.hobby.daos;

import com.hobby.models.Talk;
import com.hobby.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {
    List<Talk> findAllBySpeakers(User speaker);
    List<Talk> findAllByParticipants(User participant);
}
