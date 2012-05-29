package date;

import java.util.Date;

public class DateInterval {
	public Date getIni() {
		return _ini;
	}

	public Date getEnd() {
		return _end;
	}

	private Date _ini;
	private Date _end;
	
	public DateInterval(Date ini, Date end){
		_ini = ini.getTime() > end.getTime() ? end : ini;
		_end = ini.getTime() < end.getTime() ? end : ini;
	}
	
	
}
