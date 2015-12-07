package eg.edu.alexu.ehr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

public class RecordIterator implements Iterator<Integer> {
	private PivotalTrie trie;
	// set of active nodes
	List<PivotalActiveNode> sorted_nodes = null;

	public RecordIterator(PivotalTrie trie, Map<BasicTrieNode, PivotalActiveNode> trienodes) {
		this.trie = trie;
		sorted_nodes = new Vector<>();
		sorted_nodes.addAll(trienodes.values());
		Collections.sort(sorted_nodes);
		if (sorted_nodes == null || sorted_nodes.size() == 0) {
			active_it = max_active_it = -1;
			range_it = max_range_it = -1;
			record_it = max_record_it = -1;

		} else {
			active_it = 0;
			record_it = 0;
			n = (TrieNode) (sorted_nodes.get(active_it).node);
			range_it = n.rID.min;
			String word = trie.dictionary.get(range_it);
			keyword_records = trie.inverted_list.get(word);
			// Initialize maximum
			max_active_it = sorted_nodes.size();
			max_range_it = n.rID.max + 1;
			max_record_it = keyword_records.size();
		}
	}

	int active_it = -1;
	int max_active_it = 0;
	int range_it = -1;
	int max_range_it = 0;
	int record_it = -1;
	int max_record_it = 0;

	TrieNode n = null;

	List<Integer> keyword_records;

	@Override
	public boolean hasNext() {
		if ((record_it == max_record_it) && (range_it == max_range_it) && (active_it == max_active_it))
			return false;
		else
			return true;
	}

	void advance() {
		record_it++;

		if (record_it == max_record_it) {
			record_it = 0;
			range_it++;
			if (range_it == max_range_it) {
				active_it++;
				if (active_it == max_active_it) {
					active_it = max_active_it = -1;
					range_it = max_range_it = -1;
					record_it = max_record_it = -1;
					return;
				} else { // active
					n = (TrieNode) (sorted_nodes.get(active_it).node);
					range_it = n.rID.min;
					String word = trie.dictionary.get(range_it);
					keyword_records = trie.inverted_list.get(word);
					max_range_it = n.rID.max + 1;
					max_record_it = keyword_records.size();
				}

			} else {// range
				String word = trie.dictionary.get(range_it);
				keyword_records = trie.inverted_list.get(word);
				max_record_it = keyword_records.size();
			}
		}
	}

	@Override
	public Integer next() {
		if (!hasNext())
			throw new NoSuchElementException();
		Integer ret = keyword_records.get(record_it);
		advance();
		return ret;
	}

}
