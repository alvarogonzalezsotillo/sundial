package gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Path2D.Double;

import javax.swing.JFrame;
import javax.swing.Timer;

import computation.Point2d;

public class StepPathIterator implements PathIterator{
	
	private FlatteningPathIterator _it;
	private double _step;
	private double _x1, _y1, _x2, _y2, _length, _remainingLength;
	private double _currentSegment[];
	private int _currentType;
	private boolean _isDone;
	private double _xIni;
	private double _yIni;
	private boolean _leftAligned;
	
	public StepPathIterator( Shape s, boolean leftAligned ){
		this( s, null, .1, leftAligned );
	}

	public StepPathIterator( Shape s, double step, boolean leftAligned ){
		this( s, null, step, leftAligned );
	}

	public StepPathIterator( Shape s, AffineTransform at, double step, boolean leftAligned ){
		_step = step;
		_leftAligned = leftAligned;
		PathIterator pathIterator = s.getPathIterator(at);
		if( !_leftAligned ){
			pathIterator = ReversePathIterator.reverse(pathIterator);
		}
		_it = new FlatteningPathIterator(pathIterator, _step);
		initializeFirstSegment();
	}
	
	private void initializeFirstSegment(){
		if( _leftAligned ){
			int ret= nextSegmentImpl();
			if( ret != PathIterator.SEG_MOVETO && ret != PathIterator.SEG_LINETO ){
				throw new IllegalStateException(" " + ret);
			}
			ret= nextSegmentImpl();
			if( ret != PathIterator.SEG_LINETO ){
				throw new IllegalStateException(" " + ret);
			}
			_xIni = _x1;
			_yIni = _y1;
		}
		else{
			nextSegmentImpl();
			nextSegmentImpl();
			nextSegmentImpl();
			_xIni = _x1;
			_yIni = _y1;
			
		}
	}
	
	private int nextSegment(){
		int ret = nextSegmentImpl();
		while( ret == PathIterator.SEG_MOVETO ){
			_xIni = _x2;
			_yIni = _y2;
			ret = nextSegmentImpl();
		}
		if( ret != PathIterator.SEG_LINETO && ret != PathIterator.SEG_CLOSE && ret != -1 ){
			throw new IllegalStateException( " " + ret );
		}
		return ret;
	}
	
	private int nextSegmentImpl(){
		if( _it.isDone() ){
			return -1;
		}
		_x1 = _x2;
		_y1 = _y2;
		double c[] = new double[6];
		int ret = _it.currentSegment(c);
		_it.next();
		if( ret != PathIterator.SEG_LINETO && ret != PathIterator.SEG_MOVETO && ret != PathIterator.SEG_CLOSE ){
			throw new IllegalStateException( "" + ret );
		}
		_x2 = c[0];
		_y2 = c[1];
		System.err.println( "*************************" + ret + " " + _x2 + "," + _y2 );

		if( ret == PathIterator.SEG_CLOSE ){
			_x2 = _xIni;
			_y2 = _yIni;
		}
		_length = _remainingLength = distance(_x1, _y1, _x2, _y2 );
		_currentType = PathIterator.SEG_LINETO;
		return ret;
	}

