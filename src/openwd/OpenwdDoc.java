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
	String fileuploadpath 	= null;// 处理上传文件的临时目录
	String filedownloadpath = null;// 处理下载文件临时目录
	String openofficepath   = null;//openoffice 程序安装目录
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
				// 首次运行时初始化参数，读取配置文件信息
				if (initflag == 0) {

					Properties p = new Properties();
					try {
						InputStream in = OpenwdDoc.class
								.getResourceAsStream("ZLserver.ini");
						p.load(in);
						in.close();
					} catch (Exception e) {
						log.error("err:加载ZLserver.ini配置文件出错", e);
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
				// 初始化磁盘清理
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
            //读取第一个字节的操作标识符
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
			
			// 请求参数的第一个字符，代表操作类型	 d:下载，u:上传

			log.debug("输入参数rtp: " + new String(ud));
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
				log.error("错误的输入参数=(" + rtp + ")" + numstr);
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
				log.debug("（输入参数）= " + (new String(bd)) + " - "
								+ numstr);
			}
									
		} catch (Exception e) {
			System.out.println("初始化失败，请看到配置文件ZLserver.ini是否配置");
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