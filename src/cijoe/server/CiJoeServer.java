package cijoe.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class CiJoeServer {
    public CiJoeServer(int port) throws IOException {
        __server = create(port);
    }

    public CiJoeServer() throws IOException {
        this(__PORT);
    }

    private void __start() {
        __server.setExecutor(new __Executor());
        __server.start();
    }

    private HttpServer create(int port) throws IOException {
        InetSocketAddress sockAddr = new InetSocketAddress(port);
        return HttpServer.create(sockAddr, __BACKLOG);
    }

    private static class __Executor implements Executor {

        @Override
        public void execute(Runnable command) {
            throw new RuntimeException("Not implemented yet");
        }
    }

    private final HttpServer  __server;
    private static final int __BACKLOG = 1024;
    private static final int __PORT = 3000;
}
