package utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {

	public static Date makeDate(int year, int month, int day){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, day);		
		return cal.getTime();
	}
	
	public static int extractYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);		
		return cal.get(Calendar.YEAR);
	}
	
	public static int extractMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);		
		return 1+cal.get(Calendar.MONTH);
	}
	
	public static int extractDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);		
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static String formatDate(Date date){
		String dateString = date.toString();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try {
			dateString = formatter.format(date);
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		}
		return dateString;
	}
	
	public static String formatTime(Date date){
		String dateString = date.toString();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		try {
			dateString = formatter.format(date);
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		}
		return dateString;
	}
	
	public static String now(){		
		return formatTime(today());
	}
	
	public static Date parseDate(String str){
		String[] ps = str.split("-");
		int dd = Integer.parseInt(ps[0]);
		int mm = Integer.parseInt(ps[1]);
		int yyyy = Integer.parseInt(ps[2]);
		Date date = null;
		date = makeDate(yyyy, mm, dd);
		return date;
	}
	
	public static Date today(){
		return new Date();
	}
	
	public static java.sql.Date toSQLDate(Date utilDate){		
	    return new java.sql.Date(utilDate.getTime());
	}
	
	public static int thisYear(){
		return extractYear(new Date());
	}
	
	public static int thisMonth(){
		return extractMonth(new Date());
	}
		
}
