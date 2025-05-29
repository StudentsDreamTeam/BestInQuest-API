package com.github.StudentsDreamTeam.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IncomeTest {

    @Test
    void testIncomeCreation() {
        Income income = new Income();
        assertNotNull(income);
    }

    @Test
    void testIncomeSettersAndGetters() {
        Income income = new Income();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        income.setId(1);
        income.setUser(user);
        income.setAmount(100);
        income.setDate(now);
        income.setDescription("Test income");

        assertEquals(1, income.getId());
        assertEquals(user, income.getUser());
        assertEquals(100, income.getAmount());
        assertEquals(now, income.getDate());
        assertEquals("Test income", income.getDescription());
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        String description = "Test income";
        
        Income income = new Income(user, 100, now, description);

        assertEquals(user, income.getUser());
        assertEquals(100, income.getAmount());
        assertEquals(now, income.getDate());
        assertEquals(description, income.getDescription());
    }

    @Test
    void testNullableFields() {
        Income income = new Income();
        
        // amount, date и description могут быть null
        income.setAmount(null);
        income.setDate(null);
        income.setDescription(null);

        assertNull(income.getAmount());
        assertNull(income.getDate());
        assertNull(income.getDescription());
    }
} 