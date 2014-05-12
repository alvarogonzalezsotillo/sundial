package gui;

import java.awt.Shape;
import java.awt.Stroke;

public class DummyStroke implements Stroke{

	@Override
	public Shape createStrokedShape(Shape p) {
		return p;
	}

}
