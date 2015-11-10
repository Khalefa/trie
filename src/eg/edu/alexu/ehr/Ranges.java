package eg.edu.alexu.ehr;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;

public class Ranges {
	public static List<Range> combineRanges(List<Range> ranges) {
		try {
			PriorityBlockingQueue<Range> q = new PriorityBlockingQueue<Range>();
			List<Range> output_ranges = new Vector<Range>();

			for (Range g : ranges)
				q.put(g);
			while (q.size() >= 2) {
				Range r1 = q.take();
				Range r2 = q.take();
				if (r1.touches(r2) || r1.intersects(r2)) {
					Range r = new Range(r1, r2);
					q.put(r);
				} else {
					output_ranges.add(r1);
					q.put(r2);
				}
			}
			if (q.size() == 1) {
				output_ranges.add(q.take());
			}
			return output_ranges;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isCovered(List<Range> ranges, Range r) {
		for (Range l : ranges) {
			if (l.covers(r))
				return true;
		}
		return false;
	}
}
