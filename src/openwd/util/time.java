package openwd.util;

import java.io.Serializable;
import java.util.Calendar;

public class time implements Serializable {
	private static final long serialVersionUID = 1L;

	public time() {
	}

	public String gettime() {
		Calendar cal = null;
		String year = "0000";
		String month = "00";
		String day = "00";
		String strtime = "";
		cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		try {
			year = Integer.toString(cal.get(Calendar.YEAR));
			month = month + Integer.toString(cal.get(Calendar.MONTH) + 1);
			month = month.substring(month.length() - 2);
			day = day + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
			day = day.substring(day.length() - 2);
			strtime = year + month + day;
			return strtime;
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}
}