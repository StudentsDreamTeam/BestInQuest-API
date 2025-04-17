package com.spd.service;

import com.spd.model.Clan;
import com.spd.model.User;
import com.spd.repository.ClanRepository;
import com.spd.repository.UserRepository;
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
            .orElseThrow(() -> new RuntimeException("User not found"));

        clan.setLeader(leader);
        return clanRepository.save(clan);
    }

    public Clan getClanInfo(Long id) {
        return clanRepository.findById(id).orElseThrow(() -> new RuntimeException("Clan not found"));
    }
}
