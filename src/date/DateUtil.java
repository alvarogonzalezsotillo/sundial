package date;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import computation.SunDialData;
import computation.SunPosition;


public class DateUtil {
	
	public static Date computeDateWithAnotherYear(TimeZone tz, Date date, int year ){
		Calendar c = Calendar.getInstance(tz);
		c.setTime(date);
		c.set(Calendar.YEAR, year );
		return c.getTime();
	}

	
	public static Date computeDateWithAnotherHourAndMinute(TimeZone tz, Date date, Date time) {
		Calendar c1 = Calendar.getInstance(tz);
		Calendar c2 = Calendar.getInstance(tz);
		c1.setTime(date);
		c2.setTime(time);
		c1.set( Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY) );
		c1.set( Calendar.MINUTE, c2.get(Calendar.MINUTE) );
		c1.set( Calendar.MILLISECOND, c2.get(Calendar.MILLISECOND) );
		return c1.getTime();
	}
	
	public static Date createDateFromSecondOfDay( TimeZone tz, int year, int dayOfYear, int secondOfDay ){
		Calendar c = Calendar.getInstance(tz);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, dayOfYear);
		c.set( Calendar.HOUR_OF_DAY, secondOfDay/(60*60) );
		c.set( Calendar.MINUTE, (secondOfDay/60)%60 );
		c.set(Calendar.SECOND, secondOfDay%60);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 
	 * @param tz
	 * @param year
	 * @param month 0 - 11
	 * @param day 1 - 31
	 * @return
	 */
	public static Date createDate( TimeZone tz, int year, int month, int day ){
		return createDate( tz, year, month, day, 0, 0 );
	}

	
	/**
	 * 
	 * @param tz
	 * @param year
	 * @param month 0 - 11
	 * @param day 1 - 31
	 * @param hour 0 - 23
	 * @param minute 0 - 59
	 * @return
	 */
	public static Date createDate( TimeZone tz, int year, int month, int day, int hour, int minute){
		Calendar c = Calendar.getInstance(tz);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date firstSecondOfDate(TimeZone tz, Date date) {
		Calendar c = Calendar.getInstance(tz);
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		return createDate( tz, year, month, day );
	}

	public static Date lastSecondOfDate(TimeZone tz, Date date) {
		Calendar c = Calendar.getInstance(tz);
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		return createDate( tz, year, month, day, 23, 59 );
	}
	
	
	
}
