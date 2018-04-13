/**
 * Name : CommUtil.java
 * Date : 2009.01.04
 * Auth : elTOV
 * Desc : 기본 유틸 모음
*/


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

public class CommUtil{

    public String getListAdminPage(int i_page, int pagesize,int page_cnt,String path){
        return getListAdminPage(i_page,pagesize,page_cnt,path,"");
    }

    public String getListAdminPage(int i_page, int pagesize,int page_cnt,String path,String fnc_name){
        String ret_str="";
        String javascript_name = "setPageSubmit";

        if(fnc_name.equals("")) javascript_name = "setPageSubmit";
        else javascript_name = fnc_name;

        int block_page = ((i_page-1)/10) * (10) + 1;//현재 블럭

        if(!fnc_name.equals("")) javascript_name = fnc_name;

        if(i_page == 1){
            ret_str = "<a href='#' class=\"paging_none\">처음</a> ";
        }else{
            ret_str = "<a href='javascript:"+javascript_name+"(1);' class=\"paging_none\">처음</a> ";
        }

        if(block_page==1){
            ret_str += "<a href='#' class=\"paging_none\">이전 10</a> ";
        }else{
            ret_str += "<a href='javascript:"+javascript_name+"("+(block_page-10)+");' class=\"paging_none\">이전 10</a> ";
        }

        int  i = 0;
        while(i<10 && block_page <= page_cnt){
            if(i == 0){
                if (block_page == i_page){
                    ret_str +="<a href=\"#\" class=\"paging_select\">" + block_page + "</a> ";
                }else {
                    ret_str +="<a href=\"javascript:"+javascript_name+"(" + block_page + ")\" class=\"paging_none\">" + block_page + "</a> ";
                }
            }else{
                if (block_page == i_page){
                    ret_str +="<a href=\"#\" class=\"paging_select\">" + block_page + "</a> ";
                }else {
                    ret_str +="<a href=\"javascript:"+javascript_name+"(" + block_page + ")\" class=\"paging_none\">" + block_page + "</a> ";
                }
            }
            block_page++;
            i++;
        }

        if(block_page > page_cnt){
            ret_str += "<a href='#' class=\"paging_none\">다음 10</a> ";
        }else{
            ret_str += "<a href='javascript:setPageSubmit("+block_page+");' class=\"paging_none\">다음 10</a> ";
        }

        if(i_page==page_cnt){
            ret_str += "<a href='#' class=\"paging_none\">마지막</a> ";
        }else{
            ret_str += "<a href='javascript:"+javascript_name+"(" + page_cnt + ");' class=\"paging_none\">마지막</a> ";
        }

        return ret_str;
    }

    public static String getCheckNull(String str){
        String strTmp;

        if(str == null) strTmp = "";
        else strTmp = str;

        return strTmp.trim();
    }

    public String getCheckBr(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll("\n","<br>");
            strTmp = str;
        }

