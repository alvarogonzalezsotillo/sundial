package gui;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Set;
import java.util.TreeSet;

import computation.Point2d;

public class PaintUtil {
	
	private static final int MAX = 100000;

	public static final BasicStroke MEDIUMSTROKE = new BasicStroke(5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );
	public static final DummyStroke DUMMYSTROKE = new DummyStroke();
	public static final BasicStroke BIGSTROKE = new BasicStroke(7.5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
	public static final BasicStroke THINSTROKE = new BasicStroke(3f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );
	public static final BasicStroke VERYTHINSTROKE = new BasicStroke(.5f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );
	
	public static final BasicStroke STROKEFORRULERFONT = new BasicStroke(6f, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );
	
	public static final Font FONT = Font.decode("Verdana-10");
	public static final Font SMALLFONT = Font.decode("Verdana-8");
	public static final Font RULERFONT = Font.decode("Arial-7");
	public static final Font VERYSMALLFONT = Font.decode("Arial-4");
	public static final Font HOMENAJEFONT = Font.decode("Arial-2");
	public static final Font LEGENDFONT = Font.decode("Verdana-12");
	
	
	
	public static AffineTransform computeBestFit( Rectangle2D src, Rectangle2D dst ){
		
		// VEMOS SI HAY QUE AJUSTAR HORIZONTAL O VERTICAL
		double zoomHorizontal = dst.getWidth()/src.getWidth();
		double zoomVertical = dst.getHeight()/src.getHeight();
		boolean vertical = zoomHorizontal > zoomVertical;
		double zoomFactor;
		if( vertical ){
			zoomFactor = zoomVertical;
		}
		else{
			zoomFactor = zoomHorizontal;
		}
		AffineTransform ret = AffineTransform.getScaleInstance(zoomFactor, zoomFactor);

		double x = (src.getCenterX() * zoomFactor)-dst.getCenterX();
		double tx = -x;
		double y = (src.getCenterY() * zoomFactor)-dst.getCenterY();
		double ty = -y;
		ret.preConcatenate( AffineTransform.getTranslateInstance(tx, ty));
	
		if( true ){
			System.out.println( "src: " + src );
			System.out.println( "dst:" + dst );
			System.out.println( "zoomHorizontal:" + zoomHorizontal );
			System.out.println( "zoomVertical:" + zoomVertical );
			System.out.println( "vertical:" + vertical );
			System.out.println( "x:" + x );
			System.out.println( "y:" + y );
			System.out.println( "tx:" + tx );
			System.out.println( "ty:" + ty );
			System.out.println( "src->dst:" + transform(src,ret) );
		}
		
		return ret;
	}

	
	private static Rectangle2D transform(Rectangle2D src, AffineTransform ret) {
		// SOLO SI ES UN ZOOM CON TRASLACION, SIN ROTACION
		Point2D ul = new Point2D.Double(src.getX(), src.getY() );
		Point2D lr = new Point2D.Double(ul.getX() + src.getWidth(), ul.getY() + src.getHeight() );
		Point2D tul = new Point2D.Double();
		ret.transform(ul, tul);
		Point2D tlr = new Point2D.Double();
		ret.transform(lr, tlr);
		
		return new Rectangle2D.Double( tul.getX(), tul.getY(),
				tlr.getX() - tul.getX(),
				tlr.getY() - tul.getY() );
	}


	public static void drawLine( Graphics2D g, double x1, double y1, double x2, double y2 ){
		if( x1 > MAX || y1 > MAX || x2 > MAX || y2 > MAX ){
			return;
		}
		if( x1 < -MAX || y1 < -MAX || x2 < -MAX || y2 < -MAX ){
			return;
		}
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}

	public static void drawMark(Graphics2D g, double x, double y) {
		Stroke stroke = g.getStroke();
		g.setStroke( getPixelStroke(g, 2));
		g.drawLine((int)x-2, (int)y, (int)x+2, (int)y);
		g.drawLine((int)x, (int)y+2, (int)x, (int)y-2);
		g.setStroke(stroke);
	}
	

	public static Rectangle2D getTextBounds( String text, Font font, Graphics2D g ){
		TextLayout tl = new TextLayout(text,font,g.getFontRenderContext());
		Shape shape = tl.getOutline(null);
		Rectangle2D bounds = shape.getBounds2D();
		return bounds;
	}

	public static Shape getShapeFromRepeatingText( String text, Shape line, Font font ){
		TextStroke ts = new TextStroke( text, font, false, true, true );
		return ts.createStrokedShape(line);
	}
	
	public static Shape getShapeFromText( String text, Shape line, Font font ){
		TextStroke ts = new TextStroke( text, font, false, false, true );
		return ts.createStrokedShape(line);
	}

	public static Shape getShapeFromRightText( String text, Shape line, Font font ){
		TextStroke ts = new TextStroke( text, font, false, false, false );
		return ts.createStrokedShape(line);
	}

	public static Shape getInvertedShapeFromRepeatingText( String text, Shape line, Stroke stroke, Font font ){
		TextStroke ts = new TextStroke( text, font, false, true, true );
		ts.setMiddle(true);
		Shape textShape = ts.createStrokedShape(line);
		
		line = stroke.createStrokedShape(line);
		Area textArea = new Area(textShape);
		Area lineArea = new Area(line);
		
		lineArea.exclusiveOr(textArea);
		
		return  lineArea;
	}
	
	
	public static Shape getShapeFromText(String text, double x, double y, Font font, boolean right){
		TextLayout tl = new TextLayout(text,font,new FontRenderContext(null, true, true) );
		Shape shape = tl.getOutline(null);
		Rectangle2D bounds = shape.getBounds2D();
		y += bounds.getHeight()/2;
		AffineTransform t;
		if( right ){
			t = AffineTransform.getTranslateInstance(x, y);
		}
		else{
			t = AffineTransform.getTranslateInstance(x-bounds.getWidth(), y);
		}
		shape = tl.getOutline(t);
		return shape;
	}
	
	public static void drawString(String s, double x, double y, Font font, Graphics2D g, boolean right) {
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		Rectangle2D bounds = metrics.getStringBounds(s, g);
		y += bounds.getHeight()/2;
		if( !right ){
			x -= bounds.getWidth();
		}
		g.drawString(s, (int)x, (int)y);
	}
	
	public static Stroke getPixelStroke( Graphics2D g, int pixels ){
		Point p1 = new Point(0,0);
		Point p2 = new Point(1,1);
		AffineTransform t = g.getTransform();
		if( t == null ){
			return g.getStroke();
		}
		Point pt1 = new Point();
		Point pt2 = new Point();
		t.transform(p1, pt1);
		t.transform(p2, pt2);
		return new BasicStroke( (float) Math.abs(1.0*pixels/(pt2.x - pt1.x)) );
	}


	public static Rectangle2D getImageableRectangle(PageFormat pageFormat, double margin) {
		Rectangle2D ret = new Rectangle2D.Double( 
				pageFormat.getImageableX()+margin,
				pageFormat.getImageableY()+margin,
				pageFormat.getImageableWidth()-margin*2,
				pageFormat.getImageableHeight()-margin*2 );
		return ret;
	}


	public static Shape intersectOpenShapeWithClosedShape( Shape openShape, Shape closedShape ){
		// SUPONGO QUE ES UNA FUNCION EN X: PARA UNA X DEL DOMINIO SOLO HAY UN VALOR DE Y
		// TAMPOCO TIENE GIROS MUY CERRADOS
		// ME BASO EN QUE PUEDO ELEGIR UN PUNTO QUE CAIGA A LA IZQUIERDA Y LLEGO SALTANDO
		// A LOS MAS CERCANOS HASTA EL OTRO PUNTO
		Area closedArea = closedShape instanceof Area ? (Area)closedShape : new Area(closedShape);
		Area openArea = new Area(openShape);
		
		Shape openShapeDelta = THINSTROKE.createStrokedShape(openShape);
		
		openArea.intersect(closedArea);
		
		// ME QUEDO CON LOS PUNTOS DE openArea QUE ESTABAN EN openShape
		Point2D.Double left = null;
		
		Set<Point2D.Double> set = new TreeSet<Point2D.Double>( new Comparator<Point2D.Double>(){
			@Override
			public int compare(Point2D.Double p1, Point2D.Double p2) {
				double d = p1.x - p2.x;
				if( d < 0 ){
					return -1;
				}
				else if( d > 0 ){
					return 1;
				}
				return 0;
			}
			
		});
		
		PathIterator it = openArea.getPathIterator(null);
		float p[] = new float[6];
		while( !it.isDone() ){
			int type = it.currentSegment( p );
			Point2D.Double toAdd = null;
			switch( type ){
				case PathIterator.SEG_MOVETO:
					if( openShapeDelta.contains(p[0], p[1]) ){
						toAdd = new Point2D.Double(p[0], p[1]);
					}
					break;
	
				case PathIterator.SEG_LINETO:
					if( openShapeDelta.contains(p[0], p[1]) ){
						toAdd = new Point2D.Double(p[0], p[1]);
					}
					break;
			}
			if( toAdd != null ){
				set.add(toAdd);
				if( left == null || left.x > toAdd.x ){
					left = toAdd;
				}
			}
			it.next();
		}
		
		// COMENZAMOS DESDE LA IZQUIERDA HASTA EL FINAL
		Path2D.Double ret = null;
		for( Point2D.Double p2d : set ){
			if( ret == null ){
				ret = new Path2D.Double();
				ret.moveTo(p2d.x, p2d.y);
			}
			else{
				ret.lineTo(p2d.x, p2d.y );
			}
		}
		return ret;
	}


	public static Shape removeXInterval(Shape line, double x1, double x2) {
		Path2D.Double ret = new Path2D.Double();
		double min = x1 < x2 ? x1 : x2;
		double max = x1 > x2 ? x1 : x2;

		PathIterator it = line.getPathIterator(null);
		float p[] = new float[6];
		boolean in = false;
		while( !it.isDone() ){
			int type = it.currentSegment( p );
			boolean newIn = p[0] < min ||  p[0] > max;
			switch( type ){
				case PathIterator.SEG_MOVETO:
					if( !in && newIn ){
						ret.moveTo(p[0], p[1]);
					}
					
					break;
	
				case PathIterator.SEG_LINETO:
					if( !in && newIn ){
						ret.moveTo(p[0], p[1] );
					}
					if( in && newIn ){
						ret.lineTo(p[0], p[1]);
					}
					break;
			}
			in = newIn;
			it.next();
		}
		
		return ret;
	}


	public static Point2d getFirstPoint(Shape longDayLine) {
		float points[] = new float[6];
		PathIterator it = longDayLine.getPathIterator(null);
		it.currentSegment(points);
		return new Point2d(points[0], points[1] );
	}

	public static Point2d getMiddlePoint(Shape longDayLine) {
		float points[] = new float[6];
		float flatness = 10;
		float length = measurePathLength(longDayLine, flatness);
		PathIterator it = new FlatteningPathIterator( longDayLine.getPathIterator(null), flatness );
		float lastXY[] = null;
		float currLength = 0;
		
		while( currLength < length/2 ){
			int type = it.currentSegment(points);
			if( type == PathIterator.SEG_LINETO && lastXY != null ){
				float dx = lastXY[0] - points[0];
				float dy = lastXY[1] - points[1];
				float d = (float) Math.sqrt(dx*dx + dy*dy);
				currLength += d;
			}
			lastXY = new float[]{ points[0], points[1] };
			it.next();
		}
		
		return new Point2d( lastXY[0], lastXY[1] );
	}

	public static String sprintf( String format, Object ... args ){
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format(format, args);
		return sb.toString();
	}
	
	public static float measurePathLength( Shape shape, double flatness ) {
		PathIterator it = new FlatteningPathIterator( shape.getPathIterator( null ), flatness );
		float points[] = new float[6];
		float moveX = 0, moveY = 0;
		float lastX = 0, lastY = 0;
		float thisX = 0, thisY = 0;
		int type = 0;
        float total = 0;

		while ( !it.isDone() ) {
			type = it.currentSegment( points );
			switch( type ){
			case PathIterator.SEG_MOVETO:
				moveX = lastX = points[0];
				moveY = lastY = points[1];
				break;

			case PathIterator.SEG_CLOSE:
				points[0] = moveX;
				points[1] = moveY;
				// Fall into....

			case PathIterator.SEG_LINETO:
				thisX = points[0];
				thisY = points[1];
				float dx = thisX-lastX;
				float dy = thisY-lastY;
				total += (float)Math.sqrt( dx*dx + dy*dy );
				lastX = thisX;
				lastY = thisY;
				break;
			}
			it.next();
		}

		return total;
	}


	public static Shape intersectClosedShapeWithClosedShape(Shape s, Shape bounds ){
		Area a1 = new Area(s);
		Area a2 = new Area(bounds);
		
		a1.intersect(a2);
		
		return a1;
	}


	public static Shape createMark(Point2d p, double size){
		double s = size/2;
		double y = p.y - s;
		double x = p.x - s;
		double h = size;
		double w = size;
		return new Ellipse2D.Double( x, y, w, h );
	}

}
