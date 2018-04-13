import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

import com.amazonaws.*;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AgeRange;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class Detect{
	
	public String setDetectAlarm(int number,String url,String user_name,String database_name,String password,String gl_path_root,String folderPath,String gl_log_path){

		String retstr = "";
		String newFolderPath = gl_path_root + "/zface_files/detect_" + number + "/" ;
		String detectFileName = "" ;
		String detectNewFileName = "" ;
		String oldFilePath = "" ;
		String succFilePath = "" ;
		String newFilePath = "";
		String failFilePath = "" ;
		String check = "";
		String check_file_path = "";
		String check_value = "";

		String faceResult = "";
		String gender = "";
		String ageHigh = "";
		String ageLow = "";
		String real_time = "";
		String kiosk_id = "";

		List <FaceDetail> retList = null;
		List <FaceDetail> faceDetails =null;
		DetectInfo dinfo = new DetectInfo(); 

		DetectCommUtil cutil = new DetectCommUtil();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		StringBuffer fail_query = new StringBuffer();		

		query.append(" INSERT INTO TB_DETECT( ");
		query.append(" KIOSK_ID,FACE_RESULT,FILE_NAME,GENDER,AGE_LOW,AGE_HIGH,DB_STATUS,DETECT_DATE,REG_DATE ");
		query.append(" )");
		query.append(" VALUES( ");
		query.append(" ?,?,?,?,?,?,'A',GETDATE(),? ");
		query.append(" ) ");

		fail_query.append(" INSERT INTO TB_DETECT( ");
		fail_query.append(" KIOSK_ID,FACE_RESULT,REG_DATE ");
		fail_query.append(" )");
		fail_query.append(" VALUES( ");
		fail_query.append(" ?,?,? ");
		fail_query.append(" ) ");

		try{
			//상위 타이머들이 실행중인지 체크
			for(int i=1; i < number; i++){
				String check_path =  "D:/detect_bat/check/check_" + i + ".txt";
				File file = new File(check_path);
				String line = "";
				FileReader fileReader = new FileReader(file);
				BufferedReader bufReader = new BufferedReader(fileReader);

				while((line = bufReader.readLine()) != null){
					if(line.equals("End")){
						return retstr;
					}
				}
				bufReader.close();
			}
			
			check_file_path = "D:/detect_bat/check/check_" + number + ".txt";
			File file = new File(check_file_path);
			String line = "";
			FileReader fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);

			while((line = bufReader.readLine()) != null){
				check = line;
			}
			bufReader.close();

			new LogMgr().setSyslog(gl_log_path + "detectLog/", "CHECK:" + check);

			if(check.equals("Running")){
				new LogMgr().setSyslog(gl_log_path + "detectLog/", "BATCH RETURN");
				return retstr;
			}

			if(check.equals("End")){
				check_value = "Running";
				cutil.setResult(check_file_path,check_value);
			}

		}catch(Exception e0){
			check_value = "End";
			cutil.setResult(check_file_path,check_value);
			new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DETECT] setDetectAlarm() DETECT STATUS CHECK  :: " + e0.getMessage());
		}

		new LogMgr().setSyslog(gl_log_path + "detectLog/", "BATCH_" + number + " START ======================================================");

		File dir_1 = new File(folderPath);
		File[] fileList_1 = dir_1.listFiles();

		try{
			for(int i=0; i<fileList_1.length; i++){
			File file = fileList_1[i];

				if(file.isFile()){
					detectFileName = file.getName();
					oldFilePath = gl_path_root + "/zface_files/tmp/" + detectFileName ;
					newFilePath = gl_path_root + "/zface_files/detect_" + number + "/" + detectFileName ;
					new CommUtil().setMoveFile(oldFilePath,newFilePath,true);
				}
			}
		}catch(Exception e0){
			check_value = "End";
			cutil.setResult(check_file_path,check_value);
			new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DETECT] setDetectAlarm() SEARCH TMP FOLDER :: " + e0.getMessage());							
		}

		try{
			File dir_2 = new File(newFolderPath);
			File[] fileList_2 = dir_2.listFiles(); 

			for(int i=0; i<fileList_2.length; i++){
				File file = fileList_2[i];

				if(file.isFile()){
					detectFileName = file.getName();
					detectNewFileName = "FACE_" + cutil.getDate("","yyyyMMddHHmmss") + ".jpg" ;
					oldFilePath = gl_path_root + "/zface_files/detect_" + number + "/" + detectFileName ;
					succFilePath = gl_path_root + "/zface_files/succ/" + detectNewFileName ;
					failFilePath = gl_path_root + "/zface_files/fail/" + detectNewFileName ;
//					real_time = detectFileName.substring(detectFileName.length()-18,detectFileName.length()-4);
//					kiosk_id = detectFileName.substring(4,8);
					real_time = "100000000";
					kiosk_id = "1000";

					try{
						String bucket = "facerekognitiontest";
						AWSCredentials credentials;
						File s3File = new File(oldFilePath);
						try {
						    credentials = new ProfileCredentialsProvider("D:/01_JAVA_HOME/speech/htdocs/zface_files/leehoseon.aws/test.txt","leehoseon").getCredentials();
						} catch (Exception e) {
						    check_value = "End";
						    cutil.setResult(check_file_path,check_value);
						    new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DETECT] setDetectAlarm() DETECT Provider :: " + e.getMessage());
						    throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
						    + "Please make sure that your credentials file is at the correct "
						    + "location (/Users/userid.aws/credentials), and is in a valid format.", e);
						}

						AmazonS3 s3 = new AmazonS3Client(credentials);
						s3.putObject(new PutObjectRequest(bucket, detectFileName, s3File));
						  
						AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
						    .standard()
						    .withRegion(Regions.US_EAST_1)
						    .withCredentials(new AWSStaticCredentialsProvider(credentials))
						    .build();

						DetectFacesRequest request = new DetectFacesRequest()
						    .withImage(new Image()
						    .withS3Object(new S3Object()
						    .withName(detectFileName)
						    .withBucket(bucket)))
						    .withAttributes(Attribute.ALL);
						    // Replace Attribute.ALL with Attribute.DEFAULT to get default values.

						try {
						    DetectFacesResult result = rekognitionClient.detectFaces(request);
						    faceDetails = result.getFaceDetails();
						    retList = result.getFaceDetails();

						    for (FaceDetail face: faceDetails) {
							 
							    if (request.getAttributes().contains("ALL")) {
							       AgeRange ageRange = face.getAgeRange();
	//						       System.out.println("The detected face is estimated to be between "
	//					  		    + ageRange.getLow().toString() + " and " + ageRange.getHigh().toString()
	//							    + " years old.");
	//						        System.out.println("Here's the complete set of attributes:");
							    } else { // non-default attributes have null values.
	//						        System.out.println("Here's the default set of attributes:");
								}
							}
	//					  System.out.println(faceDetails);

						} catch (AmazonRekognitionException e) {
						    check_value = "End";
						    cutil.setResult(check_file_path,check_value);
						    e.printStackTrace();
						    faceResult ="FAIL";
						}

						for (FaceDetail rere : faceDetails) {
						    ageHigh = rere.getAgeRange().getHigh().toString();
						    ageLow = rere.getAgeRange().getLow().toString();
						    gender = rere.getGender().getValue();
						  
						    dinfo.setFile_name(detectFileName);
						    dinfo.setGender(gender);
						    dinfo.setAgeLow(ageLow);
						    dinfo.setAgeHigh(ageHigh);
						    dinfo.setFile_name(detectFileName);
						    dinfo.setDetect_date(real_time);
						    faceResult = "SUCC";
						}

					}catch(Exception e0){
						check_value = "End";
						cutil.setResult(check_file_path,check_value);
						faceResult ="FAIL";
						new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DETECT] setDetectAlarm() DETECT CONNECT :: " + e0.getMessage());
					}

					int x = 0;

					try{
						
						Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
						conn = DriverManager.getConnection(url,user_name,password);
						conn.setAutoCommit(false);

						try{
							if(faceResult.equals("SUCC")){
								pstmt = conn.prepareStatement(query.toString());
								pstmt.setString(++x,kiosk_id);
								pstmt.setString(++x,faceResult);
								pstmt.setString(++x,detectNewFileName);
								pstmt.setString(++x,dinfo.getGender());
								pstmt.setString(++x,dinfo.getAgeLow());
								pstmt.setString(++x,dinfo.getAgeHigh());
								pstmt.setString(++x,dinfo.getDetect_date());
								pstmt.executeUpdate();
							}
							if(faceResult.equals("FAIL")){
								pstmt = conn.prepareStatement(fail_query.toString());
								pstmt.setString(++x,kiosk_id);
								pstmt.setString(++x,faceResult);
								pstmt.setString(++x,dinfo.getDetect_date());
								pstmt.executeUpdate();
							}

						}catch(Exception e0){
							check_value = "End";
							cutil.setResult(check_file_path,check_value);
							faceResult ="FAIL";
							conn.rollback();
							new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DETECT] setDetectAlarm() sql :: " + e0.getMessage());
						}

						if(faceResult.equals("SUCC")){
							if(!new CommUtil().setMoveFile(oldFilePath,succFilePath,true).equals("SUCC")){
								String msg = "";
								conn.rollback();
							}
						}if(faceResult.equals("FAIL")){
							if(!new CommUtil().setMoveFile(oldFilePath,failFilePath,true).equals("SUCC")){
								String msg = "";
								conn.rollback();
							}
						}
						conn.commit();
						conn.close();
						retstr = "SUCC";
						
					}catch(Exception e0){
						check_value = "End";
						cutil.setResult(check_file_path,check_value);
						new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DETECT] setDetectAlarm() DBConnect :: " + e0.getMessage());							
					}finally{
						if(rs != null){try{rs.close();}catch (Exception SQL2){ System.err.println(SQL2.getMessage());} }
						if(pstmt != null){ try{pstmt.close();}catch (Exception SQL3){ System.err.println(SQL3.getMessage());}}
					}
				}
			}
		}catch(Exception e0){
			check_value = "End";
			cutil.setResult(check_file_path,check_value);
			new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DETECT] setDetectAlarm() FILE AND AWS CONNECT :: " + e0.getMessage());							
		}
		new LogMgr().setSyslog(gl_log_path + "detectLog/", "BATCH" + number + " END   ======================================================");
		
		try{
			check_value = "End";
			cutil.setResult(check_file_path,check_value);
		
		}catch(Exception e0){
			check_value = "End";
			cutil.setResult(check_file_path,check_value);
			new LogMgr().setSyslog(gl_log_path + "detectLog/", "["+ new CommUtil().getDebugDate("KOR","") +"][DETECT] setDetectAlarm() DETECT CHANGE CHECK :: " + e0.getMessage());
		}
		return retstr;
	}
}  

