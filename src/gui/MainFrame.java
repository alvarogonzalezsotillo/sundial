package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.TimeZone;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author alvaro
 *
 */
public class MainFrame extends JFrame{

	private SunDialPanel _sdp;
	
	private SunDialPanel createSunDialPanel(){
		return new SunDialPanel();
	}
	
	private JPanel createControlPanel(){
		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout(2, 1));
		panel.add( new ProjectedSlider((AnalemmaPainter)_sdp.getPainter()) );
		panel.add( new WallAzimutSlider((AnalemmaPainter)_sdp.getPainter()) );
		return panel;
	}
	
	public MainFrame(){
		createGui();
		setSize( 640, 480 );
	}
	
	private void createGui() {
		getContentPane().setLayout( new BorderLayout() );
		_sdp = createSunDialPanel();
		getContentPane().add( _sdp, BorderLayout.CENTER );
		getContentPane().add( createControlPanel(), BorderLayout.SOUTH );
	}

	public static void main(String[] args) {
		//showTimeZones();
		new MainFrame().setVisible(true);
	}
	

	public static void showTimeZones() {
	    String[] allTimeZones = TimeZone.getAvailableIDs();
	    
	    Arrays.sort(allTimeZones);
	
	    for (String timezone : allTimeZones) {
	        System.out.println(timezone);
	    }
	}
}
