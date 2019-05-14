package cijoe.server;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static cijoe.Util.invariant;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Singleton of Server side properties.
 */
public class Configuration {
    private static Configuration __THE_ONE;

    private Configuration(String fname) throws IOException {
        super();
        __initialize(fname);
    }

    private void __initialize(String fname) throws IOException {
        invariant(isNull(__THE_ONE), "Cannot instance another singleton");
        __THE_ONE = this;
        __fileName = fname;
        __properties = new Properties();
        __properties.load(new FileReader(fname));
    }

    public static void create(String fname) throws IOException {
        new Configuration(fname);
    }

    public static String getProperty(String key) {
        invariant(hasProperty(key), "No value for key: "+key);
        String value = __THE_ONE.__properties.getProperty(key);
        invariant(nonNull(value));
        return value;
    }

    public static boolean hasProperty(String key) {
        return __THE_ONE.__properties.containsKey(key);
    }

    private String __fileName;
    private Properties __properties;
}
