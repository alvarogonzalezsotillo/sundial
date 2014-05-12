package date;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;


public class DateIterable implements IDateIterable{

	
	private Date _endDate;
	private int _unit;
	private int _increment;
	private Calendar _c;
	private Date _nextDate;
	private Date _currentDate;

	public DateIterable( TimeZone tz, Date iniDate, Date endDate, int unit, int increment ){
		_endDate = endDate;
		_unit = unit;
		_increment = increment;
		_c = Calendar.getInstance(tz);
		_c.setTime(iniDate);
		_currentDate = _c.getTime();
		_c.add(_unit, _increment);
		_nextDate = _c.getTime();
	}


	@Override
	public Iterator<Date> iterator() {
		return new Iterator<Date>(){
			@Override
			public boolean hasNext() {
				return _nextDate.getTime() <= _endDate.getTime();
			}

			@Override
			public Date next() {
				Date ret = _currentDate;
				_currentDate = _nextDate;
				_c.add(_unit, _increment);
				_nextDate = _c.getTime();
				return ret; 
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}
}
