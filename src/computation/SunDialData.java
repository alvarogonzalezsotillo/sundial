package computation;

import java.awt.Rectangle;
import java.util.TimeZone;

import computation.geom.AzimutAltitude;


public class SunDialData {
	private LatLon _latLon;
	private AzimutAltitude _wallNormal;
	private TimeZone _timeZone;

	private Point2d _gnomonBase; // mm
	private double _gnomonHeight; // mm
	
	private Rectangle _dialBounds;
	
	// 040:31:34, 003:17:42, 0, 43:01:32
	public static final SunDialData SUR = new SunDialData( 
			new LatLon(40.526, 3.295),
			new AzimutAltitude( /*0*/43.03, 0 ),
			//TimeZone.getTimeZone("Europe/Madrid"),
			TimeZone.getTimeZone("GMT+1"),
			new Point2d(450/2,50),
			300,
			new Rectangle(0,0,450,900));
	

	
	public static final SunDialData ESTE = new SunDialData( 
			new LatLon(40.526, 3.295),
			new AzimutAltitude( /*0*/43.03-90, 0 ),
			//TimeZone.getTimeZone("Europe/Madrid"),
			TimeZone.getTimeZone("GMT+1"),
			new Point2d(450/2,50),
			300,
			new Rectangle(0,0,450,900));

	public static final SunDialData OESTE = new SunDialData( 
			new LatLon(40.526, 3.295),
			new AzimutAltitude( /*0*/43.03+90, 0 ),
			//TimeZone.getTimeZone("Europe/Madrid"),
			TimeZone.getTimeZone("GMT+1"),
			new Point2d(450/2,50),
			300,
			new Rectangle(0,0,450,900));

	public static final SunDialData ECUADOR = new SunDialData( 
			new LatLon(0, 3.295),
			new AzimutAltitude( 0, 180 ),
			//TimeZone.getTimeZone("Europe/Madrid"),
			TimeZone.getTimeZone("GMT"),
			new Point2d(450/2,50),
			300,
			new Rectangle(0,0,450,900));
	
	public static final SunDialData DEFAULT = ESTE;
	
	public SunDialData(LatLon latLon, AzimutAltitude wallNormal, TimeZone timeZone, Point2d gnomonBase, double gnomonHeight, Rectangle dialBounds) {
		super();
		_latLon = latLon;
		setWallNormal(wallNormal);
		setTimeZone(timeZone);
		setGnomonBase(gnomonBase);
		setGnomonHeight(gnomonHeight);
		setDialBounds(dialBounds);
	}

	public LatLon getLatLon() {
		return _latLon;
	}

	public void setLatLon(LatLon latLon) {
		_latLon = latLon;
	}

	public void setTimeZone(TimeZone timeZone) {
		_timeZone = timeZone;
	}

	public TimeZone getTimeZone() {
		return _timeZone;
	}

	public void setWallNormal(AzimutAltitude wallNormal) {
		_wallNormal = wallNormal;
	}

	public AzimutAltitude getWallNormal() {
		return _wallNormal;
	}

	public void setGnomonBase(Point2d gnomonBase) {
		_gnomonBase = gnomonBase;
	}

	public Point2d getGnomonBase() {
		return _gnomonBase;
	}

	public void setGnomonHeight(double gnomonHeight) {
		_gnomonHeight = gnomonHeight;
	}

	public double getGnomonHeight() {
		return _gnomonHeight;
	}

	public void setDialBounds(Rectangle dialBounds) {
		_dialBounds = dialBounds;
	}

	public Rectangle getDialBounds() {
		return _dialBounds;
	}

}
