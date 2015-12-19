package eg.edu.alexu.ehr;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import javax.naming.spi.DirStateFactory.Result;

public class ResultIterator implements Iterator<Integer> {
	public ResultIterator() {
		records_it = new Vector<>();
		max_record_it = 0;
		retrievals = new Vector<>();
		R_it=0;
	}

	// public ResultIterator(List<RecordIterator> records_it) {
	// this.records_it = records_it;
	// R_it = 0;
	// R = records_it.get(R_it);
	// max_record_it = records_it.size();
	// if (R.hasNext()) {
	// record = R.next();
	// Rs.add(record);
	// retrievals.add(R_it, Rs);
	// }
	// }

	public void addRecordIterator(RecordIterator r) {
		records_it.add(r);
		max_record_it = records_it.size();
		retrievals.add(new HashSet<>());

	}

	int R_it;
	List<RecordIterator> records_it = null;
	List<Set<Integer>> retrievals = new Vector<>();
	int max_record_it;

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
		R_it = (R_it + 1) % max_record_it;
		RecordIterator R = records_it.get(r_it);
		if (R.hasNext()) {
			int record = R.next();
			Set<Integer> Rs = retrievals.get(r_it);
			Rs.add(record);
			retrievals.add(r_it, Rs);
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
			for (Set s : retrievals) {
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
		// TODO Auto-generated method stub
		if (!hasNext())
			return null;
		else
			return getTuple();
	}

	public void replaceRecordIterator(int place, RecordIterator r) {
		records_it.set(place, r);
		retrievals.set(place, new HashSet<>());
	}

	public void replaceRecordIterator(RecordIterator r) {
		replaceRecordIterator(records_it.size() - 1, r);
	}

}
