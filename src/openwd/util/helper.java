package openwd.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;

public class helper {
	static Logger log = Logger.getLogger(helper.class.getName());
	String tempname = new String("OPENWDCABDATA");
	public helper(){
		
	}
/**
 * 首次操作下载 UNZDLL.dll
 * @param in
 * @param out
 * @param datalen
 * @param cabpath
 */
	public void downCab(ServletInputStream in, ServletOutputStream out,
			int datalen, String cabpath) {
		String cabstr = new String(cabpath + "/UNZDLL.DLL");
		// log
		log.error("down UNZDLL - cabstr: " + cabstr);
		File f = new File(cabstr);
		int cablen = new Long(f.length()).intValue();
		byte[] cabb = new byte[cablen];
 
		try {
			FileInputStream fis = new FileInputStream(f);
			fis.read(cabb);
			fis.close();
			out.write(cabb);
			out.close();
			in.close();
		} catch (IOException ioe) {
			log.error("err184:" + ioe);
			try {
				out.write(new String("error").getBytes());
				out.close();
				in.close();
			} catch (IOException ie) {
				log.error("err192:" + ie);
			}
			return;
		}
	}
/**
* 首次操作下载 ZIPDLL.dll
 * @param in
 * @param out
 * @param datalen
 * @param cabpath
 */
	public void downCab2(ServletInputStream in, ServletOutputStream out,
			int datalen,String cabpath) {
		String cabstr = new String(cabpath + "/ZIPDLL.DLL");
		// log
		log.debug("down ZIPDLL - cabstr: " + cabstr);
		File f = new File(cabstr);
		int cablen = new Long(f.length()).intValue();
		byte[] cabb = new byte[cablen];
		try {
			FileInputStream fis = new FileInputStream(f);
			fis.read(cabb);
			fis.close();
			out.write(cabb);
			out.close();
			in.close();
		} catch (IOException ioe) {
			log.error("err233:" + ioe);
			try {
				out.write(new String("error").getBytes());
				out.close();
				in.close();
			} catch (IOException ie) {
				log.error("err241:" + ie);
			}
			return;
		}
	}
	/**
	 * 下载文档
	 * @param in
	 * @param out
	 * @param datalen
	 * @param numstr
	 * @param filedownloadpath
	 */
	public void downLoad(ServletInputStream in, ServletOutputStream out,
			int datalen,String numstr,String filedownloadpath ,String realpath) {

		int datalength = datalen;
		log.debug("downL - datalength in downLoad() is " + datalength);
		Vector<Object> vvv = new Vector<Object>();
		byte[] dataByte = new byte[datalength - 1];

		try {
			in.read(dataByte);
		} catch (IOException e) {
			log.error("err172:" + e);
		}
       System.out.println("dataByte.length---------------------:"+dataByte.length);
		String qx = new String(dataByte, 0, 1);   // 0编辑,1修改,2浏览
		String dg = new String(dataByte, 1, 1);   // 生成文档标识
		String unid = new String(dataByte, 2, 23);// 文档PID

		byte[] head1 = null;
		byte[] head2 = null;
		byte[] head3 = null;
		byte[] head4 = null;
		byte[] head5 = null;
		byte[] head6 = null;
		byte[] head7 = null;

		String str1 = new String("HEADSTART\n" + qx + "\n" + "HEADEND\n");
		String str2 = new String("FILEHEADSTART\n");
		String str3 = new String("FILEHEADEND\n" + "TMPSTART\n");
		String str4 = new String("TMPEND\n" + "DATASTART\n");
		String str5 = new String("\nDATAEND");
		String str6 = new String("\nPICTURESTART\n");
		String str7 = new String("\nPICTUREEND");

		head1 = new byte[str1.length()];
		head1 = str1.getBytes();
		head2 = new byte[str2.length()];
		head2 = str2.getBytes();
		head3 = new byte[str3.length()];
		head3 = str3.getBytes();
		head4 = new byte[str4.length()];
		head4 = str4.getBytes();
		head5 = new byte[str5.length()];
		head5 = str5.getBytes();
		head6 = new byte[str6.length()];
		head6 = str6.getBytes();
		head7 = new byte[str7.length()];
		head7 = str7.getBytes();

		/**打开文档*/
		if (dg.equals("z")) {
			log.debug("view z d-begin408");
			vvv = this.getDData( unid, filedownloadpath,  numstr,realpath);
			byte[] fileBytes = (byte[]) vvv.elementAt(0);
			//int filelen = ((Integer) vvv.elementAt(1)).intValue();
	
			try {
				out.write(head1);
				out.write(head2);
				out.write(head3);
				out.write(head4);
				out.write(fileBytes);
				out.write(head5);
				out.write(head6);
				out.write(head7);
				out.flush();
				out.close();
				in.close();
				log.debug("view z d-end447");
			} catch (IOException eerr) {
				log.error("err509:" + eerr);
			}
			return;
		}		
		else {
			log.error("参数dg error");
		}

		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (Exception nn) {
			log.error("err707:" + nn);
		}

	}
	/**上传到服务器*/
	public void upLoad(ServletInputStream in, ServletOutputStream out,
			int datalen, String numstr,String fileuploadpath,String openofficepath,String realpathString) {

		int datalength = datalen;
		File upFile = null;
		String filePath = fileuploadpath;
		String cabfilename = null;
		String filename = null;
		FileOutputStream fos = null;
		String dg = null;
		String unid = null;
		int cabi;
		String fileDirPath = null;
		
		log.debug("upL - 上传路径: "+filePath);

		byte[] upBytes = new byte[24];
		byte[] upByte = new byte[1];
		int flag = 0;
		try {
			in.read(upBytes, 0, 24);

			dg = new String(upBytes, 0, 1);
			unid = new String(upBytes, 1, 23);
			filePath = filePath + unid + numstr + java.io.File.separator;

			log.debug("upL - 新文件夹: "+filePath);
			File idpath = new File(filePath);
			boolean md = idpath.mkdir();
			if(!md){
			   log.error("upL - 创建文件夹失败: "+filePath);
			}
	} catch (IOException e) {
		log.error("upload err808:" + e);
		flag = 1;//重置操作标识
		try {
			out.write(new String("error").getBytes());
			out.flush();
			out.close();
			in.close();
		} catch (IOException ee) {
			log.error("upload err816:" + ee);
		}
		return;
	}
		
		if (flag == 0) {
			int flag2 = 0;
			if (dg.equals("1")) {
				cabfilename = unid + "_dg.zip";
				filePath = filePath + unid + "_dg.doc";
				fileDirPath = fileuploadpath + unid + numstr + java.io.File.separator;
				try {
					upFile = new File(fileDirPath + cabfilename);
					log.debug("upL - upFile: "+fileDirPath+cabfilename);
					if (upFile.exists()) {
						upFile.delete();
						log.debug("upL - 文件存在，删除！");
					}
					boolean nf = upFile.createNewFile();
					if (!nf){
						log.error("upL - 创建文件失败: "+cabfilename);
					}
					Thread.sleep(10);
					fos = new FileOutputStream(upFile);
					for (int bbv = 0; bbv < (datalength - 25); bbv++) {
						in.read(upByte);
						fos.write(upByte);
					}
					fos.close();
				} catch (IOException e) {

					flag2 = 1;
					log.error("upload err:" + e);
					try {
						out.write(new String("error").getBytes());
						out.close();
						in.close();
					} catch (IOException ee) {
						log.error("upload err:" + ee);
					}
					return;
				} catch (InterruptedException ee) {
					flag2 = 1;
					log.error("upload err:" + ee);
					try {
						out.write(new String("error").getBytes());
						out.close();
						in.close();
					} catch (IOException eee) {
						log.error("upload err:" + eee);
					}
					return;
				}
				
				if (flag2 == 0) {						
					filename = unid + "_dg.doc";
					//将文件从压缩包中解压缩
					cabi = this.uncabZW(fileDirPath, cabfilename, filename, numstr);
					if (cabi != 1) {
						log.error("err908");
						try {
							out.write(new String("error").getBytes());
							out.close();
							in.close();
						} catch (IOException eee) {
							log.error("解压缩 错误:" + eee);
						}
						return;
						
					}					
                     //上传完毕，将文件提交到正式数据位置					
					
					try {
						String tempPath = FileUtil.genPath(unid);
						FileUtil.MakeDirectory(tempPath);
						
						File srcFile=new File(realpathString+File.separator +tempPath+File.separator+filename.substring(0,filename.length()-7)+".doc");
						if(srcFile.isFile()){
							FileUtil.doCopyFile(new File(filePath.substring(0,filePath.length()-7)+".doc"),
									new File(realpathString+File.separator +tempPath+File.separator+filename.substring(0,filename.length()-7)+".doc"), true);
						}else{						
							FileUtil.doCopyFile(new File(filePath.substring(0,filePath.length()-7)+".doc"),
								new File(realpathString+File.separator +tempPath+File.separator+filename.substring(0,filename.length()-7)+".docx"), true);
						}
						//转换PDF
						//DoctoPdf conver = new DoctoPdf();
						//conver.docToPdf(openofficepath,new File(genPath(unid)+filename.substring(0,filename.length()-7)+".doc"),new File(genPath(unid)+filename.substring(0,filename.length()-7)+".pdf"));
					} catch (Exception e) {
						System.out.println(e);
						log.error("正式数据提交错误："+e);
					}

					try {
						out.write(new String("ok").getBytes());
						out.close();
					} catch (Exception eg) {
						log.error("err698:" + eg);
					}
					log.debug("upL - dg(1)-end");
				}
			}
		}
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (IOException nn) {
			log.error("err806:" + nn);
		}
	}


/**
 * 获取文档，用于文档浏览	
 * @param unid
 * @param filedownloadpath
 * @param numstr
 * @return
 */

