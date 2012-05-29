package gui;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import computation.AzimutAltitudeFinder;
import computation.LatLon;
import computation.Point2d;
import computation.SunDialData;
import computation.SunPosition;
import computation.geom.AzimutAltitude;
import computation.geom.GeomUtil;

import date.DateInterval;
import date.DateIterable;
import date.DateUtil;
import date.IDateIterable;
import date.ListDateIterable;


public class AnalemmaPainter extends SunDialPainter {
	
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("dd/MMM");
	private static final DateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm");
	private static final DateFormat DATETIMEFORMAT = new SimpleDateFormat("dd/MMM HH:mm");
	private static final DateFormat MONTHFORMAT = new SimpleDateFormat("MMMM");
	private static final boolean HOMENAJE = true;

	

	Shape createVerticalLine( Graphics2D g, SunDialPanel panel ){
		
		SunDialData data = getData();
		Point2d gnomonBase = data.getGnomonBase();
		
		LatLon observer = data.getLatLon();
		
		Date date = computeTimeOfVerticalShadowInCross()[0];
		if( date == null ){
			return null;
		}

		DateIterable it = getAnalemmaDateIterator(date, Calendar.DAY_OF_YEAR, 1); 
		AzimutAltitude azalMax = null;
		AzimutAltitude azalMin = null;
		for (Date d : it) {
			AzimutAltitude azal = SunPosition.getAzimutAltitude(observer, d);
			if( azalMax == null || azalMax.getAltitude() < azal.getAltitude()){
				azalMax = azal;
			}
			if( azalMin == null || azalMin.getAltitude() > azal.getAltitude()){
				azalMin = azal;
			}
		}

		
		Point2d pMax = project(azalMax);

		Point2d pMin = project(azalMin);

		if( pMin != null && pMax != null ){
			double x =  gnomonBase.x;
			double y1 =  gnomonBase.y;
			double y2 = pMax.y + pMin.y-y1 + 10; // 10 ES CHANCHULLO
			
			Shape ret = new Line2D.Double(x,y1,x,y2);
			ret = PaintUtil.MEDIUMSTROKE.createStrokedShape(ret);
			return ret;
		}
		return null;
	}
	
	
	@Override
	protected void fillShapes(Graphics2D g, SunDialPanel panel){
		
		TimeZone tz = getData().getTimeZone();
		setTimeZone( tz );
		Shapes shapes = getShapes();
		boolean noShapes = shapes.size() == 0;
		super.fillShapes(g, panel);
		if( noShapes ){
			
			// ANALEMMA
			Shape analemma = createAnalemmaOfVerticalShadowInCross(g, panel);
			shapes.add( analemma );
			
			// SHORT DAY LINES
			List<Shape> dayLines = createDayLinesBetweenVerticalAndAnalemmaOfVerticalShadowInCross( g, panel );
			shapes.addAll( dayLines );
			
			// LONG DAY LINES
			List<Shape> longDayLines = createDayLinesInsidePrintArea( g, panel, getRepresentedDatesIterable(true,false), false );
			for( Shape s: longDayLines){
				shapes.add( PaintUtil.VERYTHINSTROKE.createStrokedShape(s) );
			}
			
			// VERTICAL LINE
			Shape verticalLine = createVerticalLine(g,panel);
			shapes.add( verticalLine );


			// DATES
			List<Shape> l = createVerticalDatesOnDayLines(g, panel, longDayLines );
			shapes.addAll( l );
			
			// HOMENAJE
			if( HOMENAJE ){
				shapes.add( createHomenaje( g, panel ) );
			}
			
			
			// LEGEND
			List<Shape> legend = createLegend(g, panel);
			shapes.addAll( legend );
			
			// HORAS
			List<Shape> horas = createHoursOnDayLines(g,panel);
			shapes.addAll(horas);
		}
	}
	

