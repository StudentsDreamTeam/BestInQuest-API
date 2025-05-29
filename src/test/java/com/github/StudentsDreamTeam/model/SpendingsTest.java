package com.github.StudentsDreamTeam.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SpendingsTest {

    @Test
    void testSpendingsCreation() {
        Spendings spendings = new Spendings();
        assertNotNull(spendings);
    }

    @Test
    void testSpendingsSettersAndGetters() {
        Spendings spendings = new Spendings();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        spendings.setId(1);
        spendings.setUser(user);
        spendings.setAmount(100);
        spendings.setDate(now);
        spendings.setDescription("Test spending");

        assertEquals(1, spendings.getId());
        assertEquals(user, spendings.getUser());
        assertEquals(100, spendings.getAmount());
        assertEquals(now, spendings.getDate());
        assertEquals("Test spending", spendings.getDescription());
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        String description = "Test spending";
        
        Spendings spendings = new Spendings(user, 100, now, description);

        assertEquals(user, spendings.getUser());
        assertEquals(100, spendings.getAmount());
        assertEquals(now, spendings.getDate());
        assertEquals(description, spendings.getDescription());
    }

    @Test
    void testNullableFields() {
        Spendings spendings = new Spendings();
        
        // amount, date и description могут быть null
        spendings.setAmount(null);
        spendings.setDate(null);
        spendings.setDescription(null);

        assertNull(spendings.getAmount());
        assertNull(spendings.getDate());
        assertNull(spendings.getDescription());

        // user не может быть null (проверять не нужно, это обеспечивается на уровне БД)
    }
} 