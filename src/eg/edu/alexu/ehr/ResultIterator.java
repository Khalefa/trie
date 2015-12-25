package eg.edu.alexu.ehr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ResultIterator implements Iterator<Pair> {
	int R_it;
	List<RecordIterator> records_it = null;
	List<Map<Integer, Pair>> retrievals = new Vector<>();
	int max_records_it;
	int postpone = 0;
	final int k=5;

	public ResultIterator() {
		records_it = new Vector<>();
		max_records_it = 0;
		retrievals = new Vector<>();
		R_it = 0;
	}

	public void resetAll() {
		for (RecordIterator r : records_it)
			r.reset();
	}

	public void addRecordIterator(RecordIterator r) {
		records_it.add(r);
		max_records_it = records_it.size();
		retrievals.add(new HashMap<>());
		R_it = max_records_it - 1;
		postpone = k * max_records_it;
	}

	public void remove(int l) {
		records_it.remove(l);
		retrievals.remove(l);
		max_records_it = records_it.size();
		R_it = 0;
	}

	@Override
	public boolean hasNext() {
		for (RecordIterator t : records_it) {
			if (t.hasNext())
				return true;
		}
		return false;
	}

	Pair advance() {
		int r_it = R_it;
		if (postpone > 0)
			R_it = (R_it + 1) % max_records_it;
		else
			postpone--;
		RecordIterator R = records_it.get(r_it);
		if (R.hasNext()) {
			Pair record = R.next();
			Map<Integer, Pair> Rs = retrievals.get(r_it);
			Rs.put(record.id, record);
			retrievals.set(r_it, Rs);
			return record;
		} else
			return null;
	}

	public Pair getTuple() {
		while (hasNext()) {
			Pair record = advance();
			if (record == null)
				continue;
			int i = 0;
			float weight = record.n;
			for (Map<?, ?> s : retrievals) {
				if (s.containsKey(record.id)) {
					Pair t = (Pair) (s.get(record.id));
					weight += t.n;
					i++;
				} else
					break;
			}
			if (i == retrievals.size())
				return new Pair(record.id, weight);
		}
		return null;
	}

	@Override
	public Pair next() {
		if (!hasNext())
			return null;
		else
			return getTuple();
	}

	public void replaceRecordIterator(int place, RecordIterator r) {
		records_it.set(place, r);
		retrievals.set(place, new HashMap<>());
		R_it = place;
		postpone=k*max_records_it;
	}

	public void replaceRecordIterator(RecordIterator r) {
		replaceRecordIterator(records_it.size() - 1, r);
	}

	@Override
	public String toString() {
		String s = "{" + R_it + " " + max_records_it + "}";
		for (RecordIterator r : records_it)
			s = s + r.toString();
		return s;
	}

}
