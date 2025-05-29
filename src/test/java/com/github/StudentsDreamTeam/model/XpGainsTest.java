package com.github.StudentsDreamTeam.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class XpGainsTest {

    @Test
    void testXpGainsCreation() {
        XpGains xpGains = new XpGains();
        assertNotNull(xpGains);
    }

    @Test
    void testXpGainsSettersAndGetters() {
        XpGains xpGains = new XpGains();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        xpGains.setId(1);
        xpGains.setAmount(100);
        xpGains.setDate(now);
        xpGains.setUser(user);

        assertEquals(1, xpGains.getId());
        assertEquals(100, xpGains.getAmount());
        assertEquals(now, xpGains.getDate());
        assertEquals(user, xpGains.getUser());
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        
        XpGains xpGains = new XpGains(user, 100, now);

        assertEquals(user, xpGains.getUser());
        assertEquals(100, xpGains.getAmount());
        assertEquals(now, xpGains.getDate());
    }

    @Test
    void testNullableFields() {
        XpGains xpGains = new XpGains();
        
        // amount и date могут быть null
        xpGains.setAmount(null);
        xpGains.setDate(null);

        assertNull(xpGains.getAmount());
        assertNull(xpGains.getDate());
    }
} 