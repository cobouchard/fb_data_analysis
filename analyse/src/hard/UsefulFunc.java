package hard;

import java.sql.Timestamp;
import java.time.LocalDate;

public abstract class UsefulFunc {
    public static LocalDate timestampToDate(long timestamp)
    {
        Timestamp foobar = new Timestamp(timestamp);
        return foobar.toLocalDateTime().toLocalDate();
    }

    public static void main(String[] args) {
        System.out.println(timestampToDate(1570282962054L));
    }
}
