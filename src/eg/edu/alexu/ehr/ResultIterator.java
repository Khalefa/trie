package eg.edu.alexu.ehr;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ResultIterator implements Iterator<Integer> {
	public ResultIterator() {
		records_it = new Vector<>();
		max_records_it = 0;
		retrievals = new Vector<>();
		R_it = 0;
	}

	public void resetAll(){		
		for(RecordIterator r :records_it)
			r.reset();
	}
	public void addRecordIterator(RecordIterator r) {
		records_it.add(r);
		max_records_it = records_it.size();
		retrievals.add(new HashSet<>());
	}
	public void remove(int l){
		records_it.remove(l);
		retrievals.remove(l);
		max_records_it=records_it.size();
		R_it=0;
	}
	

	int R_it;
	List<RecordIterator> records_it = null;
	List<Set<Integer>> retrievals = new Vector<>();
	int max_records_it;

	@Override
	public boolean hasNext() {
		for (RecordIterator t : records_it) {
			if (t.hasNext())
				return true;
		}
		return false;
	}

	Integer advance() {
		int r_it = R_it;
		R_it = (R_it + 1) % max_records_it;
		RecordIterator R = records_it.get(r_it);
		if (R.hasNext()) {
			int record = R.next();
			Set<Integer> Rs = retrievals.get(r_it);
			Rs.add(record);
			retrievals.set(r_it, Rs);
			return record;
		} else
			return null;
	}

	public Integer getTuple() {
		while (hasNext()) {
			Integer record = advance();
			if (record == null)
				continue;
			int i = 0;
			for (Set<?> s : retrievals) {
				if (s.contains(record))
					i++;
				else
					break;
			}
			if (i == retrievals.size())
				return record;
		}
		return null;
	}

	@Override
	public Integer next() {
		if (!hasNext())
			return null;
		else
			return getTuple();
	}

	public void replaceRecordIterator(int place, RecordIterator r) {
		records_it.set(place, r);
		retrievals.set(place, new HashSet<>());
		R_it=place;
	}

	public void replaceRecordIterator(RecordIterator r) {
		replaceRecordIterator(records_it.size() - 1, r);
	}
	@Override
	public String toString() {	
		String s="{"+ R_it+" "+ max_records_it+"}";
		for(RecordIterator r:records_it)s=s+r.toString();
		return s;
	}

}
