package sparkler.utils;

import java.util.logging.Logger;

public class Log {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void info(String key, String message) {
        logger.info(key +  " : " + message);
    }

    public static void warn(String key, String message) {
        logger.warning(key +  " : " + message);
    }
}
