package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import computation.Point2d;

public class StringStroke implements Stroke{

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		_text = text;
	}

	public Font getFont() {
		return _font;
	}

	public void setFont(Font font) {
		_font = font;
	}

	public boolean isRepeat() {
		return _repeat;
	}

	public void setRepeat(boolean repeat) {
		_repeat = repeat;
	}

	public boolean isLeftAligned() {
		return _leftAligned;
	}

	public void setLeftAligned(boolean leftAligned) {
		_leftAligned = leftAligned;
	}

	private String _text;
	private Font _font;
	private boolean _repeat;
	private boolean _leftAligned = false;

	public StringStroke( String text, Font font ){
		setText(text);
		setFont(font);
	}
	
	@Override
	public Shape createStrokedShape(Shape s) {
		FontRenderContext frc = new FontRenderContext(null, true, true);
		GlyphVector gv = getFont().createGlyphVector(frc, getText());
		int glyphIndex = 0;
		int numGlyphs = gv.getNumGlyphs();
		
		StepPathIterator spi = new StepPathIterator(s, isLeftAligned() );
		Point2d p1 = spi.nextPoint(0);
		Point2d p2 = p1;
		Path2D.Double ret = new Path2D.Double();
		AffineTransform at = new AffineTransform();
		
		while( !spi.isDone() ){
			log( "otro" );
			double step;
			
			if( glyphIndex < numGlyphs -1 ){
				step = gv.getGlyphPosition(glyphIndex+1).getX() - gv.getGlyphPosition(glyphIndex).getX();
			}
			else{
				GlyphMetrics metrics = gv.getGlyphMetrics(glyphIndex);
				step = metrics.getAdvanceX();
			}
	
			p1 = p2;
			p2 = spi.nextPoint(step);
			
			double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x );
			Shape outline = gv.getGlyphOutline(glyphIndex);
			at.setToIdentity();
			at.translate(p1.x, p1.y);
			at.rotate(angle);
			at.translate(-gv.getGlyphPosition(glyphIndex).getX(), 0);
		
			log( "  p1:" + p1 );
			log( "  p2:" + p2 );
			log( "  angle:" + angle );
			log( "  step:" + step );
			
			outline = at.createTransformedShape(outline);
			
			ret.append(outline, false);
			
			glyphIndex++;
			if( !isRepeat() && glyphIndex == numGlyphs ){
				break;
			}
			
			while( glyphIndex >= numGlyphs ){
				glyphIndex -= numGlyphs;
			}
			
			while( glyphIndex < 0 ){
				glyphIndex += numGlyphs;
			}
		}

		return ret;
	}

	private void log(String string) {
		System.out.println( string );
	}

	public static void main(String[] args) {
		

		final JFrame f = new JFrame("StringStroke"){
			StringStroke _stroke = new StringStroke( "alvaro gonzalez sotillo", Font.decode( "Verdana-14" ) );
			
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
				
				final Container cp = getContentPane();
				cp.setLayout( new BorderLayout() );
				cp.add( new JCheckBox("repeat"){
					{
						addActionListener( new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								_stroke.setRepeat( isSelected() );
								SwingUtilities.getRoot(cp).repaint();
							}
							
						});
					}
				}, BorderLayout.NORTH );
				
				cp.add( new JCheckBox("left"){
					{
						addActionListener( new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								_stroke.setLeftAligned(isSelected());
								SwingUtilities.getRoot(cp).repaint();
							}
							
						});
					}
				}, BorderLayout.SOUTH );
			}
			
			public void paint(Graphics g) {
				super.paint(g);
				//Shape s = new Ellipse2D.Double(200, 200, 200, 175 );
				//Shape s = new Line2D.Double( 100, 100, 200 , 200 );
				//Shape s = new Polygon( new int[]{ 100, 200, 300, 400, 500 }, new int[]{ 100, 100, 200, 500, 600 }, 5 );
				Shape s = PaintUtil.getShapeFromText("hola", 100, 100, Font.decode("Verdana-300"), true);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
				g2d.draw(s);

				g2d.setStroke(_stroke);
				g2d.draw( s );
				System.err.print( "." );
			};
		};
		
		f.setSize( 300, 300 );
		f.setVisible(true);
	}

}
