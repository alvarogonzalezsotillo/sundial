package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import javax.swing.JPanel;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import computation.AzimutAltitudeFinder;
import computation.Point2d;
import computation.SunDialData;
import computation.SunPosition;
import computation.geom.AzimutAltitude;
import computation.geom.GeomUtil;


public class SunDialPanel extends JPanel{
	
	private static final float ZOOMFACTOR = 1.2f;

	private SunDialPainter _painter;

	private AffineTransform _af = new AffineTransform();
	
	public SunDialPanel(){
		this(new AnalemmaPainter());
	}
	
	private class Listener implements MouseListener, MouseMotionListener, KeyListener{
		
		private static final boolean SHOWPOINTERINFO = true;
		private Point _startOfDrag;

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println( "mouseclicked" );
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println( "mousepressed" );
			Cursor c = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
			SunDialPanel.this.setCursor(c);
			Point point = e.getPoint();
			_startOfDrag = point;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println( "mousereleased" );
			Cursor c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
			SunDialPanel.this.setCursor(c);
			Point endOfDrag = e.getPoint();
			translate(_startOfDrag, endOfDrag);
		}


		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println( "mousedragged" );			
			Cursor c = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
			SunDialPanel.this.setCursor(c);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			System.out.println( "mousemoved" );
			if( !SHOWPOINTERINFO ){
				return;
			}
			
			Point p = e.getPoint();
			Point2D pDial = toDial(p);
			if( GeomUtil.PARANOID ){
				Point pPanel = toPanel(pDial);
				if( !GeomUtil.areEqual(p.getX(), pPanel.getX(), 1) ){
					System.out.println( pDial );
					System.out.println( pPanel );
					throw new IllegalStateException();
				}
				if( !GeomUtil.areEqual(p.getY(), pPanel.getY(), 1) ){
					System.out.println( pDial );
					System.out.println( pPanel );
					throw new IllegalStateException();
				}
			}
			
			Graphics g = getGraphics();
			g.setColor( getForeground() );
			g.fillRect(0, 0, getWidth(), 40);
			g.setColor( getBackground() );
			String str = "Panel:" + p.x + "," + p.y + "  Dial:" + pDial.getX() +"," + pDial.getY();
			g.drawString(str, 0, 15);
			
			SunDialData data = getPainter().getData();
			