        return strTmp.trim();
    }

    public String getCheckNoScript(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll("(?i)<script","");
            str = str.replaceAll("(?i)</script>","");
            str = str.replaceAll("\n","<br>");

            strTmp = str;
        }

        return strTmp.trim();
    }

    public String getCheckNoBr(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll("&lt;","<");
            str = str.replaceAll("&gt;",">");
            str = str.replaceAll("<br>","");
            str = str.replaceAll("<BR>","");
            strTmp = str;
        }

        return strTmp.trim();
    }

    public String getCheckNoHtmlScript(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll("(?i)<script","");
            str = str.replaceAll("(?i)</script>","");
            str = str.replaceAll("(?i)<table","");
            str = str.replaceAll("(?i)</table>","");
            str = str.replaceAll("(?i)<tr","");
            str = str.replaceAll("(?i)</tr>","");
            str = str.replaceAll("(?i)<td","");
            str = str.replaceAll("(?i)</td>","");
            str = str.replaceAll("\n","<br>");
            strTmp = str;
        }

        return strTmp.trim();
    }

    public String getCheckNoHtml(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll("<","&lt;");
            str = str.replaceAll(">","&gt;");
            strTmp = str;
        }

        return strTmp.trim();
    }


    public String getCheckEditValue(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll("\"","&#34;");
            str = str.replaceAll("'","&#39;");
            strTmp = str;
        }

        return strTmp.trim();
    }

    public String getCheckDateReplace(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll("-","");
            strTmp = str;
        }

        return strTmp.trim();
    }

    public String getCheckTimeReplace(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll(":","");
            strTmp = str;
        }

        return strTmp.trim();
    }

     public String getCheckNullReplace(String str){
        String strTmp;

        if(str == null){
            strTmp = "";
        }else{
            str = str.replaceAll(" ","");
            strTmp = str;
        }

        return strTmp.trim();
    }

    public String getDate(String nation, String date_type){
        if(date_type == null) return null;
        Calendar calendar = Calendar.getInstance();

        String nation_code = "Asia/Seoul";

        if(nation.equals("SIN")) nation_code = "Asia/Singapore";
        else if(nation.equals("CHN")) nation_code = "Asia/Shanghai";
    
        SimpleTimeZone timezone=new SimpleTimeZone(9*60*60*1000,nation_code);
        calendar = Calendar.getInstance(timezone);
        Date date=calendar.getTime();
    
        SimpleDateFormat sdf = new SimpleDateFormat(date_type,Locale.KOREA);
        sdf.setTimeZone(timezone);

        return sdf.format(date);
    }

    public String getDate(String nation, String date_type,int prevday,int nextday) throws Exception{
        if(date_type == null) return null;
        Calendar calendar = Calendar.getInstance();

        String nation_code = "Asia/Seoul";

        if(nation.equals("SIN")) nation_code = "Asia/Singapore";
        else if(nation.equals("CHN")) nation_code = "Asia/Shanghai";
    
        SimpleTimeZone timezone=new SimpleTimeZone(9*60*60*1000,nation_code);
        calendar = Calendar.getInstance(timezone);

        if(prevday != 0){
            calendar.add(Calendar.DATE, prevday);
        }else if(nextday != 0){
            calendar.add(Calendar.DATE, nextday);
        } 

        Date date=calendar.getTime();
    
        SimpleDateFormat sdf = new SimpleDateFormat(date_type,Locale.KOREA);
        sdf.setTimeZone(timezone);

        return sdf.format(date);
    }

    public String getDebugDate(String nation, String date_type){
        if(date_type == "") date_type = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();

        String nation_code = "Asia/Seoul";

        if(nation.equals("SIN")) nation_code = "Asia/Singapore";
        else if(nation.equals("CHN")) nation_code = "Asia/Shanghai";
    
        SimpleTimeZone timezone=new SimpleTimeZone(9*60*60*1000,nation_code);
        calendar = Calendar.getInstance(timezone);
        Date date=calendar.getTime();
    
        SimpleDateFormat sdf = new SimpleDateFormat(date_type,Locale.KOREA);
        sdf.setTimeZone(timezone);

        return sdf.format(date);
    }

    public String getFileSize(int file_size){
        String f1 = "";
        double d_size = file_size;
        NumberFormat formatter = new DecimalFormat("0.00");

        if(d_size < 1000){
            f1 = formatter.format(d_size) +" Byte";
        }else if(d_size < 1000000){
            f1 = formatter.format(d_size/1000) +" KB";
        }else if(file_size < 1000000000){
            f1 = formatter.format(d_size/1000000) +" MB";
        }else{
            f1 = formatter.format(d_size/1000000000) +" GB";
        }

        return f1;
   }

    public String getFileExt(String filename){
        String f1 = "";
        int dotindex = filename.lastIndexOf(".");
        if (dotindex == -1){
            return "";
        }
        f1 = filename.substring(dotindex);
        if(f1 != null) f1 = f1.toLowerCase();

        return f1;
    }

    public String getConvertLink(String src){
        if(src == null) return "";

        if(src.length() > 7){
            if(!src.substring(0,7).equals("http://")){
                src = "http://" + src;
            }
        }else if( src.length() > 8){
            if(!src.substring(0,8).equals("https://")){
                src = "https://" + src;
            }
        }else{
            src = "http://" + src;
        }

        return src;
    }

    public String getXmlFormat(String src){
        if(src == null) return "";

        src = src.replaceAll("&","&amp;");
        src = src.replaceAll("<","&lt;");
        src = src.replaceAll(">","&gt;");
        src = src.replaceAll("\"","&quot;");
        src = src.replaceAll("\'","&apos;");
        src = src.replaceAll("<","&lt;");
        src = src.replaceAll("<","&lt;");

        return src;
    }

    public String getGoUrl(String msg,String url,String parent){
        String ret_str = "";

        if(parent == null || parent.equals("")) parent = "SELF";
        if(msg == null ) msg = "";

        ret_str = "<script language=\"Javascript\">\n";

        if(!msg.equals("")){
            ret_str += "    alert(\"" + msg + "\");\n";
        }

        if(parent.equals("SELF")){
            ret_str += "    document.location.href = \"" + url + "\";\n";
        }else if(parent.equals("OPENER")){
            ret_str += "    opener.location.reload();\n";
            ret_str += "    self.close();\n";
        }else if(parent.equals("PARENT")){
            ret_str += "    parent.location.href = \"" + url + "\";\n";
        }else if(parent.equals("PARENTRELOAD")){
            ret_str += "    parent.location.reload();\n";
        }else if(parent.equals("PARENTCLOSE")){
            ret_str += "    parent.location.href = \"" + url + "\";\n";
            ret_str += "    self.close();\n";
        }else if(parent.equals("NOMOVE")){

        }else if(parent.equals("BACK")){
            ret_str += "    window.history.back(-1);\n";
        }else if(parent.equals("CLOSE")){
            ret_str += "    self.close();\n";
        }

        ret_str += "</script>\n";

        return ret_str;
    }

    public String getReadClobData(Reader reader) throws IOException {
        StringBuffer data = new StringBuffer();
        char[] buf = new char[1024];
        int cnt = 0;

        if(null != reader){
            while((cnt = reader.read(buf)) != -1){
                data.append(buf, 0, cnt);
            }
        }

        return data.toString();
    }

    public String getWeeklyData(int num){
        String week_date = "";
        String start_date = "";
        String end_date = "";
        int Week_num = 0;

        if(num == 1) Week_num = 6;
        else Week_num = num - 2;
        
        DecimalFormat df = new DecimalFormat("00");
        Calendar currentCalendar = Calendar.getInstance();

        currentCalendar.add(Calendar.DATE, -Week_num);

        String strYear = Integer.toString(currentCalendar.get(Calendar.YEAR));
        String strMonth = df.format(currentCalendar.get(Calendar.MONTH) + 1);
        String strDay = df.format(currentCalendar.get(Calendar.DATE));

        //strMonth = "12";
        //strDay = "31";

        start_date = strYear + "/" + strMonth + "/" + strDay;

        currentCalendar.set(Calendar.YEAR, Integer.parseInt(strYear));
        currentCalendar.set(Calendar.MONTH, Integer.parseInt(strMonth) - 1);
        currentCalendar.set(Calendar.DATE, Integer.parseInt(strDay));

        currentCalendar.add(Calendar.DATE, +6);

        String strYear1 = Integer.toString(currentCalendar.get(Calendar.YEAR));
        String strMonth1 = df.format(currentCalendar.get(Calendar.MONTH) + 1);
        String strDay1 = df.format(currentCalendar.get(Calendar.DATE));

        end_date = strYear1 + "/" + strMonth1 + "/" + strDay1;

        week_date = start_date + " ~ " + end_date;

        return week_date;
    }

    public String[] getDiffDays(String fromDate, String toDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();

        try{
            cal.setTime(sdf.parse(fromDate));
        }catch(Exception e){
        }

        int count = getDiffDayCount(fromDate, toDate);

        cal.add(Calendar.DATE, -1);

        ArrayList<String> list = new ArrayList<String>();

        for(int i=0; i<=count; i++){
            cal.add(Calendar.DATE, 1);

            list.add(sdf.format(cal.getTime()));
        }

        String[] result = new String[list.size()];

        list.toArray(result);

        return result;
    }

    public int getDiffDayCount(String fromDate, String toDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        try{
            return (int) ((sdf.parse(toDate).getTime() - sdf.parse(fromDate).getTime()) / 1000 / 60 / 60 / 24);
        }catch(Exception e){
            return 0;
        }
    }

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
            System.out.println("[COMM CommUtil] setMoveFile Exception  : " + exception.getMessage());
        }

        return strret;
    }

    public String setDeleteFile(String name, String path){
        String strret = "FAIL";

        try{
            String file_name = name;
            String file_dir = path;

            File file = new File(file_dir + file_name);

            if(file.exists()){
                file.delete();
                strret = "SUCC";
            }
        }catch(Exception exception){
            System.out.println("[COMM CommUtil] setDeleteFile Exception  : " + exception.getMessage());
        }

        return strret;
    }

    public long setResizeImage(String rename, String target_name, int i_width, int i_height){
        long l_ret = 0;
        BufferedImage src, dest;
        ImageIcon icon;

        float f_width = 0;
        float f_height = 0;

        try{
            if(fileCopy(target_name,rename)){
                src = ImageIO.read(new File(rename)); // 해당이미지 가져오기

                f_width = i_width;
                f_height = i_height;

                float orig_width = src.getWidth();
                float orig_height = src.getHeight();

                //System.out.println("SIZE = " + orig_width + " , " + orig_height);

                if(f_width < orig_width || f_height < orig_height){
                    float orig_rate = orig_width / orig_height;
                    float p_rate = i_width / i_height;

                    //System.out.println("RATE1 = " + orig_rate + " , " + p_rate);

                    if(orig_rate > p_rate){ // 가로가 더큼
                        f_height = (f_width * (orig_height / orig_width) );
                    }else{
                        f_width = (f_height * orig_rate);
                    }
                }

                i_width = (int)f_width;
                i_height = (int)f_height;

                //System.out.println("CONVERT = " + i_width + " , " + i_height);

                dest = new BufferedImage(i_width, i_height, BufferedImage.TYPE_INT_RGB); 
                Graphics2D g = dest.createGraphics(); 
                AffineTransform at = AffineTransform.getScaleInstance((double) i_width / src.getWidth(),(double) i_height / src.getHeight());

                g.drawRenderedImage(src, at);
                icon = new ImageIcon(dest);

                Image i = icon.getImage();
                BufferedImage bi = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = bi.createGraphics();

                g2.drawImage(i, 0, 0, null); 
                g2.dispose();

                ImageIO.write(bi, "jpg", new File(rename));
                File oFile = new File(rename);
                if (oFile.exists()) {
                    long L = oFile.length();
                    l_ret = L;
                }
            }
        }catch(Exception exception){
            l_ret = 0;
            System.out.println("[COMM CommUtil] setResizeImage Exception  : " + exception.getMessage());
        }
        return l_ret;
    }



    public long setResizeImageOLD(String rename, String target_name, int i_width, int i_height){
        long l_ret = 0;
        BufferedImage src, dest;
        ImageIcon icon;

        try{
            if(fileCopy(target_name,rename)){
                src = ImageIO.read(new File(rename)); // 해당이미지 가져오기

                int orig_width = src.getWidth();
                int orig_height = src.getHeight();

                if(i_width < orig_width || i_height < orig_height){
                    //float rate / i_width 
                }

                dest = new BufferedImage(i_width, i_height, BufferedImage.TYPE_INT_RGB); 
                Graphics2D g = dest.createGraphics(); 
                AffineTransform at = AffineTransform.getScaleInstance((double) i_width / src.getWidth(),(double) i_height / src.getHeight());

                g.drawRenderedImage(src, at);
                icon = new ImageIcon(dest);

                Image i = icon.getImage();
                BufferedImage bi = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = bi.createGraphics();

                g2.drawImage(i, 0, 0, null); 
                g2.dispose();

                ImageIO.write(bi, "jpg", new File(rename));
                File oFile = new File(rename);
                if (oFile.exists()) {
                    long L = oFile.length();
                    l_ret = L;
                }
            }
        }catch(Exception exception){
            l_ret = 0;
            System.out.println("[COMM CommUtil] setResizeImage Exception  : " + exception.getMessage());
        }
        return l_ret;
    }


     public boolean fileCopy(String inFileName, String outFileName) {
        boolean result = false;
        try {
           FileInputStream fis = new FileInputStream(inFileName);
           FileOutputStream fos = new FileOutputStream(outFileName);
           
           int data = 0;
           while((data=fis.read())!=-1) {
              fos.write(data);
           }

           fis.close();
           fos.close();

           result = true;

        }catch(IOException e){
         // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
     }




     public boolean getTimeResult = true;//Local Varriable
     public String movieTime = "0";//Local Varriable

     public String getMovieToImg(String oldfilename, String oldbasepath,String newFilename,String ffmpegPath){
        //oldfilename = 들어올 파일 이름  //oldbasepath= 실제 파일이 있는 경로 //newFilename = 파일을 보낼 경로. //ffmpegPath = ffmpeg.exe 파일이 있는 경로.

        String retstr = "FAIL";
        String outputName = oldfilename; 
        File fResult = new File(oldbasepath + System.getProperty("file.separator") + outputName);
        String fImg = newFilename;
        String[] cmdLine = new String[]{ffmpegPath,"-ss", "2", "-i", fResult.getPath(), "-vcodec", "mjpeg", "-vframes", "3", "-an", "-f", "rawvideo", fImg};  //SWF 이미지변환

        // 동영상 플레이 타임을 구하는 구간 시작
        try{
            ProcessBuilder reading = new ProcessBuilder(ffmpegPath,"-i",fResult.getPath(),fResult.getPath());
            final Process prc2 = reading.start();
            new Thread() {
                public void run() {//Im might be a 
                    try{
                        Pattern durPattern = Pattern.compile("(?<=Duration: )[^,]*");
                        Scanner sc = new Scanner(prc2.getErrorStream());
                        String dur = sc.findWithinHorizon(durPattern, 0);
                        if (dur == null){throw new RuntimeException("Couldn't parse duration.");}
                        String[] hms = dur.split(":");
                        double totalSecs = Integer.parseInt(hms[0])*3600 + Integer.parseInt(hms[1])*60 + Double.parseDouble(hms[2]);
                        System.out.println("Duration: " + totalSecs + " sec.");
                        movieTime = totalSecs + "";
                        getTimeResult = false;
                    }catch(Exception e){
                        //System.out.println("ERROR getMovieToImg 1");
                        getTimeResult = false;
                        e.printStackTrace();
                    }
                }
            }.start();
        }catch(Exception e){
            //System.out.println("ERROR getMovieToImg 2");
            getTimeResult = false;
            e.printStackTrace();
            return "0"; 
        }

        int count = 1;

        try{
            while(getTimeResult && count <= 100000){  //리턴값을 맞추기 위해서 프로세싱하는 동안 슬립, 최대 count 100까지
                try{
                    Thread.sleep(10);
                    count++;
                }catch(Exception e){
                    e.printStackTrace();
                    break;
                }
            }

            //파일 변환 프로세싱 시작
            ProcessBuilder pb = new ProcessBuilder(cmdLine);
            pb.redirectErrorStream(true);
            Process p = null;

            try{
                p = pb.start();
            }catch(Exception e){
                e.printStackTrace();
                p.destroy();
                return "0";
            }

            exhaustInputStream(p.getInputStream());
            // 다른 쓰레드 중지
            try {p.waitFor();} catch (InterruptedException e){p.destroy();}
            // 정상 종료가 되지 않았을 경우 
            if (p.exitValue() != 0){ movieTime = "0"; }
            // 변환을 하는 중 에러가 발생하여 파일의 크기가 0일 경우
            if (fResult.length() == 0){ movieTime = "0"; }
        }catch(Exception e){
            System.out.println("[COMM CommUtil] setResizeImage Exception  : " + e.getMessage());
            movieTime = "0";
        }

        return movieTime;
     }

     private void exhaustInputStream(final InputStream is) {
        new Thread() {
            public void run() {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String cmd;
                    while((cmd = br.readLine()) != null) { // 읽어 들일 라인이 없을 때까지 계속 반복);
                        System.out.println("cmd = " + cmd);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
     }


     public String[] getNowFirst_LastDay(){//첫번째 년,월,일  마지막 년,월,일을 구하는 메소드
         String[] result = new String[2];
         Date today = new Date();  
         Calendar calendar = Calendar.getInstance();  
         calendar.setTime(today);  
         calendar.add(Calendar.MONTH, 1);  
         calendar.set(Calendar.DAY_OF_MONTH, 1);  
         calendar.add(Calendar.DATE, -1);  
         Date lastDayOfMonth = calendar.getTime();  
         DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
         Calendar calendar2 = Calendar.getInstance();  
         calendar2.setTime(today);  
         calendar2.add(Calendar.MONTH, 0);  
         calendar2.set(Calendar.DAY_OF_MONTH, 1);  
         calendar2.add(Calendar.DATE, 0); 
         Date lastDayOfMonth2 = calendar2.getTime();  
         String lastDay = sdf.format(lastDayOfMonth);
         String firstDay = sdf.format(lastDayOfMonth2);
         result[0] = firstDay;
         result[1] = lastDay;
         return result;
     }
    
     public String[] getNowFirst_LastWeek(){//해당 일의 월요일 ~ 일요일 구간(주)
         String[] result = new String[2];
         Calendar today = Calendar.getInstance();
         String s = (today.get(Calendar.YEAR)) + "";
         String m = (today.get(Calendar.MONTH) + 1) + "";
         String e = (today.get(Calendar.DATE)) + "";
         if((today.get(Calendar.MONTH) + 1) < 10){ m = "0"+m;}
         if((today.get(Calendar.DATE)) < 10){ e = "0"+e;}		   
         String dateString = s + "" + m + "" + e;
       
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd"); 
         Date date = null; 
         try { 
            date = simpleDateFormat.parse(dateString); 
         }
         catch (Exception ee) { 
            System.out.println("잘못된 문자열이네요"); 
         } 
         Calendar cal = Calendar.getInstance(Locale.KOREA); 
         cal.setTime(date); 
         cal.add(Calendar.DATE, 1 - cal.get(Calendar.DAY_OF_WEEK)); 
         String startday = simpleDateFormat.format(cal.getTime()); 
         cal.setTime(date); 
         cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK)); 
         String endday =  simpleDateFormat.format(cal.getTime());
         result[0] = startday;
         result[1] = endday;
         return result;
     }
}
