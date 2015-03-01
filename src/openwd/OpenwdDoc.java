package openwd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import openwd.util.FileUtil;
import openwd.util.TimeKeep;
import openwd.util.helper;

public class OpenwdDoc extends HttpServlet {
	String fileuploadpath 	= null;// �����ϴ��ļ�����ʱĿ¼
	String filedownloadpath = null;// ���������ļ���ʱĿ¼
	String openofficepath   = null;//openoffice ����װĿ¼
	String cabflag 	 = null;
	String cabpath	 = null;
	String logpath 	 = null;
	String clearpath =null ; 
	String cleartime =null;
	String timeblank =null;
	String endblank  =null;
	int initflag = 0;
	int timeflag = 0;
	int filenum  = 0;
	helper hp    = new helper();
	static Logger log = Logger.getLogger(OpenwdDoc.class.getName());
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		try {
			int datalength;
			synchronized (this) {
				// �״�����ʱ��ʼ����������ȡ�����ļ���Ϣ
				if (initflag == 0) {

					Properties p = new Properties();
					try {
						InputStream in = OpenwdDoc.class
								.getResourceAsStream("ZLserver.ini");
						p.load(in);
						in.close();
					} catch (Exception e) {
						log.error("err:����ZLserver.ini�����ļ�����", e);
					}
					
					if (p.containsKey("fileuploadpath")) {
						this.fileuploadpath = p.getProperty("fileuploadpath");
					}
					if (p.containsKey("filedownloadpath")) {
						this.filedownloadpath = p.getProperty("filedownloadpath");
					}
					if (p.containsKey("cabpath")) {
						this.cabpath = p.getProperty("cabpath");
					}
					if (p.containsKey("cabflag")) {
						this.cabflag = p.getProperty("cabflag");
					}
					if (p.containsKey("logpath")) {
						this.logpath = p.getProperty("logpath");
					}
					if (p.containsKey("clearpath")) {
						this.clearpath = p.getProperty("clearpath");
					}
					if (p.containsKey("cleartime")) {
						this.cleartime = p.getProperty("cleartime");
					}
					if (p.containsKey("timeblank")) {
						this.timeblank = p.getProperty("timeblank");
					}
					if (p.containsKey("endblank")) {
						this.endblank = p.getProperty("endblank");
					}
					if (p.containsKey("openofficepath")) {
						this.openofficepath = p.getProperty("openofficepath");
					}
					
					initflag = 1;
					log.info("servlet inited .....");
				}
				// ��ʼ����������
				if (timeflag == 0) {
					if (initflag == 1) {
						TimeKeep tk = new TimeKeep(fileuploadpath, clearpath ,cleartime , timeblank , endblank ,filedownloadpath );
						tk.start();
						timeflag = 1;
						log.debug("-= timekeeper started =-");
					} else {
						log.debug("err: servlet do not inited");
					}
				}
			}

			String numstr = add();
		
			ServletInputStream in = null;
			ServletOutputStream out = null;
            //��ȡ��һ���ֽڵĲ�����ʶ��
			byte[] ud = new byte[1];
			try {
				in = req.getInputStream();
				out = res.getOutputStream();
				datalength = req.getContentLength();
				in.read(ud);
			} catch (IOException f) {
				log.error("init in-out err:" + f);
				return;
			}
			
			// ��������ĵ�һ���ַ��������������	 d:���أ�u:�ϴ�

			log.debug("�������rtp: " + new String(ud));
			char rtp =(new String(ud)).charAt(0);
			String realpathString = FileUtil.getRealPath(req);	
			switch(rtp){
			case 'd':
				log.debug("downL begin - " + numstr);
				hp.downLoad(in, out, datalength, numstr, filedownloadpath,realpathString);
				log.debug("downL end - " + numstr);
				break;
			case 'u':
				log.debug("upL begin - " + numstr);
				hp.upLoad(in, out, datalength, numstr,fileuploadpath,openofficepath,realpathString);
				log.debug("upL end - " + numstr);
				break;
			case 'c':
				log.debug("downUNDLL begin - " + numstr);
				hp.downCab(in, out, datalength, cabpath);
				log.debug("downUNDLL end - " + numstr);
				break;
			case 'z':
				log.debug("downZIPDLL begin - " + numstr);
				hp.downCab2(in, out, datalength, cabpath);
				log.debug("downZIPDLL end - " + numstr);
				break;
			default:
				log.error("realpathString==================:"+realpathString);
				log.error("������������=(" + rtp + ")" + numstr);
				byte[] bd = null;
				try {
					out.write(new String("error").getBytes());
					out.flush();
					bd = new byte[33];
					in.read(bd);
				} catch (IOException sdf) {
					System.out.println("err:" + sdf);
					log.error("err166:" + sdf);
				}
				log.debug("�����������= " + (new String(bd)) + " - "
								+ numstr);
			}
									
		} catch (Exception e) {
			System.out.println("��ʼ��ʧ�ܣ��뿴�������ļ�ZLserver.ini�Ƿ�����");
			e.printStackTrace();

		}

	}

	public synchronized String add() {
		filenum = filenum + 1;
		if (filenum > 10000)
			filenum = 1;
		return (new Long(filenum)).toString();
	}

}