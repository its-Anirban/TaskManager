package com.example.taskManager.unit.config;

import com.example.taskManager.config.JwtProperties;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtPropertiesTest {

    @Test
    void shouldSetAndGetAllJwtProperties() {
        JwtProperties props = new JwtProperties();
        props.setSecret("testSecret");
        props.setExpirationMs(3600000L);
        props.setIssuer("TestIssuer");

        assertEquals("testSecret", props.getSecret());
        assertEquals(3600000L, props.getExpirationMs());
        assertEquals("TestIssuer", props.getIssuer());
    }
}
