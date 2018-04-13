import java.sql.*;
import java.text.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class LogMgr{

    public void setSyslog(String path, String msg){
        DecimalFormat gl_date_df = new DecimalFormat("00");
        Calendar currentCalendar = Calendar.getInstance();

        String year = Integer.toString(currentCalendar.get(Calendar.YEAR));
        String month = gl_date_df.format(currentCalendar.get(Calendar.MONTH) + 1);
        String day = gl_date_df.format(currentCalendar.get(Calendar.DATE));

        String hour = "";
        if(currentCalendar.get(Calendar.AM_PM) == Calendar.PM){
            hour = gl_date_df.format(currentCalendar.get(Calendar.HOUR) + 12);
        }else{
            hour = gl_date_df.format(currentCalendar.get(Calendar.HOUR));
        }

        String min = gl_date_df.format(currentCalendar.get(Calendar.MINUTE));
        String sec = gl_date_df.format(currentCalendar.get(Calendar.SECOND));

        String filename = path + "/" + month + "/" + "log_" + year + month + day + ".log";
        File fpath = new File(path + "/" + month);

        fpath.mkdir();

        String strwrite = "[" + year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec + "] [SYSLOG] " + msg + "\r\n";

        try{
            FileWriter filewriter = new FileWriter(filename, true);
            try{
                filewriter.write(strwrite);
            }catch(Exception e1){
                filewriter.close();
            }

            filewriter.close();
        }catch(Exception exception){ }   
    }

}