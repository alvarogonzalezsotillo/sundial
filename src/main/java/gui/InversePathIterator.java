package gui;

import java.awt.geom.PathIterator;
import java.util.ArrayList;

public class InversePathIterator implements PathIterator{

	private ArrayList<double[]> _coords = new ArrayList<double[]>();
	private ArrayList<Integer> _types = new ArrayList<Integer>();
	private PathIterator _it;
	private int _index = 0;
	
	public InversePathIterator(PathIterator it){
		_it = it;
		readPathIterator();
	}

	private void readPathIterator() {
		double[] coords = new double[6];
		int type;
		
		do{
			type = _it.currentSegment(coords);
			_coords.add(coords);
			_types.add(type);
		}while( !_it.isDone() );
	}

	@Override
	public int getWindingRule() {
		return _it.getWindingRule();
	}

	@Override
	public boolean isDone() {
		return _index >= 0;
	}

	@Override
	public void next() {
		_index--;
	}

	@Override
	public int currentSegment(float[] coords) {
		double[] c = new double[6];
		int ret = currentSegment( c );
		coords[0] = (float) c[0];
		coords[1] = (float) c[1];
		coords[2] = (float) c[2];
		coords[3] = (float) c[3];
		coords[4] = (float) c[4];
		coords[5] = (float) c[5];
		
		return ret;
	}

	@Override
	public int currentSegment(double[] coords) {
		throw new UnsupportedOperationException();
	}
}
