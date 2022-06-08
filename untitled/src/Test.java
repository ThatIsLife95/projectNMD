import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Test {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        formatter = formatter.withLocale(Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse("2020-12-01 00:00:00", formatter);
    }
}
