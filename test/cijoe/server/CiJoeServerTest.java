package cijoe.server;

import org.junit.Test;

import java.io.IOException;

public class CiJoeServerTest {

    @Test
    public void create() throws IOException {
        CiJoeServer.main(null);
        //otherwise test does not wait
        while (true) ;
    }
}