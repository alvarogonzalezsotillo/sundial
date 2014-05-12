package computation.geom;
import computation.Point2d;
import computation.SunDialData;
import computation.algebra.Matrix;



public class GeomUtil {

	public static final boolean PARANOID = false;
	

	public static AzimutAltitude computeAzimutAltitude( SunDialData data, Point2d proyection ){
	
		// PASO LA PROYECCION COMO SI FUERA DE UN GNOMON DE ALTURA 1 EN (0,0)
		double gnomonHeight = data.getGnomonHeight();
		Point2d gnomonBase = data.getGnomonBase();
		//System.out.println( "<- proyection: " + proyection );
		proyection = new Point2d( proyection.getX()-gnomonBase.getX(), proyection.getY()-gnomonBase.getY());
		proyection = new Point2d( proyection.getX()/gnomonHeight, proyection.getY()/gnomonHeight );
		
		//System.out.println( "<- proyection: " + proyection );

		
		// PLANO DE LA PARED Y GNOMON
		AzimutAltitude wallNormal = data.getWallNormal();
		Point3d gnomon = Point3d.fromAzimutAltitude(wallNormal, 1);
		Plane3d wall = new Plane3d( (Point3d)gnomon );

		//System.out.println( "<- gnomon: " + gnomon );

		// PLANOS HORIZONTAL Y VERTICAL PARA CORTAR LA PARED
		Plane3d planoHorizontal = new Plane3d( new Point3d(0,0,1), Point3d.ZERO );
		Point3d horizontal = wall.computeIntersection( planoHorizontal ).getDirection();
		
		Plane3d planoVertical = new Plane3d( (Point3d)gnomon.vectorialProduct( new Point3d(0,0,1) ) );
		Point3d vertical = wall.computeIntersection( planoVertical ).getDirection();

		Point3d normal = vertical.vectorialProduct(horizontal);
		
		
		// PASO LA PROYECCION A 3D
		ReferenceSystem rs = new ReferenceSystem( Point3d.ZERO, new Point3d[]{horizontal, vertical, normal} );
		rs.normalize();
		double[] coordinates = new double[]{ proyection.getX(), proyection.getY(), 0 };
		//System.out.println( "<- coordinates: " + new Matrix(coordinates,false) );

		Point3d shadow = rs.computePoint(coordinates);
		//System.out.println( "<- shadow: " + shadow );

		// VECTOR DESDE shadow HASTA EL gnomon
		Point3d sunDirection = gnomon.sum(shadow.scale(-1));
		//System.out.println( "<- sunDirection: " + sunDirection );

		
		AzimutAltitude azimutAltitude = Point3d.toAzimutAltitude(sunDirection);
		//System.out.println( "<- azimutAltitude: " + azimutAltitude );
		
		return azimutAltitude;
	}
	
	/**
	 * 
	 * @param data
	 * @param gnomonBase
	 * @param gnomonHeight
	 * @param azal
	 * @return
	 */
	public static Point2d computeProyection( SunDialData data,  AzimutAltitude azal ){
		
		if( azal.getAltitude() < 0 ){
			return null;
		}
		
		AzimutAltitude wallNormal = data.getWallNormal();
		double azimut = azal.getAzimut() - wallNormal.getAzimut();
		double altitude = azal.getAltitude() - wallNormal.getAltitude();
		
		if( altitude < -90 || altitude > 90 ){
			return null;
		}
		if( azimut < -90 || azimut > 90 ){
			return null;
		}

		// CALCULO DE LA SOMBRA EN EL PLANO DE LA PARED
		Point3d gnomon = Point3d.fromAzimutAltitude(wallNormal, 1);
		Plane3d wall = new Plane3d( (Point3d)gnomon );
		Point3d sunDirection = (Point3d) Point3d.fromAzimutAltitude(azal, 1);
		Line3d sunRay = new Line3d( sunDirection, gnomon );
		Point3d shadow = wall.computeIntersection(sunRay);

		
		//System.out.println( "-> azal: " + azal );
		//System.out.println( "-> sunDirection: " + sunDirection );
		//System.out.println( "-> gnomon: " + gnomon );
		//System.out.println( "-> shadow: " + shadow );
		
		// PLANOS HORIZONTAL Y VERTICAL PARA CORTAR LA PARED
		Plane3d planoHorizontal = new Plane3d( new Point3d(0,0,1), Point3d.ZERO );
		Point3d horizontal = wall.computeIntersection( planoHorizontal ).getDirection();
		
		Plane3d planoVertical = new Plane3d( (Point3d)gnomon.vectorialProduct( new Point3d(0,0,1) ) );
		Point3d vertical = wall.computeIntersection( planoVertical ).getDirection();

		Point3d normal = vertical.vectorialProduct(horizontal);
		if( PARANOID ){
			//ver si normal es como el azal de la pared
			AzimutAltitude azal2 = Point3d.toAzimutAltitude(normal);
			if( azal2.same(azal) ){
				throw new IllegalStateException();
			}
			
			if( ReferenceSystem.areIndependent( new Point3d[]{ normal, gnomon } ) ){
				throw new IllegalStateException();
			}
		}
		
		ReferenceSystem rs = new ReferenceSystem( Point3d.ZERO, new Point3d[]{horizontal, vertical, normal} );
		rs.normalize();
		double[] coordinates = rs.computeCoordinates(shadow);
		if( PARANOID ){
			if( !rs.isOrthogonal() ){
				throw new IllegalStateException();
			}
			if( !isZero(coordinates[2], DELTA) ){
				//System.out.println( "shadow:" +shadow );
				//System.out.println( rs );
				//System.out.println( new Matrix(coordinates, false) );
				throw new IllegalStateException();
			}
		}

		
		Point2d gnomonBase = data.getGnomonBase();
		double gnomonHeight = data.getGnomonHeight();
		
		
		//System.out.println( "-> Coordenadas: " + new Matrix( coordinates, false) );
		Point2d ret = new Point2d( gnomonBase.x + coordinates[0]*gnomonHeight,
				                   gnomonBase.y + coordinates[1]*gnomonHeight );
		
		//System.out.println( "-> ret: " + ret );

		if( PARANOID ){
			AzimutAltitude aa = computeAzimutAltitude(data, ret);
			if( !isZero(aa.distance(azal), DELTA ) ){
				//System.out.println( "azal:" + azal );
				//System.out.println( "ret:" + ret );
				//System.out.println( "aa:" + aa );
				throw new IllegalStateException();
			}
		}
		
		return ret;
	}
	

	
	public static boolean areEqual(double d1, double d2, double delta){
		return isZero(d1-d2, delta);
	}
	
	public static boolean isZero( double d, double delta){
		return Math.abs(d) < delta;
	}

	static public final double DELTA = 0.000001;

	
	
	
	
	public static void main(String[] args) {
		/*
		Line3d line = new Line3d( new Point3d(0,1,0), new Point3d(0,0,0) );
		Point3d p = new Point3d( 10, 10, 10);
		Point3d proyection = line.computeProyection(p);
		System.out.println( proyection );
        */
		
		/*
		Plane3d plane = new Plane3d( new Point3d(1,0,0), new Point3d( 1, 1, 1) );
		Line3d line = new Line3d( new Point3d(1,0,1), new Point3d(0,0,0) );
		
		Point3d intersection = plane.computeIntersection(line);
		System.out.println(intersection);
		*/
		
		Plane3d plane = new Plane3d( new Point3d(0,1,0), new Point3d( 1, 1, 1) );
		Point3d p = new Point3d( 90, 100, 100 );
		
		Point3d proyection = plane.computeProyection(p);
		System.out.println( proyection );
	}


}
