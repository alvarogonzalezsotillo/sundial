package computation;
import java.awt.geom.Point2D;


public class Point2d extends Point2D.Double{

	public Point2d( Point2D p ){
		super( p.getX(), p.getY() );
	}
	
	public Point2d() {
		super();
	}

	public Point2d(double x, double y) {
		super(x, y);
	}

	
}