	private List<Shape> createHoursOnDayLines(Graphics2D g, SunDialPanel panel) {
		IDateIterable it = getRepresentedDatesIterable(true,false);
		TimeZone tz = getData().getTimeZone();
		LatLon observer = getData().getLatLon();
		Rectangle dialBounds = getData().getDialBounds();
		List<Shape> ret = new ArrayList<Shape>();
		
		for( Date day : it ){
			Date ini = DateUtil.firstSecondOfDate(tz, day);
			Date end = DateUtil.lastSecondOfDate(tz, day);
			
			for( Date d : new DateIterable(tz, ini, end, Calendar.HOUR, 1 ) ){
				AzimutAltitude aa = SunPosition.getAzimutAltitude(observer, d);
				Point2d p = project(aa);
				if( p == null || !dialBounds.contains(p) ){
					continue;
				}
				
				Shape s = PaintUtil.createMark(p,8);
				ret.add(s);
			}
		}
		
		return ret;
	}


	private List<Shape> createLegend(Graphics2D g, SunDialPanel panel) {
		SunDialData data = getData();
		Rectangle dial = data.getDialBounds();
		double golden = (1 + Math.sqrt(5))/2;
		
		// RECUADRO TOTAL
		Rectangle rectangle = new Rectangle( 0, 0, (int)(dial.width/2.5), (int) ((dial.width/2.5)/golden) );
		
		// RECUADRO
		double margin = 20;
		Shape innerRectangle = new Rectangle( (int)(rectangle.x+margin), (int)(rectangle.y+margin), (int)(rectangle.width-margin*2), (int) (rectangle.height-margin*2));
		innerRectangle = PaintUtil.VERYTHINSTROKE.createStrokedShape(innerRectangle);

		// RECUADRO DEL TEXTO
		double margin2 = margin+10;
		Shape textRectangle = new Rectangle( (int)(rectangle.x+margin2), (int)(rectangle.y+margin2), (int)(rectangle.width-margin2*2), (int) (rectangle.height-margin2*2));

		int cursorY = 0;
		int cursorX = 0; 
		
		// POSICION
		LatLon latLon = data.getLatLon();
		String latLonS = PaintUtil.sprintf( "Lat %.4f  Lon %.4f", latLon.getLat(), latLon.getLon() );
		Shape latLonShape = PaintUtil.getShapeFromText( latLonS, cursorX, cursorY, PaintUtil.LEGENDFONT, true );
		cursorY -= PaintUtil.getTextBounds(latLonS, PaintUtil.LEGENDFONT, g ).getHeight()*2;

		// ORIENTACION
		double azimut = data.getWallNormal().getAzimut();
		String azimutS = PaintUtil.sprintf( "Orientación %.1f", azimut );
		Shape azimutShape = PaintUtil.getShapeFromText( azimutS, cursorX, cursorY, PaintUtil.LEGENDFONT, true );
		cursorY -= PaintUtil.getTextBounds(azimutS, PaintUtil.LEGENDFONT, g ).getHeight()*2;
		
		// TIMEZONE
		TimeZone tz = data.getTimeZone();
		String tzS = PaintUtil.sprintf( "Zona horaria %s", tz.getID() );
		Shape tzShape = PaintUtil.getShapeFromText( tzS, cursorX, cursorY, PaintUtil.LEGENDFONT, true );
		cursorY -= PaintUtil.getTextBounds(tzS, PaintUtil.LEGENDFONT, g ).getHeight()*2;
		
		// TEXTOS
		Path2D.Double textos = new Path2D.Double();
		textos.append(latLonShape, false);
		textos.append(azimutShape, false);
		textos.append(tzShape, false);
		AffineTransform bestFit = PaintUtil.computeBestFit(textos.getBounds2D(), textRectangle.getBounds2D());
		Shape textosShape = bestFit.createTransformedShape(textos);

		// ESQUINA DE ABAJO, IZQUIERDA O DERECHA
		AffineTransform af = new AffineTransform();
		if( data.getWallNormal().getAzimut() > 0 ){
			af.translate( dial.x + dial.width - rectangle.width, dial.y + dial.height - rectangle.height );
		}
		else{
			af.translate( dial.x, dial.y + dial.height - rectangle.height );
		}
		
		List<Shape> ret = new ArrayList<Shape>();
		ret.add( af.createTransformedShape(innerRectangle));
		ret.add( af.createTransformedShape(textosShape) );
		
		
		return ret;
	}


