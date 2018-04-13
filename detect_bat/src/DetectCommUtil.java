
import java.io.*;
import java.util.*;
import java.util.Date;
import java.text.*;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.util.regex.Pattern;

public class DetectCommUtil{

	String config_file_path =  "D:/detect_bat/config/config.txt";
	String gl_log_path = "D:/detect_bat/log/";
	String url="" ;
	String user_name = "" ;
	String database_name = "";
	String password = "" ;
	String gl_path_root = "" ;
	String folderPath =  "" ;

	public String setMoveFile(String oldfile,String newfile,boolean isdel){
        String strret = "FAIL";

        try{
            InputStream stream = new FileInputStream(new File(oldfile));
            OutputStream bos = new FileOutputStream(newfile);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1){
               bos.write(buffer, 0, bytesRead);
            }
            bos.close();
            stream.close();
            if(isdel == true){
                File f = new File(oldfile);
                f.delete();
            }

            strret = "SUCC";
        }catch(Exception exception){
            System.out.println("[DETECT CommUtil] setMoveFile Exception  : " + exception.getMessage());
        }

        return strret;
    }

	public void setConfig(){

		try{
			File file = new File(config_file_path);
			String line = "";
			FileReader fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);

			while((line = bufReader.readLine()) != null){
				boolean starts_with = line.startsWith("#");
				String arr[] = line.split("=");
				if(arr.length == 1 || arr.length == 0){
					continue;
				}
				if(starts_with){
					new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new DetectCommUtil().getDebugDate("KOR","") +"][DetectCommUtil] setConfig() :: " + "주석:" + line);
					continue;
				}
				String key = arr[0].trim().toLowerCase();
				String value = arr[1].trim().toLowerCase();
				
				if(key.equals("url")){
					url = value;
				}
				if(key.equals("databasename")){
					database_name = value;
				}
				if(key.equals("user_name")){
					user_name = value;
				}
				if(key.equals("password")){
					password = value;
				}
				if(key.equals("gl_path_root")){
					gl_path_root = value;
				}
				if(key.equals("folderpath")){
					folderPath = value;
				}
				new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new DetectCommUtil().getDebugDate("KOR","") +"][DetectCommUtil] setConfig() :: " + key + ":" + value );
			}
			bufReader.close();

			url += "databasename=" + database_name ;

		}catch(Exception e0){
			new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new DetectCommUtil().getDebugDate("KOR","") +"][DetectCommUtil] setConfig() :: " + e0.getMessage());
		}
	}

	public void setResult(String check_file_path,String result){
		String gl_log_path = "D:/detect_bat/log/";
		try{
			
			File file = new File(check_file_path);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

			if(file.isFile() && file.canWrite()){
				bufferedWriter.write(result);
				bufferedWriter.close();
			}

		}catch(Exception e0){
			new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new DetectCommUtil().getDebugDate("KOR","") +"][DetectCommUtil] setResult() :: " + e0.getMessage());
		}
	}
	  
}
