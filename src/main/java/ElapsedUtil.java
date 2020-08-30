import java.util.logging.Logger;

public class ElapsedUtil {
    private final static Logger LOGGER = Logger.getLogger(ElapsedUtil.class.getName());

    private static double startTime = 0.0;
    private static double endTime = 0.0;
    private static double elapsedTime = 0.0;

    public static void start() {
        startTime = System.currentTimeMillis();
        LOGGER.info("elapsed time calculator starts at " + startTime);
    }

    public static void end() {
        endTime = System.currentTimeMillis();
        elapsedTime = (endTime - startTime) / 1000.0;
        LOGGER.info("elapsed time calculator ends at " + endTime);
    }

    public static double getElapsedTime() {
        if (startTime == 0.0) {
            LOGGER.warning("Haven't setup the start time");
            return 0.0;
        } else if (endTime == 0.0) {
            LOGGER.warning("Haven't setup the end time");
            return 0.0;
        }

        return elapsedTime;
    }
}
