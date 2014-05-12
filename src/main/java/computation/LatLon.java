package computation;
import java.awt.geom.Point2D;


public class LatLon extends Point2D.Double{

	public LatLon() {
		super();
	}

	public LatLon(double lat, double lon) {
		super(lat, lon);
	}
 
	public double getLat(){
		return getX();
	}
	
	public double getLon(){
		return getY();
	}
	
	void setLat(double lat){
		setLocation(lat, getY());
	}
	
	void setLog(double lon){
		setLocation(getX(), lon);
	}
}
