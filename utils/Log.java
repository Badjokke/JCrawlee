import java.time.LocalDateTime;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Log {
    private static Logger logger = Logger.getLogger(Log.class.getName());
    public Log(){
        logger.addHandler(new ConsoleHandler());
    }

    /**
     *
     * Method logs message with level and timestamp into terminal
     * ie INFO "hello, info"
     * @param level Level - log level
     * @param message String - message to logged
     */
    public static void log(Level level, String message){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        logger.log(level,hour+":"+minute+":"+second+":"+year+"."+day+"."+month+" "+message+"\n");
    }

}