	private Shape createHomenaje(Graphics2D g, SunDialPanel panel) {
		SunDialData data = getData();
		TimeZone tz = data.getTimeZone();
		ListDateIterable it = new ListDateIterable();
		it.addDate( DateUtil.createDate(tz, SunPosition.REFERENCEYEAR, Calendar.JULY, 21, 12, 00 ) );
		List<Shape> lines = createDayLinesInsidePrintArea(g, panel, it, true);
		Shape line = lines.iterator().next();
		
		String s = "  Nace el sol dos veces  ";
		return PaintUtil.getShapeFromRepeatingText( s, line, PaintUtil.HOMENAJEFONT );
	}


	private void setTimeZone(TimeZone timeZone) {
		DATEFORMAT.setTimeZone(timeZone);
		TIMEFORMAT.setTimeZone(timeZone);
		MONTHFORMAT.setTimeZone(timeZone);
		DATETIMEFORMAT.setTimeZone(timeZone);
	}


	private List<Shape> createDayLinesBetweenVerticalAndAnalemmaOfVerticalShadowInCross(Graphics2D g, SunDialPanel panel) {
		List<Shape> ret = new ArrayList<Shape>();
		SunDialData data = getData();
		TimeZone tz = data.getTimeZone();
		IDateIterable it = getRepresentedDatesIterable(true,false);
		for (Date date : it) {
			DateInterval di = getIntervalBetweenVerticalLineAndAnalemma(tz,	date);
			
			Date iniDate = di.getIni();
			Date endDate = di.getEnd();
			IDateIterable i = new DateIterable(tz, iniDate, endDate, Calendar.SECOND, 60);
			i = ListDateIterable.merge(i, iniDate);
			i = ListDateIterable.merge(i, endDate);
			Shape line = createShapeForDateIterator(g, panel, i);
			
			// VAMOS A PONER LETRAS
			String texto = MONTHFORMAT.format( iniDate );
			line = PaintUtil.getInvertedShapeFromRepeatingText( texto, line, PaintUtil.STROKEFORRULERFONT, PaintUtil.RULERFONT );
			
			ret.add( line );

		}
		return ret;
	}


	private DateInterval getIntervalBetweenVerticalLineAndAnalemma(TimeZone tz, Date date) {
		Date vertical = computeTimeOfVerticalShadow(date);
		Date verticalShadowInCross = computeTimeOfVerticalShadowInCross()[0];
		if( verticalShadowInCross == null ){
			Date ini = DateUtil.firstSecondOfDate(tz, date);
			Date end = DateUtil.lastSecondOfDate(tz, date);
			return new DateInterval(ini,end);
		}
		
		Date analemma = DateUtil.computeDateWithAnotherHourAndMinute(tz, vertical, verticalShadowInCross);
		DateInterval di = new DateInterval(analemma, vertical);

		
		
		log( "date:" + date );
		log( "  verticalShadowInCross:" + verticalShadowInCross );
		log( "  vertical:" + vertical );
		log( "  analemma:" + analemma );
		return di;
	}
	
	private void log(String string) {
	}


	private List<Shape> createDayLinesInsidePrintArea(Graphics2D g, SunDialPanel panel, IDateIterable it, boolean includeBetweenAnalemmaAndVertical) {
		List<Shape> ret = new ArrayList<Shape>();
		SunDialData data = getData();
		TimeZone tz = data.getTimeZone();
		LatLon observer = data.getLatLon();
		
		Area printArea = new Area( getPrintArea(g, panel) );
		
		for (Date date : it) {
			Date iniDate = DateUtil.firstSecondOfDate(tz,date);
			Date endDate = DateUtil.lastSecondOfDate(tz,date);
			IDateIterable i = new DateIterable(tz, iniDate, endDate, Calendar.SECOND, 60);
			
			Shape line = createShapeForDateIterator(g, panel, i);
			
			line = PaintUtil.intersectOpenShapeWithClosedShape(line,printArea);
			
			if( !includeBetweenAnalemmaAndVertical && line != null ){
				DateInterval di = getIntervalBetweenVerticalLineAndAnalemma( tz, date );
				AzimutAltitude aa1 = SunPosition.getAzimutAltitude(observer,di.getIni());
				AzimutAltitude aa2 = SunPosition.getAzimutAltitude(observer,di.getEnd());
				Point2d p1 = project(aa1);
				Point2d p2 = project(aa2);
				line = PaintUtil.removeXInterval( line, p1.x, p2.x );
			}
			
			if( line != null ){
				ret.add( line );
			}
		}
		return ret;
	}
	



