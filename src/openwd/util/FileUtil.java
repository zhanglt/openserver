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
     * �����ļ�����,�����ļ�Ψһ��ʶ��pid
     * PID����=��λһ�����Ŀ¼+��λ�������Ŀ¼+13λʱ������+4λ�ļ���׺��
     */
	
	public static String genPID(String fName, String institution_id){
		StringBuffer pid = new StringBuffer(50);
		String filedir = getCmdir();
		String fileExtEnd = fName.substring(fName.lastIndexOf("."), fName.length());//��λ�ļ���׺��
		String[] ps = filedir.split("/");
		pid.append(ps[0]);//��λһ�����Ŀ¼
		pid.append(ps[1]);//��λ�������Ŀ¼
		
		pid.append(System.currentTimeMillis());//8λ+13λʱ��+���취���2λ+4λ��׺,��27λ
		
		if ( institution_id != null && !institution_id.equals("")) {
			institution_id = institution_id.trim();
		}
		
		pid.append(institution_id.trim());//���취���
		pid.append(fileExtEnd);
		System.out.println(fName+":-----pid:"+pid);     
		return pid.toString();
	}
	 /**
     * 
     * �������ļ�����ֱ�������ļ���
     * PID����=��λһ�����Ŀ¼+��λ�������Ŀ¼+13λʱ������+4λ�ļ���׺��
     */
	public static String genPID(){
		StringBuffer pid = new StringBuffer(50);
		String filedir = getCmdir();
		String[] ps = filedir.split("/");
		pid.append(ps[0]);//��λһ�����Ŀ¼
		pid.append(ps[1]);//��λ�������Ŀ¼
		
		pid.append(System.currentTimeMillis());//8λ+13λʱ��+���취���2λ+4λ��׺,��27λ
		pid.append("JC");//���취���
		pid.append(".doc");
		return pid.toString();
	}
	/**
     * 
     * �������߱༭word�ĵ���Ψһ��ʶ��pid
     * PID����=��λһ�����Ŀ¼+��λ�������Ŀ¼+13λʱ������++���취���2λ
     * @param institution_id
     *            String ���취���
     *            
     * @return String �ļ�Ψһ��ʶ��pid
     */
	
	public static String genNofilePID(String institution_id){
		StringBuffer pid = new StringBuffer(50);
		String filedir = getCmdir();
		String[] ps = filedir.split("/");
		pid.append(ps[0]);//��λһ�����Ŀ¼
		pid.append(ps[1]);//��λ�������Ŀ¼
		
		pid.append(System.currentTimeMillis());//8λ+13λʱ��+���취���2λ,��23λ
		
//		pid.append((String.valueOf(1000+rdm.nextInt(1000))).substring(1));
		if ( institution_id != null && !institution_id.equals("")) {
			institution_id = institution_id.trim();
		}
		
		pid.append(institution_id.trim());//���취���
		System.out.println("--------------------���߱༭�ĵ���pid:"+pid);     
		return pid.toString();
	}
	/**
	* @author 
	* @version 
	* @function ����Ŀ¼  ��λ��ǰ��+��λ��ǰ��+"/"+��λ��ǰ��+��λ��ǰСʱ
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
	* @function ����λ��������ת��Ϊ��λ���ַ���
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
	* @function ����λ��������ת��Ϊ��λ���ַ���
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
	 * ����Ψһ��ʶ��pid,��ȡ�ļ���Ӧ�ĵ�����ϵͳ�е����·��������ļ�ϵͳ�ĸ�Ŀ¼
	 * @return �ļ�·��
	 */
	public static String genPath(String fPid){
		String filepath="";
		String filepathfirst=fPid.substring(0,4);
		String filepathsecond=fPid.substring(4,8);
		filepath=filepathfirst+"/"+filepathsecond;
		return filepath;
	}
	/**
	 * �õ������洢�ľ���·��
	 * @return �ļ�·��
	 */
	public static String getRealPath(HttpServletRequest request){
		
		String servPath = request.getSession().getServletContext().getRealPath(""); /**ȡ�÷�����·��*/
    	
    	String configPathKey = "URL_ZFW"; /**���и����洢Ĭ��·��*/		
    	String RealPath = servPath + File.separator + OpenConfig.getkeyvalue(configPathKey); /**�ļ������Ŀ¼*/	
    	
		return RealPath;
	}
	  /**
     * 
     * ���ݴ����������·��
     * 
     * @return boolean false ���ɹ� true �ɹ�
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
