package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public abstract class SunDialSlider extends JPanel {
	
	private SunDialPainter _sdp;
	private JSlider _slider;
	private JLabel _label;

	public SunDialSlider(SunDialPainter sdp ){
		setSundialPainter(sdp);

	}

	private void setSundialPainter(SunDialPainter sdp) {
		_sdp = sdp;
		createGUI();
	}
	
	public SunDialPainter getSundialPainter(){
		return _sdp;
	}

	private JSlider createSlider(){
		return new JSlider(JSlider.HORIZONTAL, getMinimum(), getMaximum(), getValue() );
	}
	
	protected abstract  int getValue();

	protected abstract  int getMaximum();

	protected abstract  int getMinimum();

	private JLabel createLabel(){
		return new JLabel( computeLegend() );
	}
	
	protected abstract String computeLegend() ;

	private void createGUI(){
		setLayout( new GridLayout(1,2) );
		_slider= createSlider();
		_label = createLabel();
		add( _label );
		add( _slider );
		_slider.addChangeListener( new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				int value = _slider.getValue();
				_label.setText( computeLegend() );
				setValue(value);
				getSundialPainter().refresh();
				Container ancestor = SwingUtilities.getAncestorOfClass(Window.class, _slider);
				ancestor.repaint();
			}
			
		});
	}

	protected abstract void setValue(int value);
	
	
}
