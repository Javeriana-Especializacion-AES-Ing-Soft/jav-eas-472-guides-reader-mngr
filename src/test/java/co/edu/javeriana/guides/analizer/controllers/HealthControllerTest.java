package co.edu.javeriana.guides.analizer.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void healthTest() {
        String healthy = healthController.health();
        Assertions.assertEquals("OK...", healthy);
    }

}