	private Shape createAnalemmaOfVerticalShadowInCross(Graphics2D g, SunDialPanel panel) {
		Date date = computeTimeOfVerticalShadowInCross()[0];
		if( date == null ){
			return null;
		}
		Shape analemma = createAnalemma(g, panel, date);
		
		String dateS = TIMEFORMAT.format(date) + "        ";
		
		analemma = PaintUtil.getInvertedShapeFromRepeatingText( dateS, analemma, PaintUtil.STROKEFORRULERFONT, PaintUtil.VERYSMALLFONT );
		
		return analemma;
	}


	private Date[] _timeOfVerticalShadowInCross = null;
	
	private Date[] computeTimeOfVerticalShadowInCross( ){
		
		if( _timeOfVerticalShadowInCross != null ){
			return _timeOfVerticalShadowInCross;
		}
		
		SunDialData data = getData();
		LatLon observer = data.getLatLon();
		TimeZone tz = data.getTimeZone();
		
		Date dates[] = AzimutAltitudeFinder.computeSameAltitudeInYear(tz, observer, SunPosition.REFERENCEYEAR);
		
		_timeOfVerticalShadowInCross = new Date[]{
				computeTimeOfVerticalShadow(dates[0]),
				computeTimeOfVerticalShadow(dates[1]),
		};
		return _timeOfVerticalShadowInCross;
	}
		
	private Date computeTimeOfVerticalShadow( Date date ){
		SunDialData data = getData();
		Date dateVertical = AzimutAltitudeFinder.findAzimut(data, date, data.getWallNormal().getAzimut() );
		
		if( dateVertical == null ){
			// NUNCA CAE EL SOL EN LA VERTICAL EN ESTA FECHA
			return null;
		}
		
		return dateVertical;
	}
	
	
	
	
	
	protected Shape createShapeForDateIterator( Graphics2D g, SunDialPanel panel, IDateIterable it ){
		Path2D.Double ret = new Path2D.Double();
		SunDialData data = getData();
		LatLon observer = data.getLatLon();

		AzimutAltitude azal1 = null;
		AzimutAltitude azal2 = null;
		
		boolean first = true;
		for( Date time : it ){
			azal1 = SunPosition.getAzimutAltitude(observer, time);

			// SI LOS DOS PUNTOS EXISTEN
			if( azal1 != null && azal2 != null ){
		
				if( azal1.getAltitude() > 0 && azal2.getAltitude() > 0 ){

					Point2d p1 = project(azal1);
					Point2d p2 = project(azal2);
					
					if( first && p1 != null ){
						first = false;
						ret.moveTo( p1.x, p1.y );
					}
					
					if( p2 == null && p1 != null ){
						ret.moveTo(p1.x, p1.y);
					}
					
					if( p2 != null && p1 != null ){
						ret.lineTo(p1.x, p1.y);
					}
				}
			}
			// SI SOLO EXISTE azal1
			else if( azal1 != null ){
				if( azal1.getAltitude() > 0  ){

					Point2d p1 = project(azal1);
					
					if( first && p1 != null ){
						first = false;
						ret.moveTo( p1.x, p1.y );
					}
				}
			}
		
			// SIGUIENTE PASO
			azal2 = azal1;
		}
		return ret;
	}
	
	private DateIterable getAnalemmaDateIterator( Date dateTime, int unit, int step ){
		SunDialData data = getData();
		TimeZone tz = data.getTimeZone();
		Calendar c = Calendar.getInstance(tz);
		c.setTime(dateTime);
		c.add( Calendar.YEAR, 1);
		c.add( Calendar.DAY_OF_YEAR, 1 );
		Date endDate = c.getTime();
		DateIterable it = new DateIterable( data.getTimeZone(), dateTime, endDate, unit, step );
		return it;
	}
	
	private Shape createAnalemma(Graphics2D g, SunDialPanel panel, Date dateTime) {
		DateIterable it = getAnalemmaDateIterator(dateTime, Calendar.DAY_OF_YEAR, 1); 
		return createShapeForDateIterator(g, panel, it);
	}
	


