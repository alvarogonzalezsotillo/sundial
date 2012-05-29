package computation;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import computation.geoastro.compute;
import computation.geom.AzimutAltitude;
import computation.sideralis.data.Sky;
import computation.sideralis.location.Position;
import computation.sideralis.location.Temps;
import computation.sideralis.projection.sphere.SunProj;

import date.DateIterable;
import date.DateUtil;


public class SunPosition {
	
	public static final int REFERENCEYEAR = 2015;

	/**
	 * Angulos entre [-180, 180)
	 * 
	 * Altitude: 90 arriba, 0 horizonte
	 * Azimut: Negativo en la mañana, positivo en la tarde, 0 en el sur
	 * @param observer
	 * @param time
	 * @return
	 */
	public static AzimutAltitude getAzimutAltitude( LatLon observer, Date time ){
//		AzimutAltitude ret = getAzimutAltitude_geoastro(observer, time);
		AzimutAltitude ret = getAzimutAltitude_sideralis2(observer, time);
		return ret;
	}



	private static final compute c = new compute();
	
	private static AzimutAltitude getAzimutAltitude_geoastro(LatLon observer,Date time) {
		AzimutAltitude ret = c.myCompute(time,observer);
		ret = new AzimutAltitude( ret.getAzimut()-180, ret.getAltitude() );
		return ret;
	}
	private static final Position position = new Position();
	private static final Sky sky = new Sky(position);
	
	private static AzimutAltitude getAzimutAltitude_sideralis2(LatLon observer, Date time ){
		position.setLatitude( observer.getLat());
		position.setLongitude( -observer.getLon());
		Temps temps = position.getTemps();
		temps.calculateTimeOffset(time);
		temps.adjustDate();
		sky.calculateTimeVariables();
		
		SunProj sun = sky.getSun();
		sun.calculate();
		AzimutAltitude ret = new AzimutAltitude( Math.toDegrees(sun.getAzimuth())-90, Math.toDegrees(sun.getHeight()) );
		return ret;
	}
	
	public static void test(){
		SunDialData data = SunDialData.DEFAULT;
		TimeZone tz = data.getTimeZone();

		Date time_b = DateUtil.createDate(tz, 2015, Calendar.JANUARY, 1);
		Calendar cal = Calendar.getInstance(tz);
		for( double lat = -89 ; lat <= 89 ; lat += 5 ){
			for( double lon = -179 ; lon <= 179 ; lon += 5 ){
				LatLon observer = new LatLon( lat, lon );
				for( int day = 0 ; day < 365 ; day += 5 ){
					for( int hour = 9 ; hour < 18 ; hour++ ){
						cal.setTime(time_b);
						cal.add(Calendar.DAY_OF_YEAR, day);
						cal.add(Calendar.HOUR_OF_DAY,hour);
						Date time = cal.getTime();
			
						AzimutAltitude aa_geoastro = getAzimutAltitude_geoastro(observer, time);
						AzimutAltitude aa_sideralis2 = getAzimutAltitude_sideralis2(observer, time);
				
						double distance_al = Math.abs( aa_geoastro.getAltitude() - aa_sideralis2.getAltitude() );
						double distance_az = Math.abs( aa_geoastro.getAzimut() - aa_sideralis2.getAzimut() );
						distance_al = AzimutAltitude.angleInMinus180Plus180(distance_al);
						distance_az = AzimutAltitude.angleInMinus180Plus180(distance_az);
						System.out.println( observer.getLat() + " \t" + observer.getLon() + "\t" + day + "\t" + hour + "\t" + distance_al + "\t" + distance_az );
			
						double tolerance = 1;
						if( Math.abs(distance_al) > tolerance || Math.abs(distance_az) > tolerance ){
							throw new RuntimeException( "Demasiado grande");
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		//sync_geoastro_sideralis();
		test();
		System.exit(0);
		
		SunDialData data = SunDialData.DEFAULT;
		LatLon observer = data.getLatLon();
		TimeZone tz = data.getTimeZone();

		
		Calendar c = Calendar.getInstance(tz);
		c.set( Calendar.HOUR_OF_DAY,8);
		c.set(Calendar.MINUTE, 0);
		Date iniDate = c.getTime();
		c.set(Calendar.HOUR_OF_DAY,22);
		Date endDate = c.getTime();
		
		DateIterable it = new DateIterable( data.getTimeZone(), iniDate, endDate, Calendar.MINUTE, 15 );

		for (Date date : it) {
			AzimutAltitude azal_geoastro = SunPosition.getAzimutAltitude_geoastro(observer, date);
			AzimutAltitude azal_sideralis2 = SunPosition.getAzimutAltitude_sideralis2(observer, date);
			System.out.println( date + ":   " + azal_geoastro );
			System.out.println( date + ":   " + azal_sideralis2 );
		}
		
	}
}
