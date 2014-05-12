package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import computation.Point2d;
import computation.SunDialData;
import computation.geom.AzimutAltitude;
import computation.geom.GeomUtil;


public class SunDialPainter{

	public class SunDialPageable implements Pageable {

		private SunDialPanel _panel;
		private PageFormat _pageFormat;

		public SunDialPageable(SunDialPanel panel, PageFormat pageFormat) {
			_panel = panel;
			_pageFormat = pageFormat;
		}

		@Override
		public int getNumberOfPages() {
			return 1;
		}

		@Override
		public PageFormat getPageFormat(int arg0) throws IndexOutOfBoundsException {
			return _pageFormat;
		}

		@Override
		public Printable getPrintable(int arg0)
				throws IndexOutOfBoundsException {
			return new SunDialPrintable(_panel);
		}

	}

	public class SunDialPrintable implements Printable {

		private SunDialPanel _panel;

		public SunDialPrintable(SunDialPanel panel) {
			_panel = panel;
		}

		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
			if( pageIndex != 0 ){
				return Printable.NO_SUCH_PAGE;
			}
			
			boolean oldVisible = isRulerVisible();
			setRulerVisible(false);
			try{
				double margin = 30;
				Rectangle2D imageable = PaintUtil.getImageableRectangle( pageFormat, margin );
				Rectangle2D bbox = getShapes().getBounds();
				AffineTransform af = PaintUtil.computeBestFit(bbox, imageable);
				Graphics2D g2 = (Graphics2D)graphics;
				g2.transform(af);
				paint(g2, _panel );
			}
			finally{
				setRulerVisible(oldVisible);
			}
			
			return Printable.PAGE_EXISTS;
		}

	}
	

	private SunDialData _data; 
	private Shapes _shapes;
	private boolean _projected = true;
	private boolean _rulerVisible;
	private Shape _printArea;

	public void setProjected(boolean projected) {
		_projected = projected;
	}

	public boolean isProjected() {
		return _projected;
	}
	

	
	void paint( Graphics2D g, SunDialPanel panel ){
		if( isRulerVisible() ){
			paintRuler(g , panel);
		}

		fillShapes(g,panel);
		Graphics2D g2 = (Graphics2D) g.create();
		setRenderingHints(g2);
		getShapes().paint(g2);
		g2.dispose();
	}

	
	public boolean isRulerVisible() {
		return _rulerVisible;
	}
	
	public void setRulerVisible( boolean visible ){
		_rulerVisible = visible;
	}
	
	protected Shape createGnomon( Graphics2D g, SunDialPanel panel ){
		SunDialData data = getData();
		Point2d gnomonBase = data.getGnomonBase();
		
		double x1 =  gnomonBase.x - data.getGnomonHeight()/2;
		double y =  gnomonBase.y;
		double x2 =  x1 + data.getGnomonHeight();

		return new Line2D.Double(x1,y,x2,y);
	}

	protected void setRenderingHints(Graphics2D g){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);
	}
	
	public Shape getPrintArea( Graphics2D g, SunDialPanel panel ){
		if( _printArea == null ){
			_printArea = createPrintArea(g, panel);
		}
		return _printArea;
	}
	
	private Shape createPrintArea( Graphics2D g, SunDialPanel panel ){
		return getData().getDialBounds();
	}
	
	protected void paintRuler(Graphics2D g, SunDialPanel panel) {
		g.setColor(Color.gray);
		g.setFont( PaintUtil.RULERFONT );
		
		AffineTransform oldT = g.getTransform();
		g.setTransform( new AffineTransform() );
		
		panel.getSize();
		Double upLeft = panel.toDial( new Point(0,0) );
		Double downRight = panel.toDial( new Point(panel.getWidth(),panel.getHeight()) );
		
		
		int s = 10;
		// HORIZONTAL
		for( int x = (int)Math.floor(upLeft.x/s)*s; x < downRight.x+1 ; x+=s ){
			Point pInPanel = panel.toPanel( new Point2D.Double(x,downRight.y) );
			g.drawString( "" +x/s, pInPanel.x, pInPanel.y-10 );
		}

		// VERTICAL
		for( int y = (int)Math.floor(upLeft.y/s)*s; y < downRight.y+1 ; y+=s ){
			Point pInPanel = panel.toPanel( new Point2D.Double(upLeft.x,y) );
			g.drawString( "" +y/s, pInPanel.x, pInPanel.y );
		}
		
		g.setTransform(oldT);
	}


	protected Point2d project( AzimutAltitude azal ){
		SunDialData data = getData();
		Point2d base = data.getGnomonBase();
		if( azal == null ){
			return null;
		}
		Point2d p = new Point2d(base.x+azal.x, base.y+azal.y);
		if( isProjected() ){
			p = GeomUtil.computeProyection(data, azal);
		}
		return p;
	}
	
	
	public void setData(SunDialData data) {
		_data = data;
	}


	public SunDialData getData() {
		if( _data == null ){
			return SunDialData.DEFAULT;
		}
		return _data;
	}




	public Shapes getShapes() {
		if (_shapes == null) {
			_shapes = new Shapes();
		}

		return _shapes;
	}

	public void refresh(){
		_shapes = null;
	}


	public Pageable createPageable(SunDialPanel panel, PageFormat pageFormat) {
		return new SunDialPageable(panel, pageFormat);
	}

	protected void fillShapes(Graphics2D g, SunDialPanel panel) {
		Shapes shapes = getShapes();
		boolean noShapes = shapes.size() == 0;
		if( noShapes ){
			Shape gnomon = createGnomon(g, panel);
			gnomon = PaintUtil.THINSTROKE.createStrokedShape(gnomon);
			shapes.add( gnomon );
			shapes.setProp(gnomon, Shapes.Prop.COLOR, Color.gray );
			
			Shape printArea = getPrintArea(g, panel);
			printArea = PaintUtil.VERYTHINSTROKE.createStrokedShape(printArea);
			shapes.add(printArea);
			shapes.setProp(printArea, Shapes.Prop.COLOR, Color.gray );
		}
	}
}
