package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Shapes {

	
	
	public enum Prop{STROKE,COLOR};
	
	private Set<Shape> _shapes;
	
	private Map<Shape,Map<Prop,Object>> _props;


	public Set<Shape> getShapes() {
		if (_shapes == null) {
			_shapes = new HashSet<Shape>();
		}
		return _shapes;
	}



	private Map<Shape,Map<Prop,Object>> getProps() {
		if (_props == null) {
			_props = new HashMap<Shape,Map<Prop,Object>>();
			
		}

		return _props;
	}
	
	
	public Map<Prop,Object> getProps(Shape s){
		return getPropsImpl(s, false);
	}



	private Map<Prop, Object> getPropsImpl(Shape s, boolean create) {
		Map<Prop, Object> map = getProps().get(s);
		if( map == null ){
			map = new HashMap<Prop,Object>();
			getProps().put(s, map);
		}
		return map;
	}
	
	public void setProp( Shape s, Prop prop, Object value ){
		 Map<Prop, Object> props = getPropsImpl(s,true);
		 props.put(prop,value);
	}

	public void setProp( Collection<Shape> s, Prop prop, Object value ){
		for (Shape shape : s) {
			setProp(shape,prop,value);
		}
	}
	
	public void paint( Graphics2D g ){
		Stroke defaultStroke = PaintUtil.DUMMYSTROKE;
		Color defaultColor = Color.black;
	
		for (Shape s : getShapes() ) {
			Map<Prop, Object> props = getProps(s);
			if( props != null && props.get(Prop.COLOR) != null ){
				g.setColor( (Color) props.get(Prop.COLOR) );
			}
			else{
				g.setColor(defaultColor);
			}

			if( props != null && props.get(Prop.STROKE ) != null ){
				g.setStroke( (Stroke) props.get(Prop.STROKE) );
			}
			else{
				g.setStroke(defaultStroke);
			}
			
			g.draw(s);
		}
	}

	public int size() {
		return getShapes().size();
	}

	public void add(Shape s) {
		if( s == null ){
			return;
		}
		getShapes().add(s);
	}

	public void addAll( Collection<Shape> shapes ) {
		for (Shape shape : shapes) {
			add(shape);
		}
	}



	public Rectangle2D getBounds() {
		Rectangle2D ret = null;
		for (Shape shape : getShapes() ) {
			Rectangle2D b = shape.getBounds2D();
			if( ret == null ){
				ret = b;
			}
			else{
				ret.add(b);
			}
		}
		
		return ret;
	}
}
