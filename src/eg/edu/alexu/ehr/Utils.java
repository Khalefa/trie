package eg.edu.alexu.ehr;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class Utils {
	static public String getLastPrefix(String txt) {// get newest word after
													// last white
		// spaceif any or the first word if
		// no white spaces

		String wordBeingTyped = "";
		if (txt.contains(" ")) {
			int tmp = txt.lastIndexOf(" ");
			if (tmp >= 0) {
				wordBeingTyped = txt.substring(txt.lastIndexOf(" "));
			}
		} else {
			wordBeingTyped = txt;
		}
		return wordBeingTyped.trim();
	}

	static public List<Integer> intersectList(List<Integer> cur_simi, List<Integer> prev_simi) {
		if (prev_simi == null)
			return cur_simi;
		List<Integer> shorter = cur_simi;
		List<Integer> longer = prev_simi;
		if (shorter.size() > longer.size()) {
			longer = shorter;
			shorter = prev_simi;
		}
		HashSet<Integer> ht = new HashSet<>();
		ht.addAll(longer);
		List<Integer> intersect_records = new Vector<>();

		for (int t : shorter) {
			if (ht.contains(t)) {
				intersect_records.add(t);
			}
		}

		return intersect_records;

	}

	static public List<Integer> diffList(List<Integer> intersect, List<Integer> prev_list) {
		if (prev_list == null)
			return intersect;
		List<Integer> diff_records = new Vector<>(prev_list);
		diff_records.removeAll(intersect);

		return diff_records;

	}
	static public List<Integer> UnionList(List<Integer> a, List<Integer> b) {
		if (a == null)
			return new Vector<>(b);
		if(b==null) return new Vector<>(a);
		
		List<Integer> union = new Vector<>(a);
		HashSet<Integer> exists=new HashSet<>(a);
		for(Integer ib:b){
			if(!exists.contains(ib))
				union.add(ib);
		}
	
		return union;

	}
	public static boolean verbose=false;

}
