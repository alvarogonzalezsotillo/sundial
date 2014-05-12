package computation.geom;
import java.awt.geom.Point2D;



public class AzimutAltitude extends Point2D.Double{


	public static double degreesToDMS(double degrees){
		double d = Math.floor(degrees);
		double m = Math.floor((degrees-d)*60);
		double s = 60*60*(degrees-d-m/60);
		
		return d + m/100 + s/10000;
	}
	
	public static double angleInZero360(double angle){
		while( angle < 0 ){
			angle += 360;
		}
		while( angle > 360 ){
			angle -= 360;
		}
		return angle;
	}

	public static double angleInMinus180Plus180(double angle){
		angle = angleInZero360(angle);
		while( angle > 180 ){
			angle -= 360;
		}
		return angle;
	}
	
	public static double DMSToDegrees(double DMS ){
		double d = Math.floor(DMS);
		double m = Math.floor( (DMS-d) * 100 );
		double s = (DMS - d - m/100)*10000;
		
		return d + m*1/60d + s*1/(60d*60d);
	}
	
	
	public AzimutAltitude() {
		this(0,0);
	}
	
	private static void normalize(AzimutAltitude ret) {
		ret.setAltitude( angleInMinus180Plus180(ret.getAltitude()));
		ret.setAzimut( angleInMinus180Plus180(ret.getAzimut()));
	}

	public AzimutAltitude(double az, double al) {
		super(az, al);
		normalize(this);
	}
 
	public double getAzimut(){
		return getX();
	}
	
	public double getAltitude(){
		return getY();
	}
	
	void setAzimut(double az){
		az = angleInMinus180Plus180(az);
		setLocation(az, getY());
	}
	
	public boolean same( AzimutAltitude azal ){
		return GeomUtil.areEqual( getAltitude(), azal.getAltitude(), GeomUtil.DELTA) 
		       && GeomUtil.areEqual( getAzimut(), azal.getAzimut(), GeomUtil.DELTA); 
	}
	
	void setAltitude(double al){
		al = angleInMinus180Plus180(al);
		setLocation(getX(), al);
	}

	@Override
	public String toString() {
		return "az: " + getAzimut() + "  al:" + getAltitude();
	}
}

