package cijoe;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * Manage log(s) across application.
 * Thanx: https://www.vogella.com/tutorials/Logging/article.html
 */
public class AppLogger {

    public AppLogger(String fname, boolean append) throws IOException {
        __logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        __handler = new FileHandler(fname, append);
        __initialize();
    }

    private void __initialize() {
        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
    }

    private final Logger __logger;
    private final FileHandler __handler;
}
