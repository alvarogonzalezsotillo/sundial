package gui;
/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/



import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.Timer;

public class TextStroke implements Stroke {
	private String text;
	private Font font;
	private boolean stretchToFit = false;
	private boolean repeat = false;
	private AffineTransform t = new AffineTransform();
	private boolean left;
	private boolean _middle=false;

	public boolean isMiddle() {
		return _middle;
	}

	public void setMiddle(boolean middle) {
		_middle = middle;
	}

	private static final float FLATNESS = 10;

	public TextStroke( String text, Font font ) {
		this( text, font, true, false, true );
	}

	private static void debug( String s ){
	}
	
	public TextStroke( String text, Font font, boolean stretchToFit, boolean repeat, boolean left ) {
		debug( "TextStroke:" + text );
		this.text = text;
		this.font = font;
		this.stretchToFit = stretchToFit;
		this.repeat = repeat;
		this.left = left;
	}

	public Shape createStrokedShape( Shape shape ) {
		
		if( !left ){
			return createStrokedShapeFromRigth(shape);
		}
		
		FontRenderContext frc = new FontRenderContext(null, true, true);
		GlyphVector glyphVector = font.createGlyphVector(frc, text);
		float fontSize = getFontSize();

		GeneralPath result = new GeneralPath();
		PathIterator it = new FlatteningPathIterator( shape.getPathIterator( null ), FLATNESS );
		float points[] = new float[6];
		float moveX = 0, moveY = 0;
		float lastX = 0, lastY = 0;
		float thisX = 0, thisY = 0;
		int type = 0;
		float next = 0;
		int currentChar = 0;
		int length = glyphVector.getNumGlyphs();

		if ( length == 0 )
            return result;

        float pathLength = PaintUtil.measurePathLength( shape, FLATNESS );
		float factor = stretchToFit ? pathLength/(float)glyphVector.getLogicalBounds().getWidth() : 1.0f;
        float nextAdvance = 0;

		while ( currentChar < length && !it.isDone() ) {
			type = it.currentSegment( points );
			switch( type ){
			case PathIterator.SEG_MOVETO:
				moveX = lastX = points[0];
				moveY = lastY = points[1];
				result.moveTo( moveX, moveY );
                nextAdvance = glyphVector.getGlyphMetrics( currentChar ).getAdvance() * 0.5f;
                next = nextAdvance;
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
				float distance = (float)Math.sqrt( dx*dx + dy*dy );
				if ( distance >= next ) {
					float r = 1.0f/distance;
					float angle = (float)Math.atan2( dy, dx );
					while ( currentChar < length && distance >= next ) {
						Shape glyph = glyphVector.getGlyphOutline( currentChar );
						Point2D p = glyphVector.getGlyphPosition(currentChar);
						float px = (float)p.getX();
						float py = (float)p.getY();
						float x = lastX + next*dx*r;
						float y = lastY + next*dy*r;
                        float advance = nextAdvance;
                        int nextChar = currentChar+1;
                        if( repeat ){
                        	nextChar = nextChar%length;
                        }
                        if( nextChar < length ){
                        	nextAdvance = glyphVector.getGlyphMetrics(nextChar).getAdvance() * 0.5f;
                        }
                        t.setToIdentity();
                        t.translate( x, y );
                        t.rotate( angle );
                        if(_middle) t.translate(0, +fontSize/4);
                        t.translate( -px-advance, -py );
						result.append( t.createTransformedShape( glyph ), false );
						next += (advance+nextAdvance) * factor;
						currentChar = nextChar;
					}
				}
                next -= distance;
				lastX = thisX;
				lastY = thisY;
				break;
			}
			it.next();
		}

		return result;
	}

