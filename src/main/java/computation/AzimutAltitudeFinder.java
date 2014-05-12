package computation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import computation.geom.AzimutAltitude;

import date.DateIterable;
import date.DateUtil;



public class AzimutAltitudeFinder {

	private static final double DELTA = 0.001;

	private static final void log(String s){
//		System.out.println( s );
	}
	
	public static Date findAzimut(SunDialData data, Date day, double azimut ){
		Calendar c = Calendar.getInstance(data.getTimeZone());
		c.setTime(day);
		c.set(Calendar.HOUR_OF_DAY, 8);
		c.set(Calendar.MINUTE, 0);
		Date iniDate = c.getTime();
		c.set(Calendar.HOUR_OF_DAY, 22);
		Date endDate = c.getTime();
		return findAzimut(data.getLatLon(),iniDate,endDate,azimut);
	}
	
	public static Date[] findAzimutAltitude(SunDialData data, int year, AzimutAltitude azal ){
	
		class DateTimeComparator implements Comparator<Date>{
			
			private boolean _usaAño;
			private TimeZone _tz;

			public DateTimeComparator(TimeZone tz){
				this(tz, false);
			}

			public DateTimeComparator(TimeZone tz, boolean usaAño){
				_tz = tz;
				_usaAño = usaAño;
			}

			@Override
			public int compare(Date d1, Date d2) {
				long m1 = d1.getTime();
				long m2 = d2.getTime();
				
				if( !_usaAño ){
					m1 = DateUtil.computeDateWithAnotherYear(_tz, d1, SunPosition.REFERENCEYEAR ).getTime();
					m2 = DateUtil.computeDateWithAnotherYear(_tz, d2, SunPosition.REFERENCEYEAR ).getTime();
				}
				
				// LAS FECHAS SE COMPARAN SIN AÑO, Y SON IGUALES SI NO SE DISTANCIAN MAS DE DIA Y MEDIO
				double delta = 1000*60*60*24*1.5;
				if( Math.abs(m1-m2) < delta ){
					return 0;
				}
				
				return (int) (m1 - m2);
			}
			
		};
		
		int day = 365/2;
		int minute = 60*24/2; 
		
		ArrayList<Date> ret = new ArrayList<Date>();
		
		TimeZone tz = data.getTimeZone();
		Date date1 = findAzimutAltitudeImpl(data, year, day, minute, azal);
		if( date1 == null ){
			return new Date[0];
		}
		ret.add(DateUtil.computeDateWithAnotherYear(tz, date1, SunPosition.REFERENCEYEAR));
	
		// AHORA BUSCAMOS ALREDEDOR DE ESA FECHA, CADA 10 DIAS
		Calendar c = Calendar.getInstance(tz);
		DateTimeComparator dtc= new DateTimeComparator(tz);

		for( int i = -365 ; i < 365 ; i += 10 ){
			c.setTime(date1);
			c.add(Calendar.DAY_OF_YEAR, i );
			c.set( Calendar.YEAR, year );
			
			day = c.get( Calendar.DAY_OF_YEAR );
			minute = c.get( Calendar.HOUR_OF_DAY )*60 + c.get( Calendar.MINUTE );
			Date date2 = findAzimutAltitudeImpl(data, year, day, minute, azal);
			if( date2 == null ){
				continue;
			}
			
			boolean present = false;
			for( Date d: ret ){
				present = dtc.compare(d, date2) == 0;
				if( present ){
					break;
				}
			}
			if( !present ){
				ret.add(DateUtil.computeDateWithAnotherYear(tz, date2, SunPosition.REFERENCEYEAR));
			}
		}
		
		Date[] retArray = ret.toArray(new Date[0]);
		Arrays.sort( retArray );
		
		if( retArray.length > 2 ){
			System.out.println( "findAzimutAltitude: " + retArray.length + ":" + ret );
		}
		return retArray;
		
	}
	
