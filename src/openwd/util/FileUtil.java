package openwd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import openwd.util.OpenConfig;

public class FileUtil {
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 10;
	
	public static void doCopyFile(File srcFile, File destFile,
			boolean preserveFileDate) throws IOException {

		FileInputStream input = new FileInputStream(srcFile);
		try {
			FileOutputStream output = new FileOutputStream(destFile);
			try {
				copy(input, output);
			} finally {
				try {
					output.close();
				} catch (IOException ioe) {
					// do nothing
				}
			}
		} finally {
			try {
				input.close();
			} catch (IOException ioe) {
				// do nothing
			}
		}
		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '"
					+ srcFile + "' to '" + destFile + "'");
		}
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}

	}

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	 /**
     * 
     * 根据文件名称,生成文件唯一标识符pid
     * PID编码=两位一级相对目录+两位二级相对目录+13位时间序列+4位文件后缀名
     */
	
	public static String genPID(String fName, String institution_id){
		StringBuffer pid = new StringBuffer(50);
		String filedir = getCmdir();
		String fileExtEnd = fName.substring(fName.lastIndexOf("."), fName.length());//四位文件后缀名
		String[] ps = filedir.split("/");
		pid.append(ps[0]);//两位一级相对目录
		pid.append(ps[1]);//两位二级相对目录
		
		pid.append(System.currentTimeMillis());//8位+13位时间+公检法标记2位+4位后缀,共27位
		
		if ( institution_id != null && !institution_id.equals("")) {
			institution_id = institution_id.trim();
		}
		
		pid.append(institution_id.trim());//公检法标记
		pid.append(fileExtEnd);
		System.out.println(fName+":-----pid:"+pid);     
		return pid.toString();
	}
	 /**
     * 
     * 不根据文件名，直接生成文件名
     * PID编码=两位一级相对目录+两位二级相对目录+13位时间序列+4位文件后缀名
     */
	public static String genPID(){
		StringBuffer pid = new StringBuffer(50);
		String filedir = getCmdir();
		String[] ps = filedir.split("/");
		pid.append(ps[0]);//两位一级相对目录
		pid.append(ps[1]);//两位二级相对目录
		
		pid.append(System.currentTimeMillis());//8位+13位时间+公检法标记2位+4位后缀,共27位
		pid.append("JC");//公检法标记
		pid.append(".doc");
		return pid.toString();
	}
	/**
     * 
     * 生成在线编辑word文档的唯一标识符pid
     * PID编码=两位一级相对目录+两位二级相对目录+13位时间序列++公检法标记2位
     * @param institution_id
     *            String 公检法标记
     *            
     * @return String 文件唯一标识符pid
     */
	
	public static String genNofilePID(String institution_id){
		StringBuffer pid = new StringBuffer(50);
		String filedir = getCmdir();
		String[] ps = filedir.split("/");
		pid.append(ps[0]);//两位一级相对目录
		pid.append(ps[1]);//两位二级相对目录
		
		pid.append(System.currentTimeMillis());//8位+13位时间+公检法标记2位,共23位
		
//		pid.append((String.valueOf(1000+rdm.nextInt(1000))).substring(1));
		if ( institution_id != null && !institution_id.equals("")) {
			institution_id = institution_id.trim();
		}
		
		pid.append(institution_id.trim());//公检法标记
		System.out.println("--------------------在线编辑文档的pid:"+pid);     
		return pid.toString();
	}
	/**
	* @author 
	* @version 
	* @function 生成目录  两位当前年+两位当前月+"/"+两位当前日+两位当前小时
	* @return String
	 */
	public static String  getCmdir() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return getstwo(year)+getstdir(month)+"/"+getstdir(day)+getstdir(hour) ;
	}
	
	/**
	* @author
	* @version 
	* @function 将个位数的整数转换为两位的字符串
	* @return String
	 */
	public static String  getstwo(int number) {
		String stdir=String.valueOf(number);
		if(stdir.length()>2){
			stdir=stdir.substring(stdir.length()-2);
		}
		return stdir;
	}
	
	/**
	* @author 
	* @version
	* @function 将个位数的整数转换为两位的字符串
	* @return String
	 */
	public static String  getstdir(int number) {
		String stdir=String.valueOf(number);
		if(stdir.length()<2){
			stdir=0+stdir;
		}
		return stdir;
	}
	
	/**
	 * 根据唯一标识符pid,获取文件对应文档管理系统中的相对路径，相对文件系统的根目录
	 * @return 文件路径
	 */
	public static String genPath(String fPid){
		String filepath="";
		String filepathfirst=fPid.substring(0,4);
		String filepathsecond=fPid.substring(4,8);
		filepath=filepathfirst+"/"+filepathsecond;
		return filepath;
	}
	/**
	 * 得到附件存储的绝对路径
	 * @return 文件路径
	 */
	public static String getRealPath(HttpServletRequest request){
		
		String servPath = request.getSession().getServletContext().getRealPath(""); /**取得服务器路径*/
    	
    	String configPathKey = "URL_ZFW"; /**所有附件存储默认路径*/		
    	String RealPath = servPath + File.separator + OpenConfig.getkeyvalue(configPathKey); /**文件保存根目录*/	
    	
		return RealPath;
	}
	  /**
     * 
     * 根据传入参数生成路径
     * 
     * @return boolean false 不成功 true 成功
     */
	public static boolean MakeDirectory(String FilePath) {
        File mFile = new File(FilePath);
        mFile.mkdirs();
        return mFile.isDirectory();
    }
	public static boolean delete(String file) {
		return delete(new File(file));
	}

	public static boolean delete(File file) {
		if (file.exists()) {
			return file.delete();
		}
		else {
			return false;
		}
	}

	public static void deltree(String directory) {
		deltree(new File(directory));
	}

	public static void deltree(File directory) {
		if (directory.exists() && directory.isDirectory()) {
			File[] fileArray = directory.listFiles();

			for (int i = 0; i < fileArray.length; i++) {
				if (fileArray[i].isDirectory()) {
					deltree(fileArray[i]);
				}
				else {
					fileArray[i].delete();
				}
			}

			directory.delete();
		}
	}
}