	private float getFontSize() {
		return font.getSize2D();
	}

	
	public static void main(String[] args) {
		JFrame f = new JFrame(){
			
			private float _angle = 0;
			
			private Timer t = new Timer(10, new ActionListener(){


				@Override
				public void actionPerformed(ActionEvent e) {
					_angle += .02;
					repaint();
				}
				
			}){
				{
					start();
				}
			};
			
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				if( !(g instanceof Graphics2D) ){
					return;
				}
				try{
					int centerx = 200;
					int centery = 200;
					int radius = 100;
					int x1 = (int) (centerx + radius*Math.cos(_angle));
					int y1 = (int) (centery + radius*Math.sin(_angle));
					int x2 = (int) (centerx + radius*Math.cos(_angle+Math.PI));
					int y2 = (int) (centery + radius*Math.sin(_angle+Math.PI));
					
					Graphics2D g2d = (Graphics2D) g;
					
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
					Stroke s = g2d.getStroke();
					g2d.drawLine(x1, y1, x2, y2);
					g2d.setStroke( new TextStroke( "hola que tal", getFont(), false, false, true ) );
					g2d.drawLine(x1, y1, x2, y2);
					g2d.setStroke(s);
	
					g2d.drawLine(x1, y1+centery, x2, y2+centery);
					g2d.setStroke( new TextStroke( "Adios hasta luego", getFont(), false, false, false ) );
					g2d.drawLine(x1, y1+centery, x2, y2+centery);
				}
				catch( Throwable t ){
					t.printStackTrace();
				}
			}
		};
		
		f.show();
	}
	
	public Shape createStrokedShapeFromRigth( Shape shape ) {
		
		FontRenderContext frc = new FontRenderContext(null, true, true);
		GlyphVector glyphVector = font.createGlyphVector(frc, text);
		float fontSize = getFontSize();

		GeneralPath result = new GeneralPath();
		PathIterator pathIterator = shape.getPathIterator( null );
		pathIterator = ReversePathIterator.reverse(pathIterator);
		PathIterator it = new FlatteningPathIterator( pathIterator, FLATNESS );
		float points[] = new float[6];
		float moveX = 0, moveY = 0;
		float lastX = 0, lastY = 0;
		float thisX = 0, thisY = 0;
		int type = 0;
		float next = 0;
		int currentChar = text.length()-1;
		int length = glyphVector.getNumGlyphs();

		if ( length == 0 )
            return result;

        float pathLength = PaintUtil.measurePathLength( shape, FLATNESS );
		float factor = stretchToFit ? pathLength/(float)glyphVector.getLogicalBounds().getWidth() : 1.0f;
        float nextAdvance = 0;

		while ( currentChar >= 0 && !it.isDone() ) {
			type = it.currentSegment( points );
			switch( type ){
			case PathIterator.SEG_MOVETO:
				moveX = lastX = points[0];
				moveY = lastY = points[1];
				result.moveTo( moveX, moveY );
                nextAdvance = glyphVector.getGlyphMetrics( currentChar ).getAdvance() * 0.5f;
                next = nextAdvance;
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
				float distance = (float)Math.sqrt( dx*dx + dy*dy );
				if ( distance >= next ) {
					float r = 1.0f/distance;
					float angle = (float)Math.atan2( dy, dx );
					while ( currentChar >= 0 && distance >= next ) {
						debug( "currentChar:" + currentChar );
						Shape glyph = glyphVector.getGlyphOutline( currentChar );
						Point2D p = glyphVector.getGlyphPosition(currentChar);
						float px = (float)p.getX();
						float py = (float)p.getY();
						float x = lastX + next*dx*r;
						float y = lastY + next*dy*r;
                        float advance = nextAdvance;
                        int nextChar = currentChar-1;
                        if( repeat ){
                        	nextChar = (nextChar+length)%length;
                        }
                        if( nextChar >= 0 ){
                        	nextAdvance = glyphVector.getGlyphMetrics(nextChar).getAdvance() * 0.5f;
                        }
						t.setToTranslation( x, y );
						t.rotate( angle + Math.PI );
                        if(_middle) t.translate(0, +fontSize/4);
						t.translate( -px-advance, -py );
						result.append( t.createTransformedShape( glyph ), false );
						next += (advance+nextAdvance) * factor;
						currentChar = nextChar;
					}
				}
                next -= distance;
				lastX = thisX;
				lastY = thisY;
				break;
			}
			it.next();
		}

		return result;
	}
	
}