			AzimutAltitude azal = azimutAltitudefromPanel(p);
			str = azal.toString();
			g.drawString( str, 0, 35);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if( e.getKeyCode() == KeyEvent.VK_UP ){
				translate( 0, 10 );
			}
			if( e.getKeyCode() == KeyEvent.VK_DOWN ){
				translate( 0, -10 );
			}
			if( e.getKeyCode() == KeyEvent.VK_LEFT ){
				translate( 10, 0 );
			}
			if( e.getKeyCode() == KeyEvent.VK_RIGHT ){
				translate( -10, 0 );
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println( "keyReleased" );
			SunDialData data = getPainter().getData();
			if( e.getKeyCode() == KeyEvent.VK_Q ){
				PointerInfo pointerInfo = MouseInfo.getPointerInfo();
				Point mouse = pointerInfo.getLocation();
				Point comp = SunDialPanel.this.getLocationOnScreen();
				Point pInPanel = new Point( mouse.x-comp.x, mouse.y-comp.y);
				AzimutAltitude azal = azimutAltitudefromPanel(pInPanel);
				Date[] findAzimutAltitude = AzimutAltitudeFinder.findAzimutAltitude(data, SunPosition.REFERENCEYEAR, azal);
				System.out.println( "findAzimutAltitude:" + findAzimutAltitude.length );
				System.out.println( "pInPanel:" + pInPanel.toString() );
				for (Date date : findAzimutAltitude) {
					System.out.println( "  " + date );
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			System.out.println( "keyTyped" );
			if( e.getKeyChar() == '+' ){
				zoomIn( getCenterPanel() );
			}
			if( e.getKeyChar() == '-' ){
				zoomOut( getCenterPanel() );
			}
			if( e.getKeyChar() == 'i' ){
				initZoom();
			}
			if( e.getKeyChar() == 'p' ){
				printShapes();
			}
			if( e.getKeyChar() == 's' ){
				try {
					generateSVG();
					generatePDF();
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}


		
	}

	private void initZoom() {
		_af.setToIdentity();
		repaint();
	}
	
	private SVGGraphics2D createSVGGraphics(){
		// Get a DOMImplementation.
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);

		// Create an instance of the SVG Generator.
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// Ask the test to render into the SVG Graphics2D implementation.
		getPainter().paint(svgGenerator, this);

		return svgGenerator;
	}

	private Document createSVGDocument(AffineTransform at){
		// Get a DOMImplementation.
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);

		// Create an instance of the SVG Generator.
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// Ask the test to render into the SVG Graphics2D implementation.
		svgGenerator.transform(at);
		getPainter().paint(svgGenerator, this);

        Element root = document.getDocumentElement();
        svgGenerator.getRoot(root);
        
		return document;
	}
	
	public void generateSVG() throws Exception {

		SVGGraphics2D svgGenerator = createSVGGraphics();

		// Finally, stream out SVG to the standard output using
		// UTF-8 encoding.
		boolean useCSS = true; // we want to use CSS style attributes
		Writer out = new OutputStreamWriter( new FileOutputStream("kk.svg"), "UTF-8");
		svgGenerator.stream(out, useCSS);
		out.close();
		System.out.println( "Creado SVG");
	}
	
	public void generatePDF() throws Exception{
		PDFTranscoder t = new PDFTranscoder();
		
//		132,3  264,6 mm -- 500 1000
//		210,0  297,0 mm --  
		Rectangle2D.Float pdfRect = new Rectangle2D.Float( 0, 0, 500*210/132.3f, 1000*297/264.6f );
		
		t.addTranscodingHint( PDFTranscoder.KEY_HEIGHT, (float)pdfRect.getHeight());
		t.addTranscodingHint( PDFTranscoder.KEY_WIDTH, (float)pdfRect.getWidth());
		
		Rectangle2D pdfRectInPixels = new Rectangle2D.Double( 0, 0, pdfRect.getWidth(), pdfRect.getHeight() );
		SunDialData data = getPainter().getData();
		Rectangle r = data.getDialBounds();
		int margin = 5;
		r = new Rectangle( r.x - margin, r.y - margin, r.width + margin*2, r.height + margin*2 );
		AffineTransform at = PaintUtil.computeBestFit(r, pdfRectInPixels);
		
		Document document = createSVGDocument(at);
		TranscoderInput input = new TranscoderInput(document);
		FileOutputStream fout = new FileOutputStream( "kk.pdf" );
		TranscoderOutput output = new TranscoderOutput( fout );
		t.transcode(input, output);
		fout.close();
		System.out.println( "Creado PDF");
	}

	public void printShapes() {
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		boolean print = printerJob.printDialog();
		if( !print ){
			return;
		}
		PageFormat pageFormat = printerJob.defaultPage();
		pageFormat = tunePageFormat( pageFormat );
		printerJob.setPageable( getPainter().createPageable(this, pageFormat) );
		try {
			printerJob.print();
		}
		catch (PrinterException e) {
			e.printStackTrace();
		}
	}
	private PageFormat tunePageFormat(PageFormat pageFormat) {
		Paper paper = pageFormat.getPaper();
		double margin = 1;
		double w = paper.getWidth();
		double h = paper.getHeight();
		
		paper.setImageableArea(margin, margin, w-margin, h-margin);
		pageFormat.setPaper(paper);
		return pageFormat;
	}

	private Point getCenterPanel(){
		double x = getBounds().getCenterX();
		double y = getBounds().getCenterY();
		return new Point((int)x,(int)y);
	}
	
	
	private void zoomIn( Point pointToPanel ) {
		zoom(pointToPanel,ZOOMFACTOR);
	}
	
	private void zoomOut(Point pointToPanel){
		zoom(pointToPanel,1.f/ZOOMFACTOR);
	}
	
	private void zoom(Point pointToPanel, float zoomFactor){
		Point2D oldToDial = toDial(pointToPanel);
		
		_af.scale(zoomFactor, zoomFactor);
		Point newPointToPanel = toPanel( oldToDial );

		System.out.println( "zoom");
		System.out.println( "  zoomFactor:" + zoomFactor );
		System.out.println( "  pointToPanel:" + pointToPanel );
		System.out.println( "  oldToDial:" + oldToDial );
		System.out.println( "  newPointToPanel:" + newPointToPanel );

		translate(newPointToPanel,pointToPanel);
		repaint();
	}
	

	public Point2D.Double toDial(Point pInPanel){
		Point2D.Double ret = new Point2D.Double();
		try {
			_af.inverseTransform(pInPanel, ret);
		} catch (NoninvertibleTransformException exception) {
			exception.printStackTrace();
		}
		return ret;
	}
	
	private AzimutAltitude azimutAltitudefromPanel(Point pInPanel){
		Point2D dial = toDial(pInPanel);
		SunDialData data = getPainter().getData();
		AzimutAltitude azal = GeomUtil.computeAzimutAltitude(data, new Point2d(dial) );
		return azal;
	}
	
	public Point toPanel(Point2D pInDial){
		Point ret = new Point();
		_af.transform(pInDial, ret);
		return ret;
	}

	
	private void translate(Point pInPanelSrc, Point pInPanelDst) {
		
//		System.out.println( "translate");
//		System.out.println( "  pInPanelSrc:" + pInPanelSrc );
//		System.out.println( "  pInPanelDst:" + pInPanelDst );
		
		Point pSrc = pInPanelSrc;
		Point pDst = pInPanelDst;
		
		double tx = pDst.getX() - pSrc.getX();
		double ty = pDst.getY() - pSrc.getY();
		translate(tx, ty);
	}

	private void translate(double tx, double ty) {
		AffineTransform t = AffineTransform.getTranslateInstance(tx, ty);
		_af.preConcatenate(t);
		
		SunDialPanel.this.repaint();
	}
	
	public SunDialPanel(SunDialPainter painter){
		setPainter(painter);
		Listener l = new Listener();
		addMouseListener( l );
		addMouseMotionListener(l);
		addKeyListener(l);
		setFocusable(true);
	}
	
	public void setPainter(SunDialPainter painter) {
		_painter = painter;
	}

	public SunDialPainter getPainter() {
		return _painter;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		SunDialPainter painter = getPainter();
		if( painter != null ){
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform oldAf = g2.getTransform();
			g2.transform(_af);
			painter.paint(g2, this);
			g2.setTransform(oldAf);
		}
	}
	
}
