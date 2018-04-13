
public class DetectInfo{

    private String face_id;
    private String face_sect;
    private String face_result;
    private String file_name;
    private String gender;
    private String db_status;
    private String reg_date;
	  private String ageLow;
	  private String ageHigh;
	  private String male;
	  private String female;
    private String age10;
    private String age20;
    private String age30;
    private String age40;
    private String age50;
	private String detect_date;
	private String kiosk_id;

    public void setFace_id(String s){ this.face_id = s; }
    public void setFace_sect(String s){ this.face_sect = s; }
    public void setFace_result(String s){ this.face_result = s; }
    public void setFile_name(String s){ this.file_name = s; }
    public void setGender(String s){ this.gender= s; }
    public void setDb_status(String s){ this.db_status = s; }
    public void setReg_date(String s){ this.reg_date = s; }
	  public void setAgeLow(String s){ this.ageLow = s; }
  	public void setAgeHigh(String s){ this.ageHigh = s; }
  	public void setMale(String s){ this.male = s; }
	  public void setFemale(String s){ this.female = s; }
    public void setAge10(String s){ this.age10 = s; }
    public void setAge20(String s){ this.age20 = s; }
    public void setAge30(String s){ this.age30 = s; }
    public void setAge40(String s){ this.age40 = s; }
    public void setAge50(String s){ this.age50 = s; }
	public void setDetect_date(String s){ this.detect_date = s; }
	public void setKiosk_id(String s){ this.kiosk_id = s; }


    public String getFace_id(){ return face_id; }
    public String getFace_sect(){ return face_sect; }
    public String getFace_result(){ return face_result; }
    public String getFile_name(){ return file_name; }
    public String getGender(){ return gender; }
    public String getDb_status(){ return db_status; }
    public String getReg_date(){ return reg_date; }
	  public String getAgeLow(){ return ageLow; }
  	public String getAgeHigh(){ return ageHigh; }
  	public String getMale(){ return male; }
	  public String getFemale(){ return female; }
    public String getAge10(){ return age10; }
    public String getAge20(){ return age20; }
    public String getAge30(){ return age30; }
    public String getAge40(){ return age40; }
    public String getAge50(){ return age50; }
	public String getDetect_date(){ return detect_date; }
	public String getKiosk_id(){ return kiosk_id; }
  
    public String getChkNull(String s){ if(s == null){ return ""; }else{ return s; }   }

}