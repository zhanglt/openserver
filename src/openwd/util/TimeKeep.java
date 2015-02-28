package openwd.util;

import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;


public class TimeKeep extends Thread
{

	String path = null;

	String clearPath = null;

	String clearPath1 = null;

	String timeStr = null;

	String timeblank = null;

	String endblank = null;

	String[] files = null;

	String[] files2 = null;

	String logpath = null;
	static Logger log = Logger.getLogger(TimeKeep.class.getName());
    public TimeKeep(String logpath)
    {
        super("zhonglu timekeeper");
  
        path = null;
        clearPath = null;
        clearPath1 = null;
        timeStr = null;
        timeblank = null;
        endblank = null;
        files = null;
        files2 = null;
        this.logpath = logpath;

    }

	public TimeKeep(String fileuploadpath,String  clearpath ,String cleartime , String timeblank ,String endblank ,String filedownloadpath ) {
		super("zhonglu timekeeper");
		this.path =fileuploadpath; 
		this.clearPath = clearpath;
		this.timeStr = cleartime;
		this.timeblank = timeblank;
		this.endblank = endblank;
		this.clearPath1 = filedownloadpath;
	}
    
    public void run()
    {
    	Calendar cal = null;
		String hour = null;
		// --------------------
		log.debug("-= Disk Clear Init Begin =-");
		//InitServer iser = new InitServer();
		//Vector vv = iser.initPath();
		log.debug("-= Disk Clear Init End =-");

		int tb = new Integer(timeblank).intValue();
		int eb = new Integer(endblank).intValue();
		// ------------------
		while (true) {
			cal = Calendar.getInstance();
			cal.setTime(new java.util.Date());

			try {
				hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
				log.debug("TP working in hour: " + hour);
				if (hour.equals(timeStr)) {
					log.debug("==== Clear `upfiles` Begin ====");
					clearDisk();
					log.debug("==== Clear `upfiles` End ====");

					log.debug("==== Clear `downfiles` Begin ====");
					clearDiskD();
					log.debug("==== Clear `downfiles` End ====");

					log.debug("==== Clear DiskSys Begin ====");
					// clearSys();
					log.debug("==== Clear EiskSys End ====");
					//
					log.debug("==== Clear Directories Begin ====");
					clearDiskdir();
					log.debug("==== Clear Directories End ====");
					try {
						log.debug("sleep " + eb + " minutes after DiskClear.");
						Thread.sleep(1000 * eb * 60);
					} catch (InterruptedException er) {
						log.error("clearDisk() error:" + er);
					}
				}
				log.debug(".......");
				Thread.sleep(1000 * tb * 60);
			} catch (Exception e) {
				System.out.println("TP err80:" + e);
				log.error("TP err80:" + e);
			}
		}
	

    }

    /**
	 * 清理upfiles目录
	 */
	public void clearDisk() {
		// System.out.println("in clearDisk path="+path);
		File ff = new File(path);
		int i = 0;
		if (ff.exists() && ff.isDirectory()) {
			log.debug(" Clearing "+ path);
			while (i < 10) {
				try {
					Thread.sleep(1000);
					clearDisk2(ff, path);
					break;
				} catch (InterruptedException vvs) {
					log.error("TP err86:" + vvs);
				}
				i++;
			}
		} else if (!ff.exists()) {
			try {
				// 061222 add log by zorro
				log.debug("upfiles directory doesn't exist, make it.");
				ff.mkdirs();
			} catch (Exception ffe) {
				log.error("TP err98:" + ffe);
			}
		} else {
			log.debug("clearDisk(up) doesn't run successfully");
		}
	}
	/**
	 * 清理downfiles目录
	 */
	public void clearDiskD() {
		File ff = new File(clearPath1);
		int i = 0;
		if (ff.exists() && ff.isDirectory()) {
			log.debug(" Clearing "+ clearPath1);
			while (i < 10) {
				try {
					Thread.sleep(1000);
					clearDisk2(ff, clearPath1);
					break;
				} catch (InterruptedException vvs) {
					log.error("TP err164:" + vvs);
				}
				i++;
			}
		} else if (!ff.exists()) {
			try {
				// 061222 add log by zorro
				log.debug("downfiles directory doesn't exist, make it.");
				ff.mkdirs();
			} catch (Exception ffe) {
				log.error("TP err181:" + ffe);
			}
		} else {
			log.debug("clearDisk(down) doesn't run successfully");
		}
	}


