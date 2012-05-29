package date;

import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

public class ListDateIterable implements IDateIterable{

	private TreeSet<Date> _dates = new TreeSet<Date>();
	private Iterator<Date> _iterator;
	
	public ListDateIterable(){
	}

	public ListDateIterable(Iterable<Date> dates){
		addDates(dates);
	}
	
	public void addDates( Iterable<Date> dates ){
		for (Date date : dates) {
			_dates.add(date);
		}
	}
	
	public void addDate( Date date ){
		_dates.add(date);
	}
	
	public boolean hasNext() {
		return _iterator.hasNext();
	}

	public Date next() {
		return _iterator.next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Date> iterator() {
		return _dates.iterator();
	}
	
	public static IDateIterable merge( IDateIterable it1, IDateIterable it2 ){
		ListDateIterable ret = new ListDateIterable();
		ret.addDates(it1);
		ret.addDates(it2);
		return ret;
	}
	
	public static IDateIterable merge( IDateIterable it, Date d ){
		ListDateIterable ret = new ListDateIterable();
		ret.addDates(it);
		ret.addDate(d);
		return ret;
	}
	
	@Override
	public String toString() {
		return _dates.toString();
	}
	
	public ListDateIterable copy(){
		ListDateIterable ret = new ListDateIterable();
		ret.addDates( _dates );
		return ret;
	}
}
