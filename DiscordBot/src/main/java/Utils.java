
import org.apache.commons.lang3.time.DurationFormatUtils;

public abstract class Utils {
    public static String DurationFormatLong(long Duration){
        return DurationFormatUtils.formatDuration(Duration,"HH:mm:ss");
    }
}
