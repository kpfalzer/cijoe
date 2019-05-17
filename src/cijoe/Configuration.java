package cijoe;

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

    private Configuration() throws IOException {
        this(null);
    }

    private Configuration(String fname) throws IOException {
        __initialize(fname);
    }

    private void __initialize(String fname) throws IOException {
        invariant(isNull(__THE_ONE), "Cannot instance another singleton");
        __THE_ONE = this;
        __fileName = fname;
        __properties = __getDefaults();
        if (nonNull(fname)) {
            __properties.load(new FileReader(fname));
        }
        __DEFAULTS
                .keySet()
                .forEach(k -> invariant(__properties.containsKey(k)));
    }

    public static void create(String fname) throws IOException {
        new Configuration(fname);
    }

    public static String getProperty(String key) {
        invariant(hasProperty(key), "No value for key: " + key);
        String value = __getTheOne().__properties.getProperty(key);
        invariant(nonNull(value));
        return value;
    }

    public static boolean hasProperty(String key) {
        return __getTheOne().__properties.containsKey(key);
    }

    private static Configuration __getTheOne() {
        if (isNull(__THE_ONE)) {
            try {
                new Configuration();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return __THE_ONE;
    }

    private String __fileName;
    private Properties __properties;

    private static final Properties __DEFAULTS = new Properties();
    private static Properties __USE_DEFAULTS = null;

    static {
        __DEFAULTS.setProperty("root", "/var/lib/cijoe");
        __DEFAULTS.setProperty("logFname", "/var/lib/cijoe/cijoe.log");
    }

    private static Properties __getDefaults() throws IOException {
        String fname = System.getProperty("defaultPropertyFname");
        Properties defaults = new Properties();
        if (nonNull(fname)) {
            defaults.load(new FileReader(fname));
        } else {
            defaults.putAll(__DEFAULTS);
        }
        return defaults;
    }
}
