import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities
{
    // za staticke funkcije

    public static String[] splitStringWithSeparator(String inputString, String separator)
    {
        return inputString.split(separator);

    }

    public static String getCurrentDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'u' HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);

    }

}
