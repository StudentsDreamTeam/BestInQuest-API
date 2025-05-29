package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.model.Greeting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GreetingControllerTest {

    @InjectMocks
    private GreetingController greetingController;

    @Test
    void greeting_WithDefaultName_ReturnsCorrectGreeting() {
        // Act
        Greeting result = greetingController.greeting("Ilya");

        // Assert
        assertNotNull(result);
        assertEquals("Hello, Ilya!", result.content());
        assertTrue(result.id() > 0);
    }

    @Test
    void greeting_WithCustomName_ReturnsCorrectGreeting() {
        // Act
        String customName = "John";
        Greeting result = greetingController.greeting(customName);

        // Assert
        assertNotNull(result);
        assertEquals("Hello, John!", result.content());
        assertTrue(result.id() > 0);
    }

    @Test
    void greeting_MultipleRequests_IncrementId() {
        // Act
        Greeting result1 = greetingController.greeting("Test1");
        Greeting result2 = greetingController.greeting("Test2");
        Greeting result3 = greetingController.greeting("Test3");

        // Assert
        assertTrue(result1.id() < result2.id());
        assertTrue(result2.id() < result3.id());
    }

    @Test
    void greeting_WithEmptyName_ReturnsGreeting() {
        // Act
        Greeting result = greetingController.greeting("");

        // Assert
        assertNotNull(result);
        assertEquals("Hello, !", result.content());
        assertTrue(result.id() > 0);
    }

    @Test
    void greeting_WithNullName_ReturnsGreeting() {
        // Act
        Greeting result = greetingController.greeting(null);

        // Assert
        assertNotNull(result);
        assertEquals("Hello, null!", result.content());
        assertTrue(result.id() > 0);
    }
} 