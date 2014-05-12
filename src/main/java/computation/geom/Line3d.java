package computation.geom;

public class Line3d {
	private Point3d _v;
	private Point3d _p;
	
	
	public Line3d( Point3d v, Point3d p ){
		_v = v.copy();
		_p = p.copy();
	}
	
	public Point3d getDirection(){
		return _v.copy();
	}
	
	public Point3d getPoint(){
		return _p.copy();
	}
	
	public boolean contains( Point3d p ){
		p = p.sum( _p.scale(-1) );
		double lx = p.x()/_v.x();
		double ly = p.y()/_v.y();
		double lz = p.z()/_v.z();
		
		return GeomUtil.areEqual(lx, ly, GeomUtil.DELTA) && GeomUtil.areEqual(lx, lz, GeomUtil.DELTA);
	}

	public Point3d substituteLambda(double lambda) {
		return _p.sum( _v.scale(lambda) );
	}
	
	public Point3d computeProyection( Point3d point ){
		Point3d direction = getDirection();
		Point3d p = getPoint().scale(-1).sum(point);
		double lambda = p.scalarProduct(direction) / direction.scalarProduct( new Point3d(1,1,1) );
		Point3d ret = substituteLambda(lambda);
		return ret;
	}

}
