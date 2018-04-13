import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.*;
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
import com.amazonaws.services.rekognition.model.Emotion;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class SetDetect3  {

	public static void main(String[] args) {
	
		CommUtil cutil = new CommUtil();

		String url="jdbc:sqlserver://localhost:1433;DataBaseName=facedetect" ;
		String user_name = "detect" ;
		String password = "thdxlawkd" ;

		String gl_path_root = "D:/detect_bat" ;
		String folderPath = gl_path_root + "/zface_files/tmp/" ;
		String detectFileName = "" ;
		String detectNewFileName = "" ;
		String oldFilePath = "" ;
		String succFilePath = "" ;
		String failFilePath = "" ;

		String retstr = "FAIL";
		String faceResult = "";
		String gender = "";
		String ageHigh = "";
		String ageLow = "";

		List <FaceDetail> retList = null;
		List <FaceDetail> faceDetails =null;
		List <Emotion> emotions =null;
		DetectInfo dinfo = new DetectInfo(); 

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		String queryString = "";

		query.append(" INSERT INTO TB_FACE_DETECT( ");
		query.append(" FACE_RESULT,FILE_NAME,GENDER,AGE_LOW,AGE_HIGH,DB_STATUS,DETECT_DATE ");
		query.append(" )");
		query.append(" VALUES( ");
		query.append(" ?,?,?,?,?,'A',GETDATE() ");
		query.append(" ) ");

		try{
			File dir = new File(folderPath);
			File[] fileList = dir.listFiles(); 

			for(int i=0; i<fileList.length; i++){
				File file = fileList[i];

				if(file.isFile()){
					detectFileName = file.getName();
					detectNewFileName = "FACE_" + cutil.getDate("","yyyyMMddHHmmss") + ".jpg" ;
					oldFilePath = gl_path_root + "/zface_files/tmp/" + detectFileName ;
					succFilePath = gl_path_root + "/zface_files/succ/" + detectNewFileName ;
					failFilePath = gl_path_root + "/zface_files/fail/" + detectNewFileName ;

					try{
						String bucket = "facerekognitiontest";
						AWSCredentials credentials;
						File s3File = new File(oldFilePath);
						try {
						  credentials = new ProfileCredentialsProvider("D:/01_JAVA_HOME/speech/htdocs/zface_files/leehoseon.aws/test.txt","leehoseon").getCredentials();
						} catch (Exception e) {
						  System.out.println("DetectMgr ERROR " + e.getMessage());
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
	//						   System.out.println("The detected face is estimated to be between "
	//							+ ageRange.getLow().toString() + " and " + ageRange.getHigh().toString()
	//							+ " years old.");
	//						   System.out.println("Here's the complete set of attributes:");
							} else { // non-default attributes have null values.
	//						   System.out.println("Here's the default set of attributes:");
							}
						  }
						  System.out.println(faceDetails);

						} catch (AmazonRekognitionException e) {
						  e.printStackTrace();
						  faceResult ="FAIL";
						}

						for (FaceDetail rere : faceDetails) {
	//					  System.out.println("input1");
						  ageHigh = rere.getAgeRange().getHigh().toString();
						  ageLow = rere.getAgeRange().getLow().toString();
						  emotions = rere.getEmotions();

						  for(Emotion einfo : emotions){
							System.out.println("Type:" + einfo.getType());
							System.out.println("Confidence:" + einfo.getConfidence());
						  }
						  
						  gender = rere.getGender().getValue();
						  faceResult = "SUCC";
						  dinfo.setFile_name(detectFileName);
						  dinfo.setGender(gender);
						  dinfo.setAgeLow(ageLow);
						  dinfo.setAgeHigh(ageHigh);
						  dinfo.setFile_name(detectFileName);
	//					  System.out.println("filename:" + dinfo.getFile_name());
	//					  System.out.println("ageHigh:"+ ageHigh);
	//					  System.out.println("gender:"+ gender);
						}

					}catch(Exception e0){
						faceResult ="FAIL";
						System.err.println("["+ new CommUtil().getDebugDate("KOR","") +"][DETECT.SetDetect] setDetectInsert() DETECT CONNECT :: " + e0.getMessage());
					}

					int x = 0;
					try{
	//				    System.out.println("input2");
						
						Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
						conn = DriverManager.getConnection(url,user_name,password);
						conn.setAutoCommit(false);

						try{
	//				 	  System.out.println("input3");
						  queryString = query.toString();
	//					  System.out.println(queryString);
						  pstmt = conn.prepareStatement(queryString);
						  pstmt.setString(++x,faceResult);
						  pstmt.setString(++x,detectNewFileName);
						  pstmt.setString(++x,dinfo.getGender());
						  pstmt.setString(++x,dinfo.getAgeLow());
						  pstmt.setString(++x,dinfo.getAgeHigh());
						  pstmt.executeUpdate();

						}catch(Exception e0){
							faceResult ="FAIL";
							conn.rollback();
							System.err.println("["+ new CommUtil().getDebugDate("KOR","") +"][DETECT.SetDetect] setDetectInsert() sql :: " + e0.getMessage());
							System.out.println("["+ new CommUtil().getDebugDate("KOR","") +"][DETECT.SetDetect] setDetectInsert() sql :: " + e0.getMessage());
						}
//
//						if(faceResult.equals("SUCC")){
//							if(!new CommUtil().setMoveFile(oldFilePath,succFilePath,true).equals("SUCC")){
//								String msg = "";
////								conn.rollback();
//							}
//						}if(faceResult.equals("FAIL")){
//							if(!new CommUtil().setMoveFile(oldFilePath,failFilePath,true).equals("SUCC")){
//								String msg = "";
//								conn.rollback();
//							}
//						}
						conn.commit();
						conn.close();
	//					System.out.println("["+ new CommUtil().getDebugDate("KOR","") +"]" + "[SUCCESS]");
						retstr = "SUCC";
						
					}catch(Exception e0){
						System.err.println("["+ new CommUtil().getDebugDate("KOR","") +"][DETECT.SetDetect] setDetectInsert() DBConnect :: " + e0.getMessage());
						System.out.println("["+ new CommUtil().getDebugDate("KOR","") +"][DETECT.SetDetect] setDetectInsert() DBConnect :: " + e0.getMessage());
					}finally{
						if(rs != null){try{rs.close();}catch (Exception SQL2){ System.err.println(SQL2.getMessage());} }
						if(pstmt != null){ try{pstmt.close();}catch (Exception SQL3){ System.err.println(SQL3.getMessage());}}
					}
				}
			}
		}catch(Exception e0){
			System.err.println("["+ new CommUtil().getDebugDate("KOR","") +"][DETECT.SetDetect] setDetectInsert() FILE :: " + e0.getMessage());
			System.out.println("["+ new CommUtil().getDebugDate("KOR","") +"][DETECT.SetDetect] setDetectInsert() FILE :: " + e0.getMessage());
		}
	}
}