	public void clearDisk2(File ff, String pathx) {
		File fi = null;

		int i = 0;
		String idpath = null;
		files = ff.list();
		for (i = 0; i < files.length; i++) {
			idpath = pathx + files[i];
			fi = new File(idpath);
			if (fi.isDirectory()) {
				clearDir2(fi, idpath);
			} else {
				clearFile2(pathx, files[i]);
			}
		}
	}

	public void clearDir(String dirname) {
		String path2 = path + dirname;
		File ff = new File(path2);
		String[] files2 = null;

		int i = 0;
		if (ff.exists() && ff.isDirectory()) {
			files2 = ff.list();
			for (i = 0; i < files2.length; i++) {
				clearFile(path2, files2[i]);
			}

		}
	}

	public void clearDir2(File ff, String idpath) {

		files2 = ff.list();
		for (int i = 0; i < files2.length; i++) {
			clearFile(idpath, files2[i]);
		}
	}


	public void clearFile(String path2, String filename) {
		String path3 = path2 + "/" + filename;
		File ff = new File(path3);
		try {
			ff.delete();
			log.debug("| Delete file: " + path3);
		} catch (Exception e) {
			log.error("TP err170:" + path3 + e);
		}
	}

	public void clearFile2(String path2, String filename) {
		String path3 = path2 + filename;
		File ff = new File(path3);
		try {
			ff.delete();
			log.debug("| Delete file: " + path3);
		} catch (Exception e) {
			log.error("TP err170:" + path3 + e);
		}
	}


	public void clearSys() {
		String path3 = null;
		File sysf = new File(clearPath);
		String[] fs = null;

		if (sysf.exists() && sysf.isDirectory()) {
			// String name = null;
			String name2 = null;
			File ff = null;
			fs = sysf.list();
			int index = 0;
			for (int i = 0; i < fs.length; i++) {
				path3 = clearPath + fs[i];
				ff = new File(path3);
				name2 = ff.getName();
				index = name2.indexOf(".");
				if (index == (-1)) {
					index = name2.indexOf("eo");
					if (index == 0) {
						index = name2.indexOf("tm");
						if (index != -1 && !ff.isDirectory()) {
							try {
								ff.delete();
								log.debug("| Delete " + path3+ " in clearSys()");
							} catch (Exception e) {
								log.error("TP err207:" + e);
								System.out.println("TP err207:" + e);
							}
						}
					}
				}
			}
		}
	}


	/**
	 * 删除upfiles,downfiles中的文件夹
	 */
	public void clearDiskdir() {
		File ff = new File(path);
		File fi = null;
		int i = 0;
		String idpath = null;
		{
			files = ff.list();
			for (i = 0; i < files.length; i++) {
				idpath = path + files[i];
				fi = new File(idpath);
				if (fi.isDirectory()) {
					fi.delete();
					log.debug("| Delete dir: "+ idpath);
				} else {
					log.debug(idpath + " is not dir");
				}
			}
		}
		File ff2 = new File(clearPath1);
		File fi2 = null;
		int i2 = 0;
		String idpath2 = null;
		{
			files = ff2.list();
			for (i2 = 0; i2 < files.length; i2++) {
				idpath2 = clearPath1 + files[i2];
				fi2 = new File(idpath2);
				if (fi2.isDirectory()) {
					fi2.delete();
					log.debug("| Delete dir: "+ idpath2);
				} else {
					log.debug(idpath2 + " is not dir");
				}
			}
		}

	}
}