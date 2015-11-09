package eg.edu.alexu.ehr;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Range implements Comparator<Range>, Comparable<Range> {
	int min;
	int max;

	public Range(int min, int max) {
		this.min = Math.min(min, max);
		this.max = Math.max(min, max);
	}

	public Range(Range r1, Range r2) {
		this(Math.min(r1.min, r2.min), Math.max(r2.max, r1.max));
	}

	public Range(Range r) {
		min = r.min;
		max = r.max;
	}

	@Override
	public String toString() {
		return "[" + min + "," + max + "]";
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public Range addRange(Range r) {
		return new Range(Math.min(min, r.min), Math.max(max, r.max));
	}

	public boolean touches(Range r) {
		if (min == r.max + 1)
			return true;
		if (r.min == max + 1)
			return true;
		return false;
	}

	public boolean in(int x) {
		if (x >= min && x <= max)
			return true;
		return false;
	}

	public boolean intersects(Range b) {
		if (this.in(b.min))
			return true;
		if (this.in(b.max))
			return true;
		return false;
	}

	public int compareTo(Range o) {
		if (o.min == min) {
			return Integer.compare(max, o.max);
		}
		return Integer.compare(min, o.min);
	}

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

	@Override
	public int compare(Range r1, Range r2) {
		return r1.compareTo(r2);
	}

}
