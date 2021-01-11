package uady.mx.nube.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String getFormattedDate(Date fecha){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = formatter.format(fecha);
        return date;
    }
    
    public static Date getDateObject(String fecha) throws ParseException{
        String pattern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(fecha);

        return date;
    }
}
