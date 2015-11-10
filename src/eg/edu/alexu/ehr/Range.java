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
    public boolean covers(Range r){
    	if(this.min<=r.min && this.max>=r.max) return true;
    	return false;
    }
	public int compareTo(Range o) {
		if (o.min == min) {
			return Integer.compare(max, o.max);
		}
		return Integer.compare(min, o.min);
	}

	
	@Override
	public int compare(Range r1, Range r2) {
		return r1.compareTo(r2);
	}

	public void setMax(int len) {
		this.max=len;		
	}
	public void setMin(int len) {
		this.min=len;		
	}

	public void include(int len) {
	if(len<min)
		min=len;
	if(len>max)
		max=len;
	}

}