	private List<Shape> createVerticalDatesAndTimes( Graphics2D g, SunDialPanel panel ){
		IDateIterable it = getRepresentedDatesIterable(true, true);
		
		List<Shape> ret = new ArrayList<Shape>();
		ret.addAll( createVerticalTimes( g, panel, it ) );
		ret.addAll( createVerticalDates( g, panel, it ) );
		return ret;
	}

	private IDateIterable getRepresentedDatesIterable( boolean middleOfMonth, boolean startOfMonth ) {
		
		
		SunDialData data = getData();
		TimeZone tz = data.getTimeZone();
		Date iniDate = DateUtil.createDate(tz, SunPosition.REFERENCEYEAR, 0, 1);
		Date endDate = DateUtil.createDate(tz, SunPosition.REFERENCEYEAR+1, 0, 1);
		//endDate = new Date(endDate.getTime()-24*60*60*1000);
		DateIterable it = new DateIterable(tz, iniDate, endDate, Calendar.MONTH, 1);
		
		ListDateIterable ret = new ListDateIterable();
		Calendar c = Calendar.getInstance(tz);
		for (Date date : it) {
			if( middleOfMonth ){
				c.setTime(date);
				c.add(Calendar.DAY_OF_MONTH, 14);
				ret.addDate(c.getTime());
			}
			
			if( startOfMonth ){
				ret.addDate(date);
			}
		}

		return ret;
	}
		

	private List<Shape> createVerticalDates(Graphics2D g, SunDialPanel panel, IDateIterable it) {
		List<Shape> ret = new ArrayList<Shape>();
		SunDialData data = getData();
		TimeZone tz = data.getTimeZone();
		LatLon observer = data.getLatLon();
		Point2d base = data.getGnomonBase();
		Date[] cross = computeTimeOfVerticalShadowInCross();
		if( cross == null || cross[0] == null ){
			return ret;
		}

		for (Date date : it) {
			Date dateTime = DateUtil.computeDateWithAnotherHourAndMinute(tz,date, cross[0]);

			AzimutAltitude azal = SunPosition.getAzimutAltitude(observer, dateTime );
			Date dates[] = AzimutAltitudeFinder.findAzimutAltitude(data, SunPosition.REFERENCEYEAR, azal);
			boolean belowCross = dateTime.after(cross[0]) && dateTime.before(cross[1]);

			if( Math.abs( dates[0].getTime() - dateTime.getTime() ) < Math.abs( dates[1].getTime() - dateTime.getTime() ) ){
				dates[1] = dates[1];  
				dates[0] = dateTime;
			}
			else{
				dates[1] = dates[0];
				dates[0] = dateTime;
			}
			

			Point2d p = project(azal);
			if( p != null  ){

				Date[] datesWithSameY = findDatesWithSameY(dateTime, p.y);
				double x = base.x;
				for (Date dateWithSameY : datesWithSameY) {
					AzimutAltitude aa = SunPosition.getAzimutAltitude(observer, dateWithSameY);
					Point2d paa = project(aa);
					if( paa.x < x && !belowCross ){
						x = paa.x;
					}
					if( paa.x > x && belowCross ){
						x = paa.x;
					}
				}

				x += belowCross?+15:-15;
				
				String str = DATEFORMAT.format(dates[0]);
				ret.add( PaintUtil.getShapeFromText(str, x, p.y, PaintUtil.FONT, belowCross));
				Rectangle2D bounds = PaintUtil.getTextBounds(str, PaintUtil.FONT, g);
				str = DATEFORMAT.format(dates[1]);
				x += belowCross? bounds.getWidth()+15 : -bounds.getWidth() - 15;
				ret.add( PaintUtil.getShapeFromText(str, x, p.y, PaintUtil.SMALLFONT, belowCross));
			}
			
		}
		
		return ret;
	}
	
	
	private List<Shape> createVerticalTimes( Graphics2D g, SunDialPanel panel, IDateIterable it ){
		List<Shape> ret = new ArrayList<Shape>();
		SunDialData data = getData();
		LatLon observer = data.getLatLon();
		Point2d base = data.getGnomonBase();
		Date[] cross = computeTimeOfVerticalShadowInCross();
		if( cross == null || cross[0] == null ){
			return ret;
		}
		for (Date date : it) {
			
			Date dateTime = computeTimeOfVerticalShadow(date);
			AzimutAltitude azal = SunPosition.getAzimutAltitude(observer, dateTime);
			boolean belowCross = dateTime.after(cross[0]) && dateTime.before(cross[1]);
			
			Date dates[] = AzimutAltitudeFinder.findAzimutAltitude(data, SunPosition.REFERENCEYEAR, azal);
			if( Math.abs( dates[0].getTime() - dateTime.getTime() ) < Math.abs( dates[1].getTime() - dateTime.getTime() ) ){
				dates[1] = dates[1];
				dates[0] = dateTime;
			}
			else{
				dates[1] = dates[0];  
				dates[0] = dateTime;
			}

			Point2d p0 = project( SunPosition.getAzimutAltitude(observer, dates[0] ) );
			Point2d p1 = project( SunPosition.getAzimutAltitude(observer, dates[1] ) );
			if( p1.x < p0.x ){
				dates = new Date[]{ dates[1], dates[0] };
			}
			
			Point2d p = project(azal);
			if( p != null  ){
				
				
				double x = base.x + (belowCross?-15:+15);
				
				String str = TIMEFORMAT.format(dates[0]);
				ret.add( PaintUtil.getShapeFromText(str, x, p.y, PaintUtil.FONT, !belowCross));
				Rectangle2D bounds = PaintUtil.getTextBounds(str, PaintUtil.FONT, g);
				str = TIMEFORMAT.format(dates[1]);
				x += belowCross? -bounds.getWidth()-15 : bounds.getWidth() + 15;
				ret.add( PaintUtil.getShapeFromText(str, x, p.y, PaintUtil.SMALLFONT, !belowCross));
			
			}
		}
		
		return ret;
	}
	
