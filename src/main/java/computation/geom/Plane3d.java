package computation.geom;

import computation.algebra.Matrix;

public class Plane3d {
	private Point3d _p;
	private Point3d _normal;
	private double _d = Double.NaN;
	
	public Plane3d(Point3d normal){
		this( normal, Point3d.ZERO );
	}
	
	public Plane3d(Point3d normal, Point3d point ){
		_p = point.copy();
		_normal = normal.copy();
		_d = -_p.scalarProduct(getNormal() );
	}

	public boolean contains( Point3d p ){
		
		p = p.sum( _p.scale(-1) );

		double product = p.scalarProduct(getNormal());
		
		return GeomUtil.areEqual(0, product, GeomUtil.DELTA);
	}
		
	public double a(){
		return getNormal().x();
	}
	
	public double b(){
		return getNormal().y();
	}
	
	public double c(){
		return getNormal().z();
	}
	/**
	 * 
	 * normal.x*x + normal.y*y + normal.z*z + d = 0
	 */
	public double d(){
		return _d;
	}

	public Point3d getNormal() {
		return _normal.copy();
	}
	
	public Point3d computeIntersection( Line3d line ){
		Point3d normal = getNormal();
		Point3d direction = line.getDirection();
		Point3d p = line.getPoint();
		
		if( GeomUtil.isZero( normal.scalarProduct(direction), GeomUtil.DELTA )){
			return null;
		}
		
		double lambda = -d() - normal.scalarProduct(p) / normal.scalarProduct(direction);
		
		Point3d ret = line.substituteLambda(lambda);
		
		if( GeomUtil.PARANOID ){
			if( !contains(ret) ){
				throw new IllegalStateException();
			}
			if( !line.contains(ret) ){
				throw new IllegalStateException();
			}
		}
		
		return ret;
	}
	
	public Point3d computeProyection( Point3d point ){
		Point3d normal = getNormal();
		Point3d p = _p;
		
		double lambda = (-normal.scalarProduct(point) + normal.scalarProduct(p))/normal.scalarProduct(normal);
	
		Point3d ret = point.sum( normal.scale(lambda) );
		return ret;
	}
	
	
	public Line3d computeIntersection(Plane3d p){
		
		// EL VECTOR DE LA RECTA ES PERPENDICULAR A LAS DOS NORMALES
		Point3d direction = getNormal().vectorialProduct(p.getNormal());
		double module = direction.module();
		if( GeomUtil.isZero(module, GeomUtil.DELTA) ){
			return null;
		}
		
		// HACEMOS UN PLANO CON NORMAL LA DIRECCION DE LA RECTA
		Plane3d perp = new Plane3d( direction, new Point3d(0,0,0) );
		
		// LA RECTA PASA POR DONDE CORTE ESTE PLANO CON LOS OTROS DOS
		Point3d point = computeIntersection( new Plane3d[]{ this, p, perp } );
		
		if( GeomUtil.PARANOID ){
			if( point == null ){
				throw new IllegalStateException();
			}
			if( !p.contains(point) ){
				throw new IllegalStateException();
			}
			if( !contains(point) ){
				throw new IllegalStateException();
			}
			if( !p.contains( point.sum(direction) ) ){
				throw new IllegalStateException();
			}
			if( !contains( point.sum(direction) ) ){
				throw new IllegalStateException();
			}
		}
		
		Line3d ret = new Line3d( direction, point );
		return ret;
	}
	
	private static Point3d computeIntersection(Plane3d[] planes){
		if( planes.length != 3 ){
			throw new RuntimeException();
		}
		Matrix m = new Matrix(3,4);
		for (int i = 0; i < planes.length; i++) {
			Plane3d plane = planes[i];
			m.set(i, 0, plane.a());
			m.set(i, 1, plane.b());
			m.set(i, 2, plane.c());
			m.set(i, 3, plane.d());
		}

		double[] variables = m.solveEcuationSystem();
		if( variables == null ){
			return null;
		}
		
		return new Point3d( variables );
	}
	
}
