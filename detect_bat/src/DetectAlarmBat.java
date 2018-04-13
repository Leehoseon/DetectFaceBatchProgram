import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.*;
import java.util.*;

public class DetectAlarmBat  {

//	static String check_1 = "End";
//	static String url="jdbc:sqlserver://localhost:1433;DataBaseName=facedetect" ;
//	static String user_name = "detect" ;
//	static String password = "thdxlawkd" ;
//	static String gl_path_root = "D:/detect_bat" ;
//	static String folderPath = gl_path_root + "/zface_files/tmp/" ;

	static String gl_log_path = "D:/detect_bat/log/";
	static String url="" ;
	static String user_name = "" ;
	static String database_name = "";
	static String password = "" ;
	static String gl_path_root = "" ;
	static String folderPath =  "" ;
	Detect detect = new Detect();

	public static void main(String[] args) {
		DetectAlarmBat batdo = new DetectAlarmBat();
		DetectCommUtil cutil = new DetectCommUtil();
		cutil.setConfig();
		url = cutil.url;
		user_name = cutil.user_name;
		database_name = cutil.database_name;
		password = cutil.password;
		gl_path_root = cutil.gl_path_root;
		folderPath = cutil.folderPath;
		batdo.setWorkInit();
		new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] DetectAlarmBat start :: " );

	}

	public void setWorkInit(){
        String con_result = "";
        Timer timer1 = new Timer();
		Timer timer2 = new Timer();
		Timer timer3 = new Timer();
		Timer timer4 = new Timer();
		Timer timer5 = new Timer();
		Timer timer6 = new Timer();
		Timer timer7 = new Timer();
		Timer timer8 = new Timer();
		Timer timer9 = new Timer();
		Timer timer10 = new Timer();

        timer1.schedule(new setBatchWork1(), 2000, 5000);
		timer2.schedule(new setBatchWork2(), 2000, 5000);
		timer3.schedule(new setBatchWork3(), 2000, 5000);
		timer4.schedule(new setBatchWork4(), 2000, 5000);
		timer5.schedule(new setBatchWork5(), 2000, 5000);
		timer6.schedule(new setBatchWork6(), 2000, 5000);
		timer7.schedule(new setBatchWork7(), 2000, 5000);
		timer8.schedule(new setBatchWork8(), 2000, 5000);
		timer9.schedule(new setBatchWork9(), 2000, 5000);
		timer10.schedule(new setBatchWork10(), 2000, 5000);

    }

	public class setBatchWork1 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(1,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat]  setBatchWork1 RUN()  :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork2 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(2,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat]  setBatchWork2 RUN()  :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork3 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(3,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] setBatchWork3 RUN()  :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork4 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(4,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] setBatchWork4 RUN()   :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork5 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(5,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] setBatchWork5 RUN()   :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork6 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(6,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] setBatchWork6 RUN()   :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork7 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(7,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] setBatchWork7 RUN()   :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork8 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(8,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] setBatchWork8 RUN()   :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork9 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(9,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] setBatchWork9 RUN()   :: " + e0.getMessage());
            }
        }
    }

	public class setBatchWork10 extends TimerTask{
		String retstr = "";

        @Override
        public void run(){
            try{

				retstr = detect.setDetectAlarm(10,url,user_name,database_name,password,gl_path_root,folderPath,gl_log_path);

            }catch(Exception e0){

				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DetectAlarmBat] setBatchWork10 RUN()   :: " + e0.getMessage());
            }
        }
    }

	
}

