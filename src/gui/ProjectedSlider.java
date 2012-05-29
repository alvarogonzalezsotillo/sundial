package gui;

import javax.swing.JFrame;

public class ProjectedSlider extends SunDialSlider {

	public ProjectedSlider(AnalemmaPainter p){
		super(p);
	}
	
	@Override
	protected String computeLegend() {
		return getValue()==1?"Proyectado":"Sin proyectar";
	}

	@Override
	protected int getMaximum() {
		return 1;
	}

	@Override
	protected int getMinimum() {
		return 0;
	}

	@Override
	protected int getValue() {
		return ((AnalemmaPainter)getSundialPainter()).isProjected()?1:0;
	}

	@Override
	protected void setValue(int value) {
		((AnalemmaPainter)getSundialPainter()).setProjected(value!=0?true:false);
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.getContentPane().add( new ProjectedSlider( new AnalemmaPainter()));
		f.setVisible(true);
		System.out.println( f );
	}

}
