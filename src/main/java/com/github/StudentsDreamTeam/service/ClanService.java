package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.Clan;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.ClanRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClanService {

    @Autowired
    private ClanRepository clanRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Clan createClan(Clan clan, Long userId) {
        User leader = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        clan.setLeader(leader);
        return clanRepository.save(clan);
    }

    public Clan getClanInfo(Long id) {
        return clanRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Clan not found"));
    }
}