	public Vector<Object> getDData( String unid, String filedownloadpath,String numstr,String realpath) {

		File ff = null;
		FileInputStream fis = null;
		Vector<Object> vv = new Vector<Object>();
		byte[] fbyte = null;
	
		int filelen;
		String filename = unid + ".doc";
		String cabname = unid + ".zip";
		String filepath  = filedownloadpath + unid + numstr + java.io.File.separator;
		File funid = new File(filepath);
		funid.mkdir();
        //复制文档到临时处理目录， 并改名为处理文件名称。
		try {
			//String realfile=realpath+ File.separator +FileUtil.genPath(unid)+File.separator+filename;
			//String destfile=filepath+java.io.File.separator + filename;
			File scrFile=new File(realpath+ File.separator +FileUtil.genPath(unid)+File.separator+filename);
			if(scrFile.isFile()){//是以doc结尾的文档
				FileUtil.doCopyFile(new File(realpath+ File.separator +FileUtil.genPath(unid)+File.separator+filename), new File(filepath
						+ java.io.File.separator + filename), true);				
			}else{//是以docx结尾的文档
				filename = unid + ".docx";
				FileUtil.doCopyFile(new File(realpath+ File.separator +FileUtil.genPath(unid)+File.separator+filename), new File(filepath
						+ java.io.File.separator + filename), true);
			}
			
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	
        //将正文文件打包，用于传输到客户端
		int ret = cab(filepath, cabname, filename);
		if (ret != 1) {
			log.error("cab err324");
			return null;
		}
		ff = new File(filepath + cabname);
		if (ff.exists()) {
			filelen = new Long(ff.length()).intValue();

			try {
				fbyte = new byte[filelen];
				fis = new FileInputStream(ff);
				fis.read(fbyte);
				fis.close();

			} catch (IOException e) {
				log.error("Cab getdata err338:" + e);
				return null;
			} catch (OutOfMemoryError fnf) {
				log.error("Cab err341:" + fnf);
				return null;
			}

			vv.add(0, fbyte);
			vv.add(1, new Integer(filelen));
			return vv;
		}

		else {
			log.debug("getData err ");
			return null;
		}
	}
	/**
	 * 文件传输前压缩打包
	 * @param filepath
	 * @param cabname
	 * @param filename
	 * @return
	 */
	public int cab(String filepath, String cabname, String filename) {
		char c = '\u0800';

		byte abyte0[] = new byte[c];

		try {
			FileOutputStream fileoutputstream = new FileOutputStream(filepath + cabname);
			ZipOutputStream zipoutputstream = new ZipOutputStream(
					new BufferedOutputStream(fileoutputstream));
			FileInputStream fileinputstream = new FileInputStream(filepath + filename);
			BufferedInputStream bufferedinputstream = new BufferedInputStream(
					fileinputstream, c);
			ZipEntry zipentry = new ZipEntry(tempname);
			zipoutputstream.putNextEntry(zipentry);
			int i;
			while ((i = bufferedinputstream.read(abyte0, 0, c)) != -1)
				zipoutputstream.write(abyte0, 0, i);
			bufferedinputstream.close();
			zipoutputstream.close();
		} catch (IOException ioexception) {
			log.error("Cab err259:" + ioexception);
			return 0;
		} catch (OutOfMemoryError outofmemoryerror) {
			log.error("Cab err273:" + outofmemoryerror);
			return 0;
		}
		log.debug("Cab end");
		return 1;
	}
	/**
	 * 文件传输后解压
	 * @param filepath
	 * @param cabname
	 * @param filename
	 * @return
	 */
	
	 public int uncabZW(String filepath, String cabfile, String filename, String numstr) {

			int count;
			int buffer = 2048;
			byte[] data = new byte[buffer];

			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			try {
				//log
				log.debug("unCabZW begin.");
				log.debug("unCab "+filename+" from "+cabfile);
				
				ZipFile zipf = new ZipFile(filepath + cabfile);
				ZipEntry entry = zipf.getEntry(filename);

				if (entry == null) {
					log.error("Cab err : entry=null");
					return 0;
				}

				is = new BufferedInputStream(zipf.getInputStream(entry));
				FileOutputStream fos = new FileOutputStream(filepath + filename.substring(0,filename.length()-7)+".doc");
				dest = new BufferedOutputStream(fos, buffer);
				while ((count = is.read(data, 0, buffer)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				zipf.close();
				dest.close();
				is.close();
			} catch (IOException ee) {
				log.error("uncab err 180: " + ee);
				return 0;
			}
			log.debug("unCabZW end.");
			return 1;
		}
}