	private Date[] findDatesWithSameY( Date analemmaDate, final double y ){
		SunDialData data = getData();
		LatLon observer = data.getLatLon();
		IDateIterable it = getAnalemmaDateIterator(analemmaDate, Calendar.DAY_OF_YEAR, 1);
		ArrayList<Date> ret = new ArrayList<Date>();
		for (Date date : it) {
			AzimutAltitude azal = SunPosition.getAzimutAltitude(observer, date);
			Point2d p = project(azal);
			if( GeomUtil.areEqual(y, p.y, 5) ){
				ret.add(date);
			}
		}
		return ret.toArray(new Date[0]);
	}

	@Override
	public void refresh() {
		super.refresh();
		_timeOfVerticalShadowInCross = null;
	}
	
	private List<Shape> createVerticalDatesOnDayLine(Graphics2D g, SunDialPanel panel, Shape longDayLine ){
		SunDialData data = getData();
		List<Shape> ret = new ArrayList<Shape>();

		Point2d firstPoint = PaintUtil.getMiddlePoint( longDayLine );
		AzimutAltitude azal = GeomUtil.computeAzimutAltitude(data, firstPoint );
		Date dates[] = AzimutAltitudeFinder.findAzimutAltitude(data, SunPosition.REFERENCEYEAR, azal);
		dates[0] = computeTimeOfVerticalShadow(dates[0]);
		dates[1] = computeTimeOfVerticalShadow(dates[dates.length-1]);
		
		
		String str = DATETIMEFORMAT.format(dates[0]);
		ret.add( PaintUtil.getShapeFromText("  " + str, longDayLine, PaintUtil.FONT ) );
		str = DATETIMEFORMAT.format(dates[1]);
		ret.add( PaintUtil.getShapeFromRightText( str + "  ", longDayLine, PaintUtil.FONT ) );
		
		return ret;
	}
	
	private List<Shape> createVerticalDatesOnDayLines(Graphics2D g, SunDialPanel panel, List<Shape> longDayLines) {
		List<Shape> ret = new ArrayList<Shape>();
		
		
		for (Shape longDayLine : longDayLines ) {
			
			ret.addAll( createVerticalDatesOnDayLine(g, panel, longDayLine) ); 
		}
		
		return ret;
	}
	
}
