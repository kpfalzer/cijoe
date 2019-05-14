package cijoe.server;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationTest {

    @Test
    public void create() {
        //todo
    }

    @Test
    public void getProperty() {
        assertNotNull(Configuration.getProperty("root"));
    }

    @Test
    public void hasProperty() {
        assertTrue(Configuration.hasProperty("root"));
    }
}