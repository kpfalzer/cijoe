package cijoe.server;

import cijoe.Util;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static java.util.Objects.nonNull;

public class CiJoeServer {
    public CiJoeServer(InetAddress addr, int port, Map<String, HttpHandler> handlers) throws IOException {
        __server = create(addr, port);
        handlers.forEach((k, v) -> {
            __server.createContext(k, v);
        });
        __start();
    }

    public static CiJoeServer create(String addr, String port, Map<String, HttpHandler> handlers) throws IOException {
        byte haddr[] = Util.toByte(Arrays
                .stream(addr.split("\\."))
                .map(Integer::parseInt)
                .toArray(Integer[]::new));
        InetAddress iaddr = InetAddress.getByAddress(haddr);
        Integer hport = Integer.parseInt(port);
        return new CiJoeServer(iaddr, hport, handlers);
    }

    public static CiJoeServer create(String addr, String port) throws IOException {
        return create(addr, port, __DEFAULT_HANDLERS);
    }

    public CiJoeServer(InetAddress addr, int port) throws IOException {
        this(addr, port, __DEFAULT_HANDLERS);
    }

    public CiJoeServer() throws IOException {
        this(null, __PORT, __DEFAULT_HANDLERS);
    }

    private void __start() {
        __server.setExecutor(new __Executor());
        __server.start();
    }

    private HttpServer create(InetAddress addr, int port) throws IOException {
        InetSocketAddress sockAddr = new InetSocketAddress(addr, port);
        return HttpServer.create(sockAddr, __BACKLOG);
    }

    private static class __Executor implements Executor {

        @Override
        public void execute(Runnable command) {
            //todo: need to track these...
            new Thread(command).start();
        }
    }

    private final HttpServer __server;
    private static final int __BACKLOG = 0;
    private static final int __PORT = 3000;

    static private class __MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "\n"
                    + "********************\n"
                    + "This is the response\n"
                    + "********************\n\n";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("Begin wait...");
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End wait...");
        }
    }

    static private final Map<String, HttpHandler> __DEFAULT_HANDLERS = new HashMap<>();

    static {
        __DEFAULT_HANDLERS.put("/", new __MyHandler());
    }

    public static void main(String[] argv) throws IOException {
        if (nonNull(argv) && 2 == argv.length) {
            CiJoeServer server = CiJoeServer.create(argv[0], argv[1]);
        } else {
            CiJoeServer server = new CiJoeServer();
        }
    }
}
