package cijoe.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static cijoe.Util.invariant;

/*
todo: https://technology.amis.nl/2015/05/12/make-http-post-request-from-java-se-no-frills-no-libraries-just-plain-java/
 */
public class RunCmdHandler implements HttpHandler {

    private RunCmdHandler() {
    }

    public static final RunCmdHandler THE_ONE = new RunCmdHandler();

    @Override
    public void handle(HttpExchange t) throws IOException {
        String cmd = new BufferedReader(new InputStreamReader(t.getRequestBody())).readLine();
        int exitCode = -1;
        try {
            exitCode = runCommand(cmd);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String response = "\n"
                + "********************\n"
                + "RunCmdHandler       \n"
                + ">>>" + cmd + "<<< returns: " + exitCode + "\n"
                + "This is the response\n"
                + "********************\n\n";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        ;
    }

    public static class StreamGobbler implements Runnable {
        public StreamGobbler(InputStream from, Consumer<String> to) {
            __reader = new BufferedReader(new InputStreamReader(from));
            __writer = to;
        }

        private final BufferedReader __reader;
        private final Consumer<String> __writer;

        @Override
        public void run() {
            __reader.lines().forEach(__writer);
        }
    }

    private static int runCommand(String cmd) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd.split("\\s+"));
        Process proc = pb.start();
        Thread cout = new Thread(new StreamGobbler(proc.getInputStream(), System.out::println));
        Thread cerr = new Thread(new StreamGobbler(proc.getErrorStream(), System.err::println));
        cout.start();
        cerr.start();
        proc.waitFor();
        return proc.exitValue();
    }

    public static void main(String[] argv) throws IOException {
        invariant(2 == argv.length, "Usage: ip1.2.3.4 port");
        Map<String, HttpHandler> handler = new HashMap<>();
        handler.put("/execute", THE_ONE);
        CiJoeServer.create(argv[0], argv[1], handler);
    }

}
