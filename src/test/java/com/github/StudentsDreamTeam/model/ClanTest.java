package com.github.StudentsDreamTeam.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClanTest {

    @Test
    void testClanCreation() {
        Clan clan = new Clan();
        assertNotNull(clan);
    }

    @Test
    void testClanSettersAndGetters() {
        Clan clan = new Clan();
        User leader = new User();
        Project project = new Project();
        LocalDateTime now = LocalDateTime.now();

        clan.setId(1);
        clan.setName("Test Clan");
        clan.setLeader(leader);
        clan.setCreationDate(now);
        clan.setRating(100);
        clan.setProject(project);

        assertEquals(1, clan.getId());
        assertEquals("Test Clan", clan.getName());
        assertEquals(leader, clan.getLeader());
        assertEquals(now, clan.getCreationDate());
        assertEquals(100, clan.getRating());
        assertEquals(project, clan.getProject());
    }

    @Test
    void testNullableFields() {
        Clan clan = new Clan();
        
        // Все поля кроме id могут быть null
        clan.setName(null);
        clan.setLeader(null);
        clan.setCreationDate(null);
        clan.setRating(null);
        clan.setProject(null);

        assertNull(clan.getName());
        assertNull(clan.getLeader());
        assertNull(clan.getCreationDate());
        assertNull(clan.getRating());
        assertNull(clan.getProject());
    }
} 