	public static Date findAzimutAltitudeImpl(SunDialData data, int year, int day, int minute, AzimutAltitude azal ){
		TimeZone tz = data.getTimeZone();
		LatLon observer = data.getLatLon();

		int second = minute*60;
		
		// UP: -DAY
		// LEFT: -MINUTE
		// 0-UPLEFT, 1-UP, 2-UPRIGHT, 3-LEFT, 4-CENTER, 5-RIGHT, 6-DOWNLEFT, 7-DOWN, 8-DOWNRIGHT
		AzimutAltitude azals[] = new AzimutAltitude[9];
		double distances[] = new double[9];
		
		int minIndex = -1;
		int tries = 0;
		int step = 10;
		double minFoundDistance = Double.MAX_VALUE;
		while( minIndex != 4 && tries < 100000 ){
			tries++;
			
			azals[0] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day-1, second-step));
			azals[1] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day-1, second));
			azals[2] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day-1, second+step));
			azals[3] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day, second-step));
			azals[4] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day, second));
			azals[5] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day, second+step));
			azals[6] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day+1, second-step));
			azals[7] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day+1, second));
			azals[8] = SunPosition.getAzimutAltitude(observer, DateUtil.createDateFromSecondOfDay(tz, year, day+1, second+step));
			

			double minDistance = Double.MAX_VALUE;
			for (int i = 0; i < distances.length; i++) {
				distances[i] = azal.distance(azals[i]);
				if( distances[i] < minDistance ){
					minIndex = i;
					minDistance = distances[i];
				}
			}
			
			log( tries + "  distance:" + minDistance + "  minIndex:" + minIndex );
			
			if( minFoundDistance <= minDistance ){
				log( "No he mejorado:" + tries );
			}
			minFoundDistance = minDistance;
			
			switch( minIndex ){
				case 0:{ // UPLEFT
					day--;
					second-=step;
					break;
				}
				case 1:{ // UP
					day--;
					break;
				}
				case 2:{ // UPRIGHT
					day--;
					second+=step;
					break;
				}
				case 3:{ // LEFT
					second-=step;
					break;
				}
				case 4:{ // CENTER
					break;
				}
				case 5:{ // RIGHT
					second+=step;
					break;
				}
				case 6:{ // DOWNLEFT
					day++;
					second-=step;
					break;
				}
				case 7:{ // DOWN
					day++;
					break;
				}
				case 8:{ // DOWNRIGHT
					day++;
					second+=step;
					break;
				}	
			}
			
		}
		
		return DateUtil.createDateFromSecondOfDay(tz, year, day, second);
	}	
		
	
		
	public static Date findAzimut(LatLon observer, Date iniDate, Date endDate, double azimut ){
		return findAzimutImpl(observer, iniDate, endDate, azimut, DELTA );
	}

	private static Date findAzimutImpl(LatLon observer, Date iniDate, Date endDate, double azimut, double delta ) {
		
		log( "findAzimutImpl");
		log( "  iniDate:" + iniDate );
		log( "  endDate:" + endDate );
		
		Date date = new Date( (iniDate.getTime() + endDate.getTime())/2);
		if( date.getTime() == iniDate.getTime() || date.getTime() == endDate.getTime() ){
			return date;
		}
		AzimutAltitude azal = SunPosition.getAzimutAltitude(observer, date);
		log( "  date:" + date );
		log( "  azal:" + azal );

		
		double error = azal.getAzimut() - azimut;
		log( "  error:" + error );
		if( Math.abs(error) <= delta ){
			return date;
		}
		
		AzimutAltitude iniAzal = SunPosition.getAzimutAltitude(observer, iniDate);
		AzimutAltitude endAzal = SunPosition.getAzimutAltitude(observer, endDate);
		log( "  iniAzal.getAzimut():" + iniAzal.getAzimut() );
		log( "  endAzal.getAzimut():" + endAzal.getAzimut() );
		log( "  azimut:" + azimut );

		if( iniAzal.getAzimut() < azimut && azal.getAzimut() > azimut ){
			return findAzimutImpl(observer, iniDate, date, azimut, delta);
		}
		if( azal.getAzimut() < azimut && endAzal.getAzimut() > azimut ){
			return findAzimutImpl(observer, date, endDate, azimut, delta);
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		LatLon latLon = SunDialData.DEFAULT.getLatLon();
		Date date = new Date();
		Date d = findAzimut(SunDialData.DEFAULT, date, 43);
		System.out.println("Date:" + d );

//		LatLon observer = SunDialData.DEFAULT.getLatLon();
//		TimeZone tz = SunDialData.DEFAULT.getTimeZone();
//		for( int year = 2000 ; year <= SunPosition.REFERENCEYEAR; year++){
//			Date max = computeMaxAltitudeInYear(tz, observer, year);
//			Date min = computeMinAltitudeInYear(tz, observer, year);
//			Date[] same = computeSameAltitudeInYear(tz, observer, year);
//			System.out.println( year );
//			System.out.println( "  " + max );
//			System.out.println( "  " + min );
//			System.out.println( "  " + same[0] + "    " + same[1] );
//		}
	}
	
	
	public static Date computeMaxAltitudeInYear( TimeZone tz, LatLon observer, int year ){
		Date iniDate = DateUtil.createDate(tz, year, 0, 1, 12, 0);
		Date endDate = DateUtil.createDate(tz, year+1, 0, 1);
		DateIterable it = new DateIterable(tz, iniDate, endDate, Calendar.DAY_OF_YEAR, 1);
		
	   	Date ret = it.iterator().next();
	   	AzimutAltitude retAzal = SunPosition.getAzimutAltitude(observer, ret);
	   	
	   	for (Date date : it) {
			AzimutAltitude azal = SunPosition.getAzimutAltitude(observer, date);
			if( azal.getAltitude() > retAzal.getAltitude() ){
				ret = date;
				retAzal = azal;
			}
		}
		
		return ret;
	}

	
	public static Date computeMinAltitudeInYear( TimeZone tz, LatLon observer, int year ){
		Date iniDate = DateUtil.createDate(tz, year, 0, 1, 12, 0);
		Date endDate = DateUtil.createDate(tz, year+1, 0, 1);
		DateIterable it = new DateIterable(tz, iniDate, endDate, Calendar.DAY_OF_YEAR, 1);
		
	   	Date ret = it.iterator().next();
	   	AzimutAltitude retAzal = SunPosition.getAzimutAltitude(observer, ret);
	   	
	   	for (Date date : it) {
			AzimutAltitude azal = SunPosition.getAzimutAltitude(observer, date);
			if( azal.getAltitude() < retAzal.getAltitude() ){
				ret = date;
				retAzal = azal;
			}
		}
		
		return ret;
	}
	
	
	public static Date[] computeSameAltitudeInYear(TimeZone tz, LatLon observer, int year){
		Date iniDate = DateUtil.createDate(tz, year, 0, 1, 12, 0);
		Date endDate = DateUtil.createDate(tz, year+1, 0, 1);
		DateIterable it1 = new DateIterable(tz, iniDate, endDate, Calendar.DAY_OF_YEAR, 1);
		Date[] ret = null;
		double minDiff = Double.MAX_VALUE;
		Calendar c = Calendar.getInstance(tz);
		for (Date date1 : it1) {

			AzimutAltitude azal1 = SunPosition.getAzimutAltitude(observer, date1);
			c.setTime(date1);
			int day1 = c.get(Calendar.DAY_OF_YEAR);
		   		
			
			DateIterable it2 = new DateIterable(tz, date1, endDate, Calendar.DAY_OF_YEAR, 1);
			for (Date date2 : it2) {

				c.setTime(date2);
				int day2 = c.get(Calendar.DAY_OF_YEAR);
				if( Math.abs(day1 - day2) < 60 ){
					continue;
				}
				
				AzimutAltitude azal2 = SunPosition.getAzimutAltitude(observer, date2);
			   	double al = azal2.getAltitude() - azal1.getAltitude();
			   	double az = azal2.getAzimut() - azal1.getAzimut();
			   	
				double diff = Math.sqrt(al*al + az*az);
				if( diff < minDiff ){
					ret = new Date[]{ date1, date2 };
					minDiff = diff;
				}
			}
			
			
		}
			
		
		if( ret == null ){
			throw new RuntimeException("No lo esperaba");
		}
		return ret;
	
	}
}