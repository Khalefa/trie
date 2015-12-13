package eg.edu.alexu.ehr;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

public class ResultIterator implements Iterator<Integer> {

	public ResultIterator(List<RecordIterator> records_it) {
		this.records_it = records_it;
		Rs = new HashSet<>();
		R_it = 0;
		R = records_it.get(R_it);
		max_record_it = records_it.size();
		if (R.hasNext()) {
			record = R.next();
			Rs.add(record);
			retrievals.add(R_it, Rs);
		}
	}

	int R_it;
	int record;
	RecordIterator R;
	List<RecordIterator> records_it = null;
	Set<Integer> Rs = null;
	List<Set<Integer>> retrievals = new Vector<>();
	int max_record_it;

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		for (RecordIterator t : records_it) {
			if (t.hasNext())
				return true;
		}
		return false;
	}

	void advance() {
		R_it = (R_it + 1) % max_record_it;
		R = records_it.get(R_it);
		if (R.hasNext()) {
			record = R.next();
			Rs.add(record);
			retrievals.add(R_it, Rs);
		}
	}

	public Integer intersect() {
		Integer ret = -1;
		while (ret == -1) {
			int i = 0;
			for (Set s : retrievals) {
				if (s.contains(record))
					i++;
			}
			if (i == retrievals.size())
				ret = record;
			advance();
		}
		return ret;
	}

	@Override
	public Integer next() {
		// TODO Auto-generated method stub
		if (!hasNext())
			throw new NoSuchElementException();

		return intersect();
	}

}