	private double distance(double x1, double y1, double x2, double y2) {
		double d = (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
		d = Math.sqrt(d);
		return d;
	}

	@Override
	public int getWindingRule() {
		return _it.getWindingRule();
	}

	@Override
	public int currentSegment(float[] coords) {
		double ret[] = new double[6];
		int iret = currentSegment(ret);
		for( int i = 0 ; i < ret.length ; i++ ){
			coords[i] = (float) ret[i];
		}
		return iret;
	}

	@Override
	public boolean isDone() {
		return _isDone;
	}

	@Override
	public void next() {
		next(_step);
	}
	
	public Point2d nextPoint(double step){
		next(step);
		return new Point2d( _currentSegment[0], _currentSegment[1] );
	}

	
	public void next( double step ){
		log( "next:");
		
		if( isDone() ){
			throw new IllegalStateException();
		}
		
		if( _currentSegment == null ){
			// ES LA PRIMERA LLAMADA
			_currentSegment = new double[6];
			_currentType = PathIterator.SEG_MOVETO;
			_currentSegment[0] = _x1;
			_currentSegment[1] = _y1;
			log( "  Primera llamada:" + _currentSegment[0] + "," + _currentSegment[1] );
			return;
		}
		
		// CALCULO HASTA DONDE HAY QUE AVANZAR, DESDE DONDE ESTOY
		double currLength = _length - _remainingLength;
		double advanceTo = currLength + step;
		
		log( "  step:" + step );
		log( "  _x1, _y1:" + _x1 + "," + _y1 );
		log( "  _x2, _y2:" + _x2 + "," + _y2 );
		log( "  _length:" + _length );
		log( "  _remainingLength:" + _remainingLength );
		log( "  advanceTo:" + advanceTo );

		
		// TENGO QUE IR AVANZANDO LOS SEGMENTOS HASTA QUE EL AVANCE ESTE DENTRO DEL SEGMENTO
		while( advanceTo > _length ){
			log( "    otro segmento");
			advanceTo -= _length;
			log( "    advanceTo:" + advanceTo );
			if( nextSegment() == -1 ){
				// ES AQUI CUANDO DETECTO QUE YA NO HAY MAS. DOY EL ULTIMO PUNTO DISPONIBLE,
				// NO PUEDO SABERLO ANTES PORQUE NO SE SABE CUAL SERÁ EL SIGUIENTE STEP
				_isDone = true;
				_currentSegment[0] = _x2;
				_currentSegment[1] = _y2;
				
				log( "    ya no quedan segmentos:" + _currentSegment[0] + "," + _currentSegment[1] );
				return;
			}
		}
		
		// EL AVANCE ESTA DENTRO DEL SEGMENTO EN _x1, _y1, _x2, _y2
		Point2d p = interpolation( _x1, _y1, _x2, _y2, advanceTo );
		_remainingLength = _length - advanceTo;
		log( "  advanceTo final:" + advanceTo );
		log( "  Interpolando:" + p );
		_currentSegment[0] = p.x;
		_currentSegment[1] = p.y;
		return;
	}

	private void log(String string) {
		//System.out.println( string );
	}

	private Point2d interpolation(double x1, double y1, double x2, double y2, double d) {
		double vx = x2 - x1;
		double vy = y2 - y1;
		double distance = distance(x1, y1, x2, y2);
		vx /= distance;
		vy /= distance;
		
		vx *= d;
		vy *= d;
		
		log( "      x1,y1 x2,y2:" + x1 +"," + y1 +"  " +x2 + "," + y2);
		log( "      distance:" + distance );
		log( "      d:" + d );
		log( "      vx,vy:" + vx + "," + vy );
		return new Point2d( x1 + vx, y1 + vy );
	}

	@Override
	public int currentSegment(double[] coords) {
		for( int i = 0 ; i < coords.length ; i++){
			coords[i] = _currentSegment[i];
		}
		return _currentType;
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame("StepPathIterator"){
			{
				new Timer(1000, new ActionListener(){
	
	
					@Override
					public void actionPerformed(ActionEvent e) {
						repaint();
					}
					
				}){
					{
						//start();
					}
				};
			}
			
			public void paint(Graphics g) {
				super.paint(g);
				//Shape s = new Ellipse2D.Double(200, 200, 200, 175 );
				//Shape s = new Line2D.Double( 10, 10, 100 , 100 );
				//Shape s = new Polygon( new int[]{ 100, 200, 300, 400, 500 }, new int[]{ 100, 100, 200, 500, 600 }, 5 );
				Shape s = PaintUtil.getShapeFromText("hola", 100, 100, Font.decode("Verdana-100"), true);
				Graphics2D g2d = (Graphics2D) g;
				g2d.draw(s);
				
				StepPathIterator spi = new StepPathIterator(s, true);
				while( !spi.isDone() ){
					Point2d p = spi.nextPoint(5);
					g2d.fillArc((int)p.x, (int)p.y, 3, 3, 0, 360);
				}
				System.err.print( "." );
			};
		};
		
		f.setSize( 300, 300 );
		f.setVisible(true);
	}
}
