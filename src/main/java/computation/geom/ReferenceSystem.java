package computation.geom;

import computation.algebra.Matrix;

public class ReferenceSystem {

	private Point3d _o;
	private Point3d _v[];

	/**
	 * 
	 * @param origin
	 * @param v
	 */
	public ReferenceSystem( Point3d origin, Point3d v[] ){
		if( v.length != 3 ){
			throw new IllegalArgumentException();
		}
		if( !areIndependent(v) ){
			Matrix m = createMatrix(v);
			System.out.println( "Vectores de la base");
			Matrix.l(m);
			System.out.println( "Vectores de la base triangularizados");
			m.triangularize();
			Matrix.l(m);
			throw new IllegalArgumentException("No son independientes");
		}
		_o = origin.copy();
		_v = v.clone();
		for (int i = 0; i < v.length; i++) {
			v[i] = v[i].copy();
		}
	}
	
	public void normalize(){
		for( int i = 0 ; i < _v.length ; i++ ){
			_v[i] = _v[i].scale( 1./_v[i].module() );
		}
	}
	
	private static Matrix createMatrix( Point3d v[] ){
		Matrix m = new Matrix(v.length,3);
		for( int row = 0 ; row < v.length ; row++ ){
			m.set(row, 0, v[row].x() );
			m.set(row, 1, v[row].y() );
			m.set(row, 2, v[row].z() );
		}
		return m;
	}
	
	public static boolean areIndependent( Point3d v[] ){
	
		if( v.length > 3 ){
			return false;
		}
		
		Matrix m = createMatrix(v);
		m.triangularize();
		
		for( int col = 0 ; col < 3 ; col++ ){
			double d = m.get(v.length-1, col);
			if( !GeomUtil.isZero(d, GeomUtil.DELTA ) ){
				return true;
			}
		}
		return false;
	}
	
	public static boolean areOrthogonal( Point3d v[] ){
		for (int i = 0; i < v.length; i++) {
			Point3d vi = v[i];
			
			for (int j = i+1; j < v.length; j++) {
				Point3d vj = v[j];

				double angle = vi.angleWith(vj);
				
				if( !GeomUtil.areEqual( angle, 90, GeomUtil.DELTA) ){
					return false;
				}
			}
		}
		return true;
	}
	
	public Point3d computePoint(double[] coords){
		if( coords.length != 3 ){
			throw new IllegalArgumentException();
		}
		Point3d ret = _o;
		ret = ret.sum( _v[0].scale(coords[0]));
		ret = ret.sum( _v[1].scale(coords[1]));
		ret = ret.sum( _v[2].scale(coords[2]));
		return ret;
	}
	
	public double[] computeCoordinates(Point3d point){
		Point3d p = point.sum( _o.scale(-1) );

	
		Matrix m = new Matrix(3,4);
		
		for( int column = 0 ; column < 3 ; column++ ){
			m.set( 0, column, _v[column].x() );
			m.set( 1, column, _v[column].y() );
			m.set( 2, column, _v[column].z() );
		}
		m.set(0, 3, -p.x());
		m.set(1, 3, -p.y());
		m.set(2, 3, -p.z());
		
		double[] ret = m.solveEcuationSystem();
		
		if( GeomUtil.PARANOID ){
			if( !computePoint(ret).same( point ) ){
				System.out.println( this );
				System.out.println( point );
				Matrix.l(m);
				System.out.println( "coordenadas: " + new Matrix(ret,false) );
				System.out.println( "punto segun coordenadas:" + computePoint(ret) );
				throw new IllegalStateException();
			}
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return "O:" + _o + " V:" + _v[0] + _v[1] + _v[2];
	}
	
	public static void main(String[] args) {
		Point3d o = new Point3d(1,-1,0);
		
		Point3d v[] = {
			new Point3d(1,0,0),
			new Point3d(0,0,1),
			new Point3d(0,1,1)
		};
		
		ReferenceSystem rs = new ReferenceSystem(o, v);
		
		Point3d p = new Point3d(10,20,-30);
		System.out.println( p );
		double[] coordinates = rs.computeCoordinates(p);
		System.out.println( new Matrix(coordinates,false));
		System.out.println( rs.computePoint(coordinates));
	}

	public boolean isOrthogonal() {
		return areOrthogonal(_v);
	}
	
	
}
