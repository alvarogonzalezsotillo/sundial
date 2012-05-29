package computation.geom;


public class Point3d{
	private double x;
	private double y;
	private double z;
	
	public static final Point3d ZERO = new Point3d(0,0,0);
	
	public Point3d( double[] coords ){
		this(coords[0], coords[1], coords[2] );
	}
	
	public Point3d( double x, double y, double z ){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3d scale( double s ){
		return new Point3d( x*s,y*s,z*s );
	}
	
	public double module(){
		return Math.sqrt( x*x + y*y + z*z );
	}
	
	public double scalarProduct( Point3d p ){
		return x*p.x + y*p.y + z*p.z;
	}
	
	public Point3d sum( Point3d p ){
		return new Point3d( x+p.x, y+p.y, z+p.z );
	}
	
	
	public Point3d copy(){
		return new Point3d(x,y,z);
	}

	public double x() {
		return x;
	}
	public double y() {
		return y;
	}
	public double z() {
		return z;
	}

	@Override
	public String toString() {
		return "(" + x() + "," + y() + "," + z() + ")";
	}
	
	public double angleWith(Point3d p){
		double cos = scalarProduct(p)/( p.module() * module() );
		double angle = Math.acos(cos);
		
		return Math.toDegrees(angle);
	}
	
	
	public static AzimutAltitude toAzimutAltitude( Point3d p ){
		// LA ALTITUD ES EL ANGULO ENTRE EL PUNTO Y OTRO CON z=0
		Point3d pConZACero = new Point3d( p.x(), p.y(), 0 );
		double altitude = p.angleWith( pConZACero );
		if( p.z() < 0 ){
			altitude *= -1;
		}
		
		// EL AZIMUT ES EL ANGULO ENTRE EL PUNTO CON z=0 Y EL EJE X
		Point3d ejeX = new Point3d(1,0,0);
		double azimut = ejeX.angleWith(pConZACero);
		if( p.y() < 0 ){
			azimut *= -1;
		}
		return new AzimutAltitude( azimut, altitude );
	}
	
	/**
	 * altitude crece hacia la z
	 * en la direccion de x azimut es 0
	 * @param azal
	 * @param radius
	 */
	public static Point3d fromAzimutAltitude(AzimutAltitude azal, double radius){
		double altitude = azal.getAltitude();
		double azimut = azal.getAzimut();
	
		altitude = Math.toRadians(altitude);
		azimut = Math.toRadians(azimut);
		
		double z = radius*Math.sin(altitude);
		double r = radius*Math.cos(altitude);
		
		double x = r*Math.cos(azimut);
		double y = r*Math.sin(azimut);
		
		Point3d ret = new Point3d(x,y,z);
		if( GeomUtil.PARANOID ){
			if( !GeomUtil.areEqual( ret.module(), radius, GeomUtil.DELTA ) ){
				throw new IllegalStateException();
			}
		}
		return ret;
	}
	
	public Point3d vectorialProduct(Point3d p){
		double x = y()*p.z() - p.y()*z();
		double y = p.x()*z() - x()*p.z();
		double z = x()*p.y() - p.x()*y();
		
		return new Point3d(x,y,z);
	}

	public static void main(String[] args) {
		for( int altitude = 0 ; altitude < 90 ; altitude += 10 ){
			for( int azimut = -90 ; azimut < 90 ; azimut++ ){
				AzimutAltitude azal = new AzimutAltitude(azimut, altitude);
				Point3d p = fromAzimutAltitude(azal, 10);
				AzimutAltitude azal2 = toAzimutAltitude(p);
				System.out.println( azal + " --- " + p + " --- " + azal2 + " --- " + azal.same(azal2) );
			}
		}
	}

	public boolean same(Point3d p) {
		return GeomUtil.areEqual(x(), p.x(), GeomUtil.DELTA)
			   && GeomUtil.areEqual(y(), p.y(), GeomUtil.DELTA)
			   && GeomUtil.areEqual(z(), p.z(), GeomUtil.DELTA);
	}
	
}
