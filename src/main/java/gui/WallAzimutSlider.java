package gui;

import computation.geom.AzimutAltitude;

public class WallAzimutSlider extends SunDialSlider {

	public WallAzimutSlider(SunDialPainter sdp) {
		super(sdp);
	}

	@Override
	protected String computeLegend() {
		return "Azimut de la pared:" + getValue();
	}

	@Override
	protected int getMaximum() {
		return 180;
	}

	@Override
	protected int getMinimum() {
		return -180;
	}

	@Override
	protected int getValue() {
		return (int) getSundialPainter().getData().getWallNormal().getAzimut();
	}

	@Override
	protected void setValue(int value) {
		// TODO Auto-generated method stub
		AzimutAltitude azal = getSundialPainter().getData().getWallNormal();
		getSundialPainter().getData().setWallNormal( new AzimutAltitude(value, azal.getAltitude() ) );
	}